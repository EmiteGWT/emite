/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008-2009 The emite development team (see CREDITS for details)
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
package com.calclab.emite.xep.muc.client;

import java.util.List;

import com.calclab.emite.core.client.events.ErrorEvent;
import com.calclab.emite.core.client.events.MessageEvent;
import com.calclab.emite.core.client.events.MessageHandler;
import com.calclab.emite.core.client.events.PresenceEvent;
import com.calclab.emite.core.client.events.PresenceHandler;
import com.calclab.emite.core.client.events.ChangedEvent.ChangeTypes;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.MatcherFactory;
import com.calclab.emite.core.client.packet.PacketMatcher;
import com.calclab.emite.core.client.xmpp.datetime.XmppDateTime;
import com.calclab.emite.core.client.xmpp.session.IQResponseHandler;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.stanzas.BasicStanza;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.core.client.xmpp.stanzas.Presence.Show;
import com.calclab.emite.core.client.xmpp.stanzas.Presence.Type;
import com.calclab.emite.im.client.chat.ChatProperties;
import com.calclab.emite.xep.muc.client.events.BeforeRoomInvitationSendEvent;
import com.calclab.emite.xep.muc.client.events.OccupantChangedEvent;
import com.calclab.emite.xep.muc.client.events.RoomInvitationSentEvent;
import com.calclab.emite.xep.muc.client.events.RoomSubjectChangedEvent;

/**
 * A Room implementation. You can create rooms using RoomManager.
 * 
 * @see RoomManager
 */
public class RoomChat extends RoomBoilerplate {
    protected static final PacketMatcher ROOM_CREATED = MatcherFactory.byNameAndXMLNS("x",
	    "http://jabber.org/protocol/muc#user");

