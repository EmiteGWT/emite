/*
 * ((e)) emite: A pure Google Web Toolkit XMPP library
 * Copyright (c) 2008-2011 The Emite development team
 * 
 * This file is part of Emite.
 *
 * Emite is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * Emite is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with Emite.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.calclab.emite.xep.mucchatstate.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.calclab.emite.core.client.events.BeforeMessageSentEvent;
import com.calclab.emite.core.client.events.MessageReceivedEvent;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.NoPacket;
import com.calclab.emite.core.client.packet.PacketMatcher;
import com.calclab.emite.core.client.stanzas.Message;
import com.calclab.emite.core.client.stanzas.XmppURI;
import com.calclab.emite.xep.chatstate.client.ChatStateHook.ChatState;
import com.calclab.emite.xep.muc.client.RoomChat;
import com.google.gwt.user.client.Timer;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * XEP-0085: Chat State Notifications
 * http://www.xmpp.org/extensions/xep-0085.html (Version: 1.2)
 */
public class RoomChatStateManager implements BeforeMessageSentEvent.Handler, MessageReceivedEvent.Handler {

	private static final Logger logger = Logger.getLogger(RoomChatStateManager.class.getName());
	
	public static final String XMLNS = "http://jabber.org/protocol/chatstates";
	public static final String KEY = "RoomChatStateManager";
	
	/*
	 * REUSING ChatStateManager.ChatState
	 * http://xmpp.org/extensions/xep-0085.html#bizrules-groupchat # A client
	 * SHOULD NOT generate <gone/> notifications. # A client SHOULD ignore
	 * <gone/> notifications received from other room occupants.
	 */

	private final static List<String> stateString;
	static {
		stateString = new ArrayList<String>(4);
		for (final ChatState s : ChatState.values()) {
			stateString.add(s.name());
		}
	}

	private final EventBus eventBus;
	private final RoomChat room;
	
	private final Map<XmppURI, ChatState> othersState;
	private ChatState ownState;
	
	private final int inactiveDelay = 120 * 1000; // 2 minutes
	private final int pauseDelay = 10 * 1000; // 10 secondes

	private PacketMatcher bodySubjectThreadMatchter = new PacketMatcher() {
		@Override
		public boolean matches(final IPacket packet) {
			final String nn = packet.getName();
			return "body".equals(nn) || "subject".equals(nn) || "thread".equals(nn);
		}
	};
	
	/**
	 * From http://xmpp.org/extensions/xep-0085.html#bizrules-syntax
	 * <ul>
	 * <li>A message stanza MUST NOT contain more than one child element
	 * qualified by the 'http://jabber.org/protocol/chatstates' namespace.
	 * 
	 * <li>A message stanza that contains standard instant messaging content
	 * SHOULD NOT contain a chat state notification extension other than
	 * <active/>, where "standard instant messaging content" is taken to mean
	 * the <body/>, <subject/>, and <thread/> child elements defined in XMPP IM
	 * [7] or any other child element that would lead the recipient to treat the
	 * stanza as an instant message as explained in Message Stanza Profiles [8].
	 * 
	 * <li>A message stanza that does not contain standard messaging content and
	 * is intended to specify only the chat state MUST NOT contain any child
	 * elements other than the chat state notification extension, which SHOULD
	 * be a state other than <active/>; however, if threads are used (see below)
	 * then the standalone notification MUST also contain the <thread/> element.
	 * </ul>
	 */

	private final Timer inactiveTimer = new Timer() {
		@Override
		public void run() {
			setOwnState(ChatState.inactive);
		}
	};

	private final Timer pauseTimer = new Timer() {
		@Override
		public void run() {
			setOwnState(ChatState.pause);
		}
	};

	public RoomChatStateManager(final EventBus eventBus, final RoomChat room) {
		this.eventBus = eventBus;
		this.room = room;
		othersState = new HashMap<XmppURI, ChatState>();

		room.addBeforeMessageSentHandler(this);
		room.addMessageReceivedHandler(this);
	}
	
	@Override
	public void onBeforeMessageSent(final BeforeMessageSentEvent event) {
		// Only Messages are listened not presence events
		// But sendStateMessage goes through here.
		final Message message = event.getMessage();
		final boolean alreadyWithState = getStateFromMessage(message) != null;
		if (!alreadyWithState && ownState != ChatState.active && NoPacket.INSTANCE != message.getFirstChild(bodySubjectThreadMatchter)) {
			if (ownState == ChatState.composing) {
				pauseTimer.cancel();
			}

			logger.finer("Setting own status to: " + ownState + " because we send a body or a subject");
			ownState = ChatState.active;
			message.addChild(ChatState.active.toString(), XMLNS);
		}
		if (ownState != ChatState.inactive) {
			inactiveTimer.schedule(inactiveDelay);
		}
	}
	
	@Override
	public void onMessageReceived(final MessageReceivedEvent event) {
		final Message message = event.getMessage();
		final ChatState state = getStateFromMessage(message);
		if (state != null) {
			final XmppURI from = message.getFrom();
			othersState.put(from, state);
			eventBus.fireEventFromSource(new RoomChatStateNotificationEvent(message.getFrom(), state), this);
		}
	}

	public HandlerRegistration addRoomChatStateNotificationHandler(final RoomChatStateNotificationEvent.Handler handler) {
		return eventBus.addHandlerToSource(RoomChatStateNotificationEvent.TYPE, this, handler);
	}

	public ChatState getOtherState(final XmppURI occupantUri) {
		final ChatState state = othersState.get(occupantUri);
		return state == null ? ChatState.active : state;
	}

	public ChatState getOwnState() {
		return ownState;
	}

	public void setOwnState(final ChatState chatState) {
		// From XEP: a client MUST NOT send a second instance of any given
		// standalone notification (i.e., a standalone notification MUST be
		// followed by a different state, not repetition of the same state).
		// However, every content message SHOULD contain an <active/>
		// notification.
		if (ownState == null || !ownState.equals(chatState)) {
			ownState = chatState;
			logger.finer("Setting own status to: " + chatState.toString());
			final Message message = new Message(null, room.getURI());
			message.addChild(chatState.toString(), XMLNS);
			room.send(message);
		}
		if (ownState == ChatState.composing) {
			pauseTimer.schedule(pauseDelay);
		}
	}

	private static ChatState getStateFromMessage(final Message message) {
		final IPacket stateNode = message.getFirstChild(new PacketMatcher() {
			@Override
			public boolean matches(final IPacket packet) {
				final boolean vn = stateString.contains(packet.getName());
				return vn;
				/*
				 * Namespaces don't work String ns =
				 * message.getAttribute("xmlns"); ns = (ns != null ? ns :
				 * message.getAttributes().get("xmlns")); return vn &&
				 * XMLNS.equals(ns);
				 */
			}
		});
		return stateNode == NoPacket.INSTANCE ? null : ChatState.valueOf(stateNode.getName());
	}

}
