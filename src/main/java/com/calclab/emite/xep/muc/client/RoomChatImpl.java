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

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.calclab.emite.core.client.events.ChangedEvent.ChangeType;
import com.calclab.emite.core.client.events.ErrorEvent;
import com.calclab.emite.core.client.events.MessageReceivedEvent;
import com.calclab.emite.core.client.events.PresenceReceivedEvent;
import com.calclab.emite.core.client.session.IQCallback;
import com.calclab.emite.core.client.session.XmppSession;
import com.calclab.emite.core.client.stanzas.IQ;
import com.calclab.emite.core.client.stanzas.Message;
import com.calclab.emite.core.client.stanzas.Presence;
import com.calclab.emite.core.client.stanzas.Stanza;
import com.calclab.emite.core.client.stanzas.XmppURI;
import com.calclab.emite.core.client.util.XmppDateTime;
import com.calclab.emite.core.client.xml.XMLPacket;
import com.calclab.emite.im.client.chat.ChatBoilerplate;
import com.calclab.emite.im.client.chat.ChatErrors;
import com.calclab.emite.im.client.chat.ChatProperties;
import com.calclab.emite.im.client.chat.ChatStatus;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * A Room implementation. You can create rooms using RoomManager.
 * 
 * @see RoomChatManager
 */
public class RoomChatImpl extends ChatBoilerplate implements RoomChat, PresenceReceivedEvent.Handler, MessageReceivedEvent.Handler {

	private final Map<XmppURI, Occupant> occupantsByOccupantUri;
	private final Map<XmppURI, Occupant> occupantsByUserUri;

	/**
	 * Create a new room with the given properties. Room are created by
	 * RoomManagers
	 * 
	 * @param session
	 *            the session
	 * @param roomURI
	 *            the room uri with the nick specified in the resource part
	 */
	protected RoomChatImpl(final EventBus eventBus, final XmppSession session, final ChatProperties properties) {
		super(eventBus, session, properties);

		occupantsByOccupantUri = new LinkedHashMap<XmppURI, Occupant>();
		occupantsByUserUri = new LinkedHashMap<XmppURI, Occupant>();

		setStatus(ChatStatus.locked);

		// Perform some room actions when presence received: handle room occupants, create instant rooms...
		addPresenceReceivedHandler(this);

		addMessageReceivedHandler(this);
	}

	@Override
	public void onPresenceReceived(final PresenceReceivedEvent event) {
		final Presence presence = event.getPresence();
		final XmppURI occupantURI = presence.getFrom();
		final Presence.Type type = presence.getType();
		if (type == Presence.Type.error) {
			eventBus.fireEventFromSource(new ErrorEvent(ChatErrors.errorPresence, "We received a presence error", presence), this);
		} else if (type == Presence.Type.unavailable) {
			removeOccupant(occupantURI);
			if (occupantURI.equalsNoResource(session.getCurrentUserURI())) {
				close();
			}
		} else {
			for (final XMLPacket child : presence.getXML().getChildren("x", "http://jabber.org/protocol/muc#user")) {
				final XMLPacket item = child.getFirstChild("item");
				final String affiliation = item.getAttribute("affiliation");
				final String role = item.getAttribute("role");
				final XmppURI userUri = XmppURI.uri(item.getAttribute("jid"));
				setOccupantPresence(userUri, occupantURI, affiliation, role, presence.getShow(), presence.getStatus());
				if (isNewRoom(child)) {
					requestCreateInstantRoom();
				} else {
					setStatus(ChatStatus.ready);
				}
			}
		}
	}

	@Override
	public void onMessageReceived(final MessageReceivedEvent event) {
		super.onMessageReceived(event);
		final Message message = event.getMessage();
		if (message.getSubject() != null) {
			eventBus.fireEventFromSource(new RoomSubjectChangedEvent(message.getFrom(), message.getSubject()), this);
		}
	}

	@Override
	public HandlerRegistration addOccupantChangedHandler(final OccupantChangedEvent.Handler handler) {
		return eventBus.addHandlerToSource(OccupantChangedEvent.TYPE, this, handler);
	}

	@Override
	public HandlerRegistration addPresenceReceivedHandler(final PresenceReceivedEvent.Handler handler) {
		return eventBus.addHandlerToSource(PresenceReceivedEvent.TYPE, this, handler);
	}

	@Override
	public HandlerRegistration addBeforeRoomInvitationSentHandler(final BeforeRoomInvitationSentEvent.Handler handler) {
		return eventBus.addHandlerToSource(BeforeRoomInvitationSentEvent.TYPE, this, handler);
	}

	@Override
	public HandlerRegistration addRoomInvitationSentHandler(final RoomInvitationSentEvent.Handler handler) {
		return eventBus.addHandlerToSource(RoomInvitationSentEvent.TYPE, this, handler);
	}

	@Override
	public HandlerRegistration addRoomSubjectChangedHandler(final RoomSubjectChangedEvent.Handler handler) {
		return eventBus.addHandlerToSource(RoomSubjectChangedEvent.TYPE, this, handler);
	}

	@Override
	public Occupant getOccupantByOccupantUri(final XmppURI occupantUri) {
		return occupantsByOccupantUri.get(occupantUri);
	}

	@Override
	public Occupant getOccupantByUserUri(final XmppURI userUri) {
		return occupantsByUserUri.get(userUri.getJID());
	}