    /**
     * Create a new room with the given properties. Room are created by
     * RoomManagers
     * 
     * @param session
     *            the session
     * @param roomURI
     *            the room uri with the nick specified in the resource part
     */
    RoomChat(final XmppSession session, final ChatProperties properties) {
	super(session, properties);
	setChatState(ChatStates.locked);

	trackRoomPresence();
	trackSubjectChangeMessages();

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.calclab.emite.xep.muc.client.IRoom#close()
     */
    @Override
    public void close() {
	if (ChatStates.ready.equals(properties.getState())) {
	    if (getOccupantByUserUri(session.getCurrentUser()) != null) {
		session.send(new Presence(Type.unavailable, null, getURI()));
	    }
	    properties.setState(ChatStates.locked);
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.calclab.emite.xep.muc.client.IRoom#getID()
     */
    @Override
    public String getID() {
	return getURI().toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.calclab.emite.xep.muc.client.IRoom#isComingFromMe(com.calclab.emite
     * .core.client.xmpp.stanzas.Message)
     */
    @Override
    public boolean isComingFromMe(final Message message) {
	final String myNick = getURI().getResource();
	final String messageNick = message.getFrom().getResource();
	return myNick.equals(messageNick);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.calclab.emite.xep.muc.client.IRoom#isUserMessage(com.calclab.emite
     * .core.client.xmpp.stanzas.Message)
     */
    @Override
    public boolean isUserMessage(Message message) {
	String resource = message.getFrom().getResource();
	return resource != null && !"".equals(resource);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.calclab.emite.xep.muc.client.IRoom#open()
     */
    @Override
    public void open() {
	final HistoryOptions historyOptions = (HistoryOptions) properties.getData(HistoryOptions.KEY);
	session.send(createEnterPresence(historyOptions));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.calclab.emite.xep.muc.client.IRoom#reEnter(com.calclab.emite.xep.
     * muc.client.HistoryOptions)
     */
    @Override
    public void reEnter(final HistoryOptions historyOptions) {
	if (getState() == State.locked) {
	    session.send(createEnterPresence(historyOptions));
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.calclab.emite.xep.muc.client.IRoom#setSubject(java.lang.String)
     */
    @Override
    public void requestSubjectChange(final String subjectText) {
	final BasicStanza message = new BasicStanza("message", null);
	message.setFrom(session.getCurrentUser());
	message.setTo(getURI().getJID());
	message.setType(Message.Type.groupchat.toString());
	final IPacket subject = message.addChild("subject", null);
	subject.setText(subjectText);
	session.send(message);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.calclab.emite.xep.muc.client.IRoom#send(com.calclab.emite.core.client
     * .xmpp.stanzas.Message)
     */
    @Override
    public void send(final Message message) {
	message.setTo(getURI().getJID());
	message.setType(Message.Type.groupchat);
	super.send(message);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.calclab.emite.xep.muc.client.IRoom#sendInvitationTo(com.calclab.emite
     * .core.client.xmpp.stanzas.XmppURI, java.lang.String)
     */
    @Override
    public void sendInvitationTo(final XmppURI userJid, final String reasonText) {
	final BasicStanza message = new BasicStanza("message", null);
	message.setFrom(session.getCurrentUser());
	message.setTo(getURI().getJID());
	final IPacket x = message.addChild("x", "http://jabber.org/protocol/muc#user");
	final IPacket invite = x.addChild("invite", null);
	invite.setAttribute("to", userJid.toString());
	final IPacket reason = invite.addChild("reason", null);
	reason.setText(reasonText);
	chatEventBus.fireEvent(new BeforeRoomInvitationSendEvent(message, invite));
	session.send(message);
	chatEventBus.fireEvent(new RoomInvitationSentEvent(userJid, reasonText));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.calclab.emite.xep.muc.client.IRoom#setStatus(java.lang.String,
     * com.calclab.emite.core.client.xmpp.stanzas.Presence.Show)
     */
    @Override
    public void setStatus(final String statusMessage, final Show show) {
	final Presence presence = Presence.build(statusMessage, show);
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
	final IPacket x = presence.addChild("x", "http://jabber.org/protocol/muc");
	presence.setPriority(0);
	if (historyOptions != null) {
	    final IPacket h = x.addChild("history");
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

    protected boolean isNewRoom(final IPacket xtension) {
	final String code = xtension.getFirstChild("status").getAttribute("code");
	return code != null && code.equals("201");
    }

    protected void requestCreateInstantRoom() {
	final IQ iq = new IQ(IQ.Type.set, getURI().getJID());
	iq.addQuery("http://jabber.org/protocol/muc#owner").addChild("x", "jabber:x:data").With("type", "submit");

	session.sendIQ("rooms", iq, new IQResponseHandler() {
	    @Override
	    public void onIQ(final IQ iq) {
		if (IQ.isSuccess(iq)) {
		    setChatState(ChatStates.ready);
		}
	    }
	});

    }

    protected Occupant setOccupantPresence(final XmppURI userUri, XmppURI occupantUri, final String affiliation,
	    final String role, final Show show, final String statusMessage) {
	Occupant occupant = getOccupantByOccupantUri(occupantUri);
	if (occupant == null) {
	    occupant = new Occupant(userUri, occupantUri, affiliation, role, show, statusMessage);
	    addOccupant(occupant);
	} else {
	    occupant.setAffiliation(affiliation);
	    occupant.setRole(role);
	    occupant.setShow(show);
	    occupant.setStatusMessage(statusMessage);
	    chatEventBus.fireEvent(new OccupantChangedEvent(ChangeTypes.modified, occupant));
	}
	return occupant;
    }

    /**
     * Perform some room actions when presence received: handle room occupants,
     * create instant rooms...
     */
    private void trackRoomPresence() {
	addPresenceReceivedHandler(new PresenceHandler() {
	    @Override
	    public void onPresence(PresenceEvent event) {
		Presence presence = event.getPresence();
		final XmppURI occupantURI = presence.getFrom();
		final Type type = presence.getType();
		if (type == Type.error) {
		    chatEventBus.fireEvent(new ErrorEvent(ChatErrors.errorPresence, "We received a presence error",
			    presence));
		} else if (type == Type.unavailable) {
		    removeOccupant(occupantURI);
		    if (occupantURI.equalsNoResource(session.getCurrentUser())) {
			close();
		    }
		} else {
		    final List<? extends IPacket> children = presence.getChildren(ROOM_CREATED);
		    for (final IPacket child : children) {
			final IPacket item = child.getFirstChild("item");
			final String affiliation = item.getAttribute("affiliation");
			final String role = item.getAttribute("role");
			final XmppURI userUri = XmppURI.uri(item.getAttribute("jid"));
			setOccupantPresence(userUri, occupantURI, affiliation, role, presence.getShow(), presence
				.getStatus());
			if (isNewRoom(child)) {
			    requestCreateInstantRoom();
			} else {
			    setChatState(ChatStates.ready);
			}
		    }
		}
	    }
	});
    }

    private void trackSubjectChangeMessages() {
	addMessageReceivedHandler(new MessageHandler() {
	    @Override
	    public void onMessage(MessageEvent event) {
		Message message = event.getMessage();
		if (message.getSubject() != null) {
		    Occupant occupant = getOccupantByOccupantUri(message.getFrom());
		    if (occupant != null) {
			chatEventBus.fireEvent(new RoomSubjectChangedEvent(occupant, message.getSubject()));
		    } else {
			chatEventBus.fireEvent(new ErrorEvent(ChatErrors.occupantNotFound,
				"The occupant of this message is not found", message));
		    }
		}
	    }
	});
    }
}
