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

package com.calclab.emite.xep.muc;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Map;

import com.calclab.emite.base.util.XmppDateTime;
import com.calclab.emite.base.xml.XMLPacket;
import com.calclab.emite.core.IQCallback;
import com.calclab.emite.core.XmppNamespaces;
import com.calclab.emite.core.XmppURI;
import com.calclab.emite.core.events.BeforeMessageReceivedEvent;
import com.calclab.emite.core.events.BeforeMessageSentEvent;
import com.calclab.emite.core.events.MessageReceivedEvent;
import com.calclab.emite.core.events.MessageSentEvent;
import com.calclab.emite.core.events.PresenceReceivedEvent;
import com.calclab.emite.core.events.ChangedEvent.ChangeType;
import com.calclab.emite.core.session.XmppSession;
import com.calclab.emite.core.stanzas.IQ;
import com.calclab.emite.core.stanzas.Message;
import com.calclab.emite.core.stanzas.Presence;
import com.calclab.emite.core.stanzas.Stanza;
import com.calclab.emite.xep.muc.events.BeforeRoomInvitationSentEvent;
import com.calclab.emite.xep.muc.events.OccupantChangedEvent;
import com.calclab.emite.xep.muc.events.RoomChatChangedEvent;
import com.calclab.emite.xep.muc.events.RoomInvitationSentEvent;
import com.calclab.emite.xep.muc.events.RoomSubjectChangedEvent;
import com.google.common.collect.Maps;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * A Room implementation. You can create rooms using RoomManager.
 * 
 * @see RoomChatManager
 */
public final class RoomChat {
	
	private static enum RoomStatus {
		ready, locked;
	}
	
	private final RoomChatManagerImpl roomManager;
	private final EventBus eventBus;
	private final XmppSession session;
	
	private final XmppURI roomUri;
	private final XmppURI initiatorUri;
	
	private final Map<XmppURI, Occupant> occupantsByOccupantUri;
	private final Map<XmppURI, Occupant> occupantsByUserUri;
	
	private RoomStatus status;
	
	protected RoomChat(final RoomChatManagerImpl roomManager, final EventBus eventBus, final XmppSession session, final XmppURI roomUri, final XmppURI initiatorUri) {
		this.roomManager = checkNotNull(roomManager);
		this.eventBus = checkNotNull(eventBus);
		this.session = checkNotNull(session);
		this.roomUri = checkNotNull(roomUri);
		this.initiatorUri = checkNotNull(initiatorUri);

		occupantsByOccupantUri = Maps.newLinkedHashMap();
		occupantsByUserUri = Maps.newLinkedHashMap();
		
		status = RoomStatus.locked;
	}

	protected void receivePresence(final Presence presence) {
		final XmppURI occupantURI = presence.getFrom();
		final Presence.Type type = presence.getType();
		if (Presence.Type.unavailable.equals(type)) {
			removeOccupant(occupantURI);
			if (occupantURI.equalsNoResource(session.getCurrentUserURI())) {
				status = RoomStatus.locked;
				roomManager.closeRoom(this);
				eventBus.fireEventFromSource(new RoomChatChangedEvent(ChangeType.closed, this), roomManager);
			}
		} else if (!Presence.Type.error.equals(type)) {
			final XMLPacket xmuc = presence.getExtension("x", XmppNamespaces.MUC_USER);
			if (xmuc != null) {
				final XMLPacket item = xmuc.getFirstChild("item");
				final String affiliation = item.getAttribute("affiliation");
				final String role = item.getAttribute("role");
				final XmppURI userUri = XmppURI.uri(item.getAttribute("jid"));
				setOccupantPresence(userUri, occupantURI, affiliation, role, presence.getShow(), presence.getStatus());
				if (hasStatus(xmuc, 201)) {
					final IQ iq = new IQ(IQ.Type.set);
					iq.setTo(roomUri.getJID());
					iq.addChild("query", XmppNamespaces.MUC_OWNER).addChild("x", "jabber:x:data").setAttribute("type", "submit");

					session.sendIQ("rooms", iq, new IQCallback() {
						@Override
						public void onIQSuccess(final IQ iq) {
							status = RoomStatus.ready;
							eventBus.fireEventFromSource(new RoomChatChangedEvent(ChangeType.opened, RoomChat.this), roomManager);
						}

						@Override
						public void onIQFailure(final IQ iq) {
						}
					});
				} else {
					status = RoomStatus.ready;
					eventBus.fireEventFromSource(new RoomChatChangedEvent(ChangeType.opened, this), roomManager);
				}
			}
		}
		
		eventBus.fireEventFromSource(new PresenceReceivedEvent(presence), this);
	}