	@Override
	public Collection<Occupant> getOccupants() {
		return occupantsByOccupantUri.values();
	}

	@Override
	public int getOccupantsCount() {
		return occupantsByOccupantUri.size();
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

	@Override
	public void close() {
		if (ChatStatus.ready.equals(properties.getStatus())) {
			session.send(new Presence(Presence.Type.unavailable, null, getURI()));
			properties.setStatus(ChatStatus.locked);
		}
	}

	@Override
	public String getID() {
		return getURI().toString();
	}

	@Override
	public boolean isComingFromMe(final Message message) {
		final String myNick = getURI().getResource();
		final String messageNick = message.getFrom().getResource();
		return myNick.equals(messageNick);
	}

	@Override
	public boolean isUserMessage(final Message message) {
		final String resource = message.getFrom().getResource();
		return resource != null && !"".equals(resource);
	}

	@Override
	public void open() {
		final HistoryOptions historyOptions = (HistoryOptions) properties.getData(HistoryOptions.KEY);
		session.send(createEnterPresence(historyOptions));
	}

	@Override
	public void reEnter(final HistoryOptions historyOptions) {
		if (ChatStatus.locked.equals(getStatus())) {
			session.send(createEnterPresence(historyOptions));
		}
	}

	@Override
	public void send(final Message message) {
		message.setTo(getURI().getJID());
		message.setType(Message.Type.groupchat);
		super.send(message);
	}

	@Override
	public void sendPrivateMessage(final Message message, final String nick) {
		message.setTo(XmppURI.uri(getURI().getNode(), getURI().getHost(), nick));
		message.setType(Message.Type.chat);
		super.send(message);
	}

	@Override
	public void sendInvitationTo(final XmppURI userJid, final String reasonText) {
		final Stanza message = new Message();
		message.setFrom(session.getCurrentUserURI());
		message.setTo(getURI().getJID());
		final XMLPacket invite = message.getXML().addChild("x", "http://jabber.org/protocol/muc#user").addChild("invite");
		invite.setAttribute("to", userJid.toString());
		invite.setChildText("reason", reasonText);
		eventBus.fireEventFromSource(new BeforeRoomInvitationSentEvent(message, invite), this);
		session.send(message);
		eventBus.fireEventFromSource(new RoomInvitationSentEvent(userJid, reasonText), this);
	}

	@Override
	public void setStatus(final String statusMessage, final Presence.Show show) {
		final Presence presence = new Presence();
		presence.setStatus(statusMessage);
		presence.setShow(show);
		presence.setTo(getURI());
		// presence.addChild("x", "http://jabber.org/protocol/muc");
		// presence.setPriority(0);
		session.send(presence);
	}

	@Override
	public String toString() {
		return "ROOM: " + getURI();
	}

	/**
	 * Created a enter presence object based on the history options given
	 * 
	 * @param historyOptions
	 * @return
	 */
	protected Presence createEnterPresence(final HistoryOptions historyOptions) {
		final Presence presence = new Presence(null, null, getURI());
		final XMLPacket x = presence.getXML().addChild("x", "http://jabber.org/protocol/muc");
		presence.setPriority(0);
		if (historyOptions != null) {
			final XMLPacket h = x.addChild("history");
			if (historyOptions.maxchars >= 0) {
				h.setAttribute("maxchars", Integer.toString(historyOptions.maxchars));
			}
			if (historyOptions.maxstanzas >= 0) {
				h.setAttribute("maxstanzas", Integer.toString(historyOptions.maxstanzas));
			}
			if (historyOptions.seconds >= 0) {
				h.setAttribute("seconds", Long.toString(historyOptions.seconds));
			}
			if (historyOptions.since != null) {
				h.setAttribute("since", XmppDateTime.formatXMPPDateTime(historyOptions.since));
			}
		}
		return presence;
	}

	protected static boolean isNewRoom(final XMLPacket xtension) {
		final String code = xtension.getFirstChild("status").getAttribute("code");
		return code != null && code.equals("201");
	}

	protected void requestCreateInstantRoom() {
		final IQ iq = new IQ(IQ.Type.set);
		iq.setTo(getURI().getJID());
		iq.addChild("query", "http://jabber.org/protocol/muc#owner").addChild("x", "jabber:x:data").setAttribute("type", "submit");

		session.sendIQ("rooms", iq, new IQCallback() {
			@Override
			public void onIQSuccess(final IQ iq) {
				setStatus(ChatStatus.ready);
			}

			@Override
			public void onIQFailure(final IQ iq) {
			}
		});
	}

	protected Occupant setOccupantPresence(final XmppURI userUri, final XmppURI occupantUri, final String affiliation, final String role, final Presence.Show show, final String statusMessage) {
		Occupant occupant = getOccupantByOccupantUri(occupantUri);
		if (occupant == null) {
			occupant = new Occupant(userUri, occupantUri, affiliation, role, show, statusMessage);
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

	// TODO: check occupants affiliation to see if the user can do that!!
	@Override
	public void requestSubjectChange(final String subjectText) {
		final Message message = new Message();
		message.setFrom(session.getCurrentUserURI());
		message.setTo(getURI().getJID());
		message.setType(Message.Type.groupchat);
		final XMLPacket subject = message.getXML().addChild("subject", null);
		subject.setText(subjectText);
		session.send(message);
	}

}
