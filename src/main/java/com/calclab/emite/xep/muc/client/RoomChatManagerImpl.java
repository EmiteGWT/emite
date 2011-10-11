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

package com.calclab.emite.xep.muc.client;

import java.util.HashMap;

import com.calclab.emite.core.client.events.MessageReceivedEvent;
import com.calclab.emite.core.client.events.PresenceReceivedEvent;
import com.calclab.emite.core.client.events.ChangedEvent.ChangeType;
import com.calclab.emite.core.client.session.XmppSession;
import com.calclab.emite.core.client.stanzas.Message;
import com.calclab.emite.core.client.stanzas.Presence;
import com.calclab.emite.core.client.stanzas.XmppURI;
import com.calclab.emite.core.client.xml.XMLPacket;
import com.calclab.emite.im.client.chat.ChatManagerBoilerplate;
import com.calclab.emite.im.client.chat.ChatProperties;
import com.calclab.emite.im.client.chat.ChatSelectionStrategy;
import com.calclab.emite.im.client.chat.ChatStatus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * The default implementation of RoomManager. See RoomManager for javadoc.
 * 
 * @see RoomChatManager
 */
@Singleton
public class RoomChatManagerImpl extends ChatManagerBoilerplate<RoomChat> implements RoomChatManager, PresenceReceivedEvent.Handler, MessageReceivedEvent.Handler {

	private static final String HISTORY_OPTIONS_PROP = "history.options";
	
	private final HashMap<XmppURI, RoomChat> roomsByJID;
	private HistoryOptions defaultHistoryOptions;

	@Inject
	public RoomChatManagerImpl(@Named("emite") final EventBus eventBus, final XmppSession session, @Named("Room") final ChatSelectionStrategy strategy) {
		super(eventBus, session, strategy);
		roomsByJID = new HashMap<XmppURI, RoomChat>();

		// Forward the presence messages to the room event bus
		session.addPresenceReceivedHandler(this);
		
		// Check if the incomming message is a room invitation to the user
		// TODO: ChatManagerBoilerplate already has a onMessageReceived
		//session.addMessageReceivedHandler(this);
	}
	
	@Override
	public HandlerRegistration addRoomChatChangedHandler(RoomChatChangedEvent.Handler handler) {
		return eventBus.addHandlerToSource(RoomChatChangedEvent.TYPE, this, handler);
	}
	
	@Override
	protected void fireChanged(ChangeType type, RoomChat chat) {
		eventBus.fireEventFromSource(new RoomChatChangedEvent(type, chat), this);
	}
	
	@Override
	public void onPresenceReceived(final PresenceReceivedEvent event) {
		final Presence presence = event.getPresence();
		final ChatProperties properties = strategy.extractProperties(presence);
		RoomChat chat = getChat(properties, false);
		if (chat == null && properties.shouldCreateNewChat()) {
			// we need to create a chat for this incoming presence
			properties.setInitiatorUri(properties.getUri());
			chat = addNewChat(properties);
		}
		if (chat != null) {
			eventBus.fireEventFromSource(new PresenceReceivedEvent(presence), chat);
		}
	}

	@Override
	public void onMessageReceived(final MessageReceivedEvent event) {
		super.onMessageReceived(event);
		final Message message = event.getMessage();
		XMLPacket child = message.getXML().getFirstChild("x", "http://jabber.org/protocol/muc#user").getFirstChild("invite");
		if (child != null) {
			// We extract the chat properties from the message
			final ChatProperties chatProperties = strategy.extractProperties(message);

			final RoomInvitation invitation = new RoomInvitation(XmppURI.uri(child.getAttribute("from")), message.getFrom(), child.getChildText("reason"), chatProperties);
			eventBus.fireEventFromSource(new RoomInvitationReceivedEvent(invitation), this);
		}
	}
	
	@Override
	public RoomChat acceptRoomInvitation(final RoomInvitation invitation) {
		final XmppURI roomURI = invitation.getRoomURI();
		final XmppURI uri = XmppURI.uri(roomURI.getNode(), roomURI.getHost(), session.getCurrentUserURI().getNode());

		final ChatProperties properties = new ChatProperties(uri, invitation.getChatProperties());

		return openChat(properties, true);
	}

	@Override
	public HandlerRegistration addRoomInvitationReceivedHandler(final RoomInvitationReceivedEvent.Handler handler) {
		return eventBus.addHandlerToSource(RoomInvitationReceivedEvent.TYPE, this, handler);
	}

	@Override
	public void close(final RoomChat whatToClose) {
		final RoomChat room = roomsByJID.remove(whatToClose.getURI().getJID());
		if (room != null) {
			room.close();
			super.close(room);
		}
	}

	@Override
	public RoomChat getChat(final XmppURI uri) {
		return roomsByJID.get(uri.getJID());
	}

	@Override
	public HistoryOptions getDefaultHistoryOptions() {
		return defaultHistoryOptions;
	}

	@Override
	public RoomChat open(final XmppURI uri, final HistoryOptions historyOptions) {
		final ChatProperties properties = new ChatProperties(uri);
		properties.setData(HISTORY_OPTIONS_PROP, historyOptions);
		return openChat(properties, true);
	}

	@Override
	public void setDefaultHistoryOptions(final HistoryOptions defaultHistoryOptions) {
		this.defaultHistoryOptions = defaultHistoryOptions;
	}

	@Override
	protected void addChat(final RoomChat chat) {
		final XmppURI jid = chat.getURI().getJID();
		roomsByJID.put(jid, chat);
		super.addChat(chat);
	}

	@Override
	protected RoomChat createChat(final ChatProperties properties) {
		if (properties.getStatus() == null) {
			properties.setStatus(ChatStatus.locked);
		}
		return new RoomChatImpl(eventBus, session, properties);
	}
}
