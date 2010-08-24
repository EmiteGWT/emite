/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008-2010 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emite.xep.mucchatstate.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.NoPacket;
import com.calclab.emite.core.client.packet.PacketMatcher;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xep.chatstate.client.ChatStateManager.ChatState;
import com.calclab.emite.xep.muc.client.Room;
import com.calclab.emite.xep.mucchatstate.client.events.RoomChatStateNotificationEvent;
import com.calclab.emite.xep.mucchatstate.client.events.RoomChatStateNotificationHandler;
import com.calclab.suco.client.events.Listener;
import com.calclab.suco.client.events.Listener2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;

/**
 * XEP-0085: Chat State Notifications
 * http://www.xmpp.org/extensions/xep-0085.html (Version: 1.2)
 */
public class RoomChatStateManager {

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

    public static final String XMLNS = "http://jabber.org/protocol/chatstates";
    public static final String KEY = "RoomChatStateManager";

    private ChatState ownState;
    private final Map<XmppURI, ChatState> othersState;
    private final Room room;
    private final int inactiveDelay = 120 * 1000; // 2 minutes
    private final int pauseDelay = 10 * 1000; // 10 secondes

    PacketMatcher bodySubjectThreadMatchter = new PacketMatcher() {
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
    final Listener<Message> doBeforeSend = new Listener<Message>() {
	// Only Messages are listened not presence events
	// But sendStateMessage goes through here.
	public void onEvent(final Message message) {
	    final boolean alreadyWithState = getStateFromMessage(message) != null;
	    if (!alreadyWithState && ownState != ChatState.active
		    && NoPacket.INSTANCE != message.getFirstChild(bodySubjectThreadMatchter)) {
		if (ownState == ChatState.composing) {
		    pauseTimer.cancel();
		}

		GWT.log("Setting own status to: " + ownState + " because we send a body or a subject", null);
		ownState = ChatState.active;
		message.addChild(ChatState.active.toString(), XMLNS);
	    }
	    if (ownState != ChatState.inactive) {
		inactiveTimer.schedule(inactiveDelay);
	    }
	}
    };

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

    public RoomChatStateManager(final Room room) {
	this.room = room;
	othersState = new HashMap<XmppURI, ChatState>();
	room.onMessageReceived(new Listener<Message>() {
	    public void onEvent(final Message message) {
		final ChatState state = getStateFromMessage(message);
		if (state != null) {
		    final XmppURI from = message.getFrom();
		    othersState.put(from, state);
		    room.getChatEventBus().fireEvent(new RoomChatStateNotificationEvent(message.getFrom(), state));
		}
	    }
	});
    }

    public HandlerRegistration addRoomChatStateNotificationHandler(RoomChatStateNotificationHandler handler) {
	return RoomChatStateNotificationEvent.bind(room.getChatEventBus(), handler);
    }

    public ChatState getOtherState(final XmppURI occupantUri) {
	final ChatState state = othersState.get(occupantUri);
	return state == null ? ChatState.active : state;
    }

    public ChatState getOwnState() {
	return ownState;
    }

    public void onChatStateChanged(final Listener2<XmppURI, ChatState> listener) {
	addRoomChatStateNotificationHandler(new RoomChatStateNotificationHandler() {
	    @Override
	    public void onRoomChatStateNotification(RoomChatStateNotificationEvent event) {
		listener.onEvent(event.getFrom(), event.getChatState());
	    }
	});
    }

    public void setOwnState(final ChatState chatState) {
	// From XEP: a client MUST NOT send a second instance of any given
	// standalone notification (i.e., a standalone notification MUST be
	// followed by a different state, not repetition of the same state).
	// However, every content message SHOULD contain an <active/>
	// notification.
	if (ownState == null || !ownState.equals(chatState)) {
	    ownState = chatState;
	    GWT.log("Setting own status to: " + chatState.toString(), null);
	    final Message message = new Message(null, room.getURI());
	    message.addChild(chatState.toString(), XMLNS);
	    room.send(message);
	}
	if (ownState == ChatState.composing) {
	    pauseTimer.schedule(pauseDelay);
	}
    }

    private ChatState getStateFromMessage(final Message message) {
	final IPacket stateNode = message.getFirstChild(new PacketMatcher() {
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
