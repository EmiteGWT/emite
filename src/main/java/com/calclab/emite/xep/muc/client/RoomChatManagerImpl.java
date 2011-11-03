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

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import javax.annotation.Nullable;

import com.calclab.emite.core.client.events.ChangedEvent.ChangeType;
import com.calclab.emite.core.client.events.MessageReceivedEvent;
import com.calclab.emite.core.client.events.PresenceReceivedEvent;
import com.calclab.emite.core.client.events.SessionStatusChangedEvent;
import com.calclab.emite.core.client.session.SessionStatus;
import com.calclab.emite.core.client.session.XmppSession;
import com.calclab.emite.core.client.stanzas.Message;
import com.calclab.emite.core.client.stanzas.Presence;
import com.calclab.emite.core.client.uri.XmppURI;
import com.calclab.emite.core.client.xml.XMLPacket;
import com.calclab.emite.xep.muc.client.events.RoomChatChangedEvent;
import com.calclab.emite.xep.muc.client.events.RoomInvitationReceivedEvent;
import com.google.common.collect.Maps;
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
public final class RoomChatManagerImpl implements RoomChatManager, SessionStatusChangedEvent.Handler, PresenceReceivedEvent.Handler, MessageReceivedEvent.Handler {

	private final EventBus eventBus;
	private final XmppSession session;
	private final Map<XmppURI, RoomChat> roomsByJID;

	@Nullable private XmppURI currentChatUser;
	
	@Inject
	protected RoomChatManagerImpl(@Named("emite") final EventBus eventBus, final XmppSession session) {
		this.eventBus = checkNotNull(eventBus);
		this.session = checkNotNull(session);
		roomsByJID = Maps.newHashMap();
		
		// Control chat status when the user logout and login again
		session.addSessionStatusChangedHandler(this, true);

		// Forward the presence messages to the room event bus
		session.addPresenceReceivedHandler(this);

		// Check if the incoming message is a room invitation to the user
		session.addMessageReceivedHandler(this);
	}

	@Override
	public final HandlerRegistration addRoomChatChangedHandler(final RoomChatChangedEvent.Handler handler) {
		return eventBus.addHandlerToSource(RoomChatChangedEvent.TYPE, this, handler);
	}
	
	@Override
	public final HandlerRegistration addRoomInvitationReceivedHandler(final RoomInvitationReceivedEvent.Handler handler) {
		return eventBus.addHandlerToSource(RoomInvitationReceivedEvent.TYPE, this, handler);
	}

	@Override
	public final void onSessionStatusChanged(final SessionStatusChangedEvent event) {
		if (SessionStatus.loggedIn.equals(event.getStatus())) {
			final XmppURI currentUser = session.getCurrentUserURI();
			if (currentChatUser == null) {
				currentChatUser = currentUser;
			}
			if (currentUser.equalsNoResource(currentChatUser)) {
				for (final RoomChat chat : roomsByJID.values()) {
					chat.open(null);
				}
			}
		} else if (SessionStatus.loggingOut.equals(event.getStatus()) || SessionStatus.disconnected.equals(event.getStatus())) {
			// check both status: loggingOut is preferred, but not
			// always fired (i.e. error)
			for (final RoomChat chat : roomsByJID.values()) {
				chat.close();
			}
		}
	}
	
	@Override
	public final void onMessageReceived(final MessageReceivedEvent event) {
		final Message message = event.getMessage();
		
		final XMLPacket x = message.getExtension("x", "http://jabber.org/protocol/muc#user");
		if (x != null && x.hasChild("invite")) {
			final XMLPacket invite = x.getFirstChild("invite");
			final RoomInvitation invitation = new RoomInvitation(XmppURI.uri(invite.getAttribute("from")), message.getFrom(), invite.getChildText("reason"));
			eventBus.fireEventFromSource(new RoomInvitationReceivedEvent(invitation), this);
			return;
		}
		
		final RoomChat chat = getRoom(message.getFrom());
		if (chat != null) {
			chat.receiveMessage(message);
		}
	}
	
	@Override
	public final void onPresenceReceived(final PresenceReceivedEvent event) {
		final Presence presence = event.getPresence();
		final RoomChat chat = getRoom(presence.getFrom());
		if (chat != null) {
			chat.receivePresence(presence);
		}
	}

	@Override
	public final RoomChat acceptRoomInvitation(final RoomInvitation invitation, @Nullable final HistoryOptions historyOptions) {
		final XmppURI roomURI = invitation.getRoomURI();
		final XmppURI uri = XmppURI.uri(roomURI.getNode(), roomURI.getHost(), session.getCurrentUserURI().getNode());

		return openRoom(uri, historyOptions);
	}

	@Override
	public final RoomChat openRoom(final XmppURI uri, final HistoryOptions historyOptions) {
		RoomChat chat = getRoom(uri);
		if (chat == null) {
			chat = new RoomChat(eventBus, session, this, uri, session.getCurrentUserURI());
			roomsByJID.put(uri.getJID(), chat);
			eventBus.fireEventFromSource(new RoomChatChangedEvent(ChangeType.created, chat), this);
		}
		
		chat.open(historyOptions);
		eventBus.fireEventFromSource(new RoomChatChangedEvent(ChangeType.opened, chat), this);
		return chat;
	}

	protected final boolean closeRoom(final RoomChat room) {
		if (roomsByJID.remove(room.getRoomURI().getJID()) == null)
			return false;
		
		eventBus.fireEventFromSource(new RoomChatChangedEvent(ChangeType.closed, room), this);
		return true;
	}

	@Override
	public final RoomChat getRoom(final XmppURI uri) {
		return roomsByJID.get(uri.getJID());
	}

	@Override
	public final Collection<RoomChat> getRooms() {
		return Collections.unmodifiableCollection(roomsByJID.values());
	}
	
}