	protected void receiveMessage(final Message message) {
		if (message.getSubject() != null) {
			eventBus.fireEventFromSource(new RoomSubjectChangedEvent(message.getFrom(), message.getSubject()), this);
			return;
		}
		
		eventBus.fireEventFromSource(new BeforeMessageReceivedEvent(message), this);
		eventBus.fireEventFromSource(new MessageReceivedEvent(message), this);
	}
	
	/**
	 * Add a handler to know when a message is received. It allows the listener
	 * to modify the message just before the receive event (a kind of
	 * interceptor in aop programming)
	 * 
	 * @param handler
	 *            the message handler
	 */
	public HandlerRegistration addBeforeMessageReceivedHandler(final BeforeMessageReceivedEvent.Handler handler) {
		return eventBus.addHandlerToSource(BeforeMessageReceivedEvent.TYPE, this, handler);
	}
	
	/**
	 * Add a handler to know when a message is received in this chat
	 * 
	 * @param handler
	 * @return a handler registration object to detach the handler
	 */
	public HandlerRegistration addMessageReceivedHandler(final MessageReceivedEvent.Handler handler) {
		return eventBus.addHandlerToSource(MessageReceivedEvent.TYPE, this, handler);
	}

	/**
	 * A a handler to know when a message is going to be sent. It allows the
	 * listener to modify the message just before send it (a kind of interceptor
	 * in aop programming)
	 * 
	 * @param handler
	 *            the message handler
	 */
	public HandlerRegistration addBeforeMessageSentHandler(final BeforeMessageSentEvent.Handler handler) {
		return eventBus.addHandlerToSource(BeforeMessageSentEvent.TYPE, this, handler);
	}
	
	/**
	 * Add a handler to know when this chat has sent a message
	 * 
	 * @param handler
	 *            the message handler
	 * @return a handler registration object to detach the handler
	 * 
	 */
	public HandlerRegistration addMessageSentHandler(final MessageSentEvent.Handler handler) {
		return eventBus.addHandlerToSource(MessageSentEvent.TYPE, this, handler);
	}

	/**
	 * Adds a handler to know when a presence arrives to this room
	 * 
	 * @param handler
	 * @return
	 */
	public HandlerRegistration addPresenceReceivedHandler(final PresenceReceivedEvent.Handler handler) {
		return eventBus.addHandlerToSource(PresenceReceivedEvent.TYPE, this, handler);
	}
	
	/**
	 * Adds a handler to know when a OccupantChangedEvent ocurs. This is used
	 * know when a user enter or exites the room, for example.
	 * 
	 * @param handler
	 * @return
	 */
	public HandlerRegistration addOccupantChangedHandler(final OccupantChangedEvent.Handler handler) {
		return eventBus.addHandlerToSource(OccupantChangedEvent.TYPE, this, handler);
	}

	/**
	 * Adds a handler to know when a invitation to this room is going to be
	 * sent. This handler allows to decorate the Message object before is sent
	 * 
	 * @param handler
	 * @return
	 */
	public HandlerRegistration addBeforeRoomInvitationSentHandler(final BeforeRoomInvitationSentEvent.Handler handler) {
		return eventBus.addHandlerToSource(BeforeRoomInvitationSentEvent.TYPE, this, handler);
	}

	/**
	 * Add a handler to know when a room invitation has been sent
	 * 
	 * @param handler
	 * @return
	 */
	public HandlerRegistration addRoomInvitationSentHandler(final RoomInvitationSentEvent.Handler handler) {
		return eventBus.addHandlerToSource(RoomInvitationSentEvent.TYPE, this, handler);
	}

	public HandlerRegistration addRoomSubjectChangedHandler(final RoomSubjectChangedEvent.Handler handler) {
		return eventBus.addHandlerToSource(RoomSubjectChangedEvent.TYPE, this, handler);
	}
	
	protected void addOccupant(final Occupant occupant) {
		occupantsByOccupantUri.put(occupant.getOccupantUri(), occupant);
		final XmppURI userUri = occupant.getUserUri();
		if (userUri != null) {
			occupantsByUserUri.put(userUri.getJID(), occupant);
		}
		eventBus.fireEventFromSource(new OccupantChangedEvent(ChangeType.added, occupant), this);
	}

	protected void removeOccupant(final XmppURI occupantUri) {
		final Occupant occupant = occupantsByOccupantUri.remove(occupantUri);
		if (occupant != null) {
			final XmppURI userUri = occupant.getUserUri();
			if (userUri != null) {
				occupantsByUserUri.remove(userUri.getJID());
			}
			eventBus.fireEventFromSource(new OccupantChangedEvent(ChangeType.removed, occupant), this);
		}
	}

	protected void open(final HistoryOptions historyOptions) {
		session.send(createEnterPresence(historyOptions));
	}

	public void close(final String exitStatus) {
		if (RoomStatus.ready.equals(status)) {
			final Presence exitPresence = new Presence(Presence.Type.unavailable, roomUri);
			if (exitStatus != null) {
				exitPresence.setStatus(exitStatus);
			}
			session.send(exitPresence);
		}
	}

	/**
	 * To check if is an echo message
	 * 
	 * @param message
	 * @return true if this message is a room echo
	 */
	public boolean isComingFromMe(final Message message) {
		final String myNick = roomUri.getResource();
		final String messageNick = message.getFrom().getResource();
		return myNick.equals(messageNick);
	}

	public boolean isUserMessage(final Message message) {
		final String resource = message.getFrom().getResource();
		return resource != null && !"".equals(resource);
	}
	
	// TODO: check occupants affiliation to see if the user can do that!!
	/**
	 * Request a subject change on the given room
	 * 
	 * @param room
	 * @param subjectText
	 */
	public void requestSubjectChange(final String subjectText) {
		final Message message = new Message();
		message.setTo(roomUri.getJID());
		message.setType(Message.Type.groupchat);
		message.setSubject(subjectText);
		session.send(message);
	}

	/**
	 * Update my status to other occupants.
	 * 
	 * @param statusMessage
	 * @param show
	 */
	public void setStatus(final String statusMessage, final Presence.Show show) {
		final Presence presence = new Presence();
		presence.setStatus(statusMessage);
		presence.setShow(show);
		presence.setTo(roomUri);
		// presence.addChild("x", XmppNamespaces.MUC);
		// presence.setPriority(0);
		session.send(presence);
	}
	
	public void send(final Message message) {
		message.setTo(roomUri.getJID());
		message.setType(Message.Type.groupchat);
		
		eventBus.fireEventFromSource(new BeforeMessageSentEvent(message), this);
		session.send(message);
		eventBus.fireEventFromSource(new MessageSentEvent(message), this);
	}

	public void sendPrivateMessage(final Message message, final String nick) {
		message.setTo(XmppURI.uri(roomUri.getNode(), roomUri.getHost(), nick));
		message.setType(Message.Type.chat);
		
		eventBus.fireEventFromSource(new BeforeMessageSentEvent(message), this);
		session.send(message);
		eventBus.fireEventFromSource(new MessageSentEvent(message), this);
	}
	
	/**
	 * 
	 * http://www.xmpp.org/extensions/xep-0045.html#invite
	 * 
	 * @param userJid
	 *            user to invite
	 * @param reasonText
	 *            reason for the invitation
	 */
	public void sendInvitationTo(final XmppURI userJid, final String reasonText) {
		final Stanza message = new Message();
		message.setTo(roomUri.getJID());
		final XMLPacket invite = message.getXML().addChild("x", XmppNamespaces.MUC_USER).addChild("invite");
		invite.setAttribute("to", userJid.toString());
		invite.setChildText("reason", reasonText);
		
		eventBus.fireEventFromSource(new BeforeRoomInvitationSentEvent(message, invite), this);
		session.send(message);
		eventBus.fireEventFromSource(new RoomInvitationSentEvent(userJid, reasonText), this);
	}
	
	/**
	 * Get the chats uri. Is the other side of the conversation in a PairChat,
	 * or the room uri in a RoomChat
	 * 
	 * @return
	 */
	public XmppURI getRoomURI() {
		return roomUri;
	}
	
	/**
	 * This is the uri of the entity that initiated the chat
	 * 
	 * @return
	 */
	public XmppURI getInitiatorUri() {
		return initiatorUri;
	}
	
	public boolean isInitiatedByMe() {
		return initiatorUri.equals(session.getCurrentUserURI());
	}
	
	/**
	 * Find an occupant with the given occupant uri
	 * 
	 * @param occupantUri
	 *            occupant uri is the room jid with nick name as resource
	 * @return the occupant if found, null if not
	 */
	public Occupant getOccupantByOccupantUri(final XmppURI occupantUri) {
		return occupantsByOccupantUri.get(occupantUri);
	}

	/**
	 * Find an occupant with the given user jid
	 * 
	 * @param userUri
	 *            the user's uri (resource is ignored)
	 * @return the occupant if found, null if not
	 */
	public Occupant getOccupantByUserUri(final XmppURI userUri) {
		return occupantsByUserUri.get(userUri.getJID());
	}

	/**
	 * Return the occupants of this room. THIS IS THE ACTUAL BACKEND
	 * IMPLEMENTATION. DO NOT MODIFY
	 * 
	 * @return
	 */
	public Collection<Occupant> getOccupants() {
		return occupantsByOccupantUri.values();
	}

	/**
	 * Return the current number of occupants of this room
	 * 
	 * @return
	 */
	public int getOccupantsCount() {
		return occupantsByOccupantUri.size();
	}

	@Override
	public String toString() {
		return "ROOM: " + roomUri.toString();
	}

	/**
	 * Created a enter presence object based on the history options given
	 * 
	 * @param historyOptions
	 * @return
	 */
	private Presence createEnterPresence(final HistoryOptions historyOptions) {
		final Presence presence = new Presence(null, roomUri);
		final XMLPacket x = presence.getXML().addChild("x", XmppNamespaces.MUC);
		presence.setPriority(0);
		if (historyOptions != null) {
			final XMLPacket h = x.addChild("history");
			if (historyOptions.getMaxChars() >= 0) {
				h.setAttribute("maxchars", Integer.toString(historyOptions.getMaxChars()));
			}
			if (historyOptions.getMaxStanzas() >= 0) {
				h.setAttribute("maxstanzas", Integer.toString(historyOptions.getMaxStanzas()));
			}
			if (historyOptions.getSeconds() >= 0) {
				h.setAttribute("seconds", Long.toString(historyOptions.getSeconds()));
			}
			if (historyOptions.getSince() != null) {
				h.setAttribute("since", XmppDateTime.formatXMPPDateTime(historyOptions.getSince()));
			}
		}
		return presence;
	}

	private static boolean hasStatus(final XMLPacket xtension, final int code) {
		for (final XMLPacket child : xtension.getChildren("status")) {
			if (String.valueOf(code).equals(child.getAttribute("code")))
				return true;
		}
		
		return false;
	}

	private Occupant setOccupantPresence(final XmppURI userUri, final XmppURI occupantUri, final String affiliation, final String role, final Presence.Show show, final String statusMessage) {
		Occupant occupant = getOccupantByOccupantUri(occupantUri);
		if (occupant == null) {
			occupant = new Occupant(occupantUri, userUri, affiliation, role, show, statusMessage);
			addOccupant(occupant);
		} else {
			occupant.setAffiliation(affiliation);
			occupant.setRole(role);
			occupant.setShow(show);
			occupant.setStatusMessage(statusMessage);
			eventBus.fireEventFromSource(new OccupantChangedEvent(ChangeType.modified, occupant), this);
		}
		return occupant;
	}

}
