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

import com.calclab.emite.core.client.events.ChangedEvent.ChangeEventTypes;
import com.calclab.emite.core.client.events.PresenceEvent;
import com.calclab.emite.core.client.events.PresenceHandler;
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
import com.calclab.emite.core.client.xmpp.stanzas.Presence.Show;
import com.calclab.emite.core.client.xmpp.stanzas.Presence.Type;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.ChatProperties;
import com.calclab.emite.xep.muc.client.events.OccupantChangedEvent;
import com.calclab.emite.xep.muc.client.events.RoomInvitationSentEvent;
import com.calclab.emite.xep.muc.client.events.RoomSubjectChangedEvent;
import com.google.gwt.core.client.GWT;

/**
 * A Room implementation. You can create rooms using RoomManager
 * 
 * @see RoomManager
 */
public class Room extends RoomBoilerplate {
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
    Room(final XmppSession session, final ChatProperties properties) {
	super(session, properties);
	setChatState(ChatStates.locked);

	// @see http://www.xmpp.org/extensions/xep-0045.html#createroom
	session.addPresenceReceivedHandler(new PresenceHandler() {
	    @Override
	    public void onPresence(final PresenceEvent event) {
		final Presence presence = event.getPresence();
		final XmppURI occupantURI = presence.getFrom();
		final XmppURI roomURI = properties.getUri();
		if (roomURI.equalsNoResource(occupantURI)) {
		    handlePresence(occupantURI, presence);
		}
	    }
	});

    }

    /**
     * Exit and locks the current room. This method is called by RoomManager
     * when session logouts
     * 
     * @see http://www.xmpp.org/extensions/xep-0045.html#exit
     */
    @Override
    public void close() {
	if (ChatStates.ready.equals(properties.getState())) {
	    session.send(new Presence(Type.unavailable, null, getURI()));
	    properties.setState(ChatStates.locked);
	}
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

    @Override
    public String getID() {
	return getURI().toString();
    }

    protected void handlePresence(final XmppURI occupantURI, final Presence presence) {
	final Type type = presence.getType();
	if (type == Type.error || type == Type.unavailable && occupantURI.equals(getURI())) {
	    // TODO : add an error/out state ?
	    setState(State.locked);
	} else if (type == Type.unavailable) {
	    removeOccupant(occupantURI);
	} else {
	    final List<? extends IPacket> children = presence.getChildren(ROOM_CREATED);
	    for (final IPacket child : children) {
		final IPacket item = child.getFirstChild("item");
		final String affiliation = item.getAttribute("affiliation");
		final String role = item.getAttribute("role");
		setOccupantPresence(occupantURI, affiliation, role, presence.getShow(), presence.getStatus());
		if (isNewRoom(child)) {
		    requestCreateInstantRoom();
		} else {
		    if (ChatStates.ready.equals(getChatState())) {
			setChatState(ChatStates.ready);
		    }
		}
	    }
	}
    }

    /**
     * To check if is an echo message
     * 
     * @param message
     * @return true if this message is a room echo
     */
    public boolean isComingFromMe(final Message message) {
	final String myNick = getURI().getResource();
	final String messageNick = message.getFrom().getResource();
	return myNick.equals(messageNick);
    }

    protected boolean isNewRoom(final IPacket xtension) {
	final String code = xtension.getFirstChild("status").getAttribute("code");
	return code != null && code.equals("201");
    }

    @Override
    public void open() {
	final HistoryOptions historyOptions = (HistoryOptions) properties.getData(HistoryOptions.KEY);
	session.send(createEnterPresence(historyOptions));

    }

    @Override
    protected void receive(final Message message) {
	if (message.getSubject() != null) {
	    eventBus.fireEvent(new RoomSubjectChangedEvent(occupantsByURI.get(message.getFrom()), message.getSubject()));
	}
	if (message.getType() == Message.Type.error) {
	    GWT.log("Received Error message :" + message);
	    setChatState(ChatStates.locked);
	}
	super.receive(message);
    }

    public void reEnter(final HistoryOptions historyOptions) {
	if (getState() == State.locked) {
	    session.send(createEnterPresence(historyOptions));
	}
    }

    public void removeOccupant(final XmppURI uri) {
	final Occupant occupant = occupantsByURI.remove(uri);
	if (occupant != null) {
	    eventBus.fireEvent(new OccupantChangedEvent(ChangeEventTypes.removed, occupant));
	}
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

    @Override
    public void send(final Message message) {
	message.setTo(getURI().getJID());
	message.setType(Message.Type.groupchat);
	super.send(message);
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
	final BasicStanza message = new BasicStanza("message", null);
	message.setFrom(session.getCurrentUser());
	message.setTo(getURI().getJID());
	final IPacket x = message.addChild("x", "http://jabber.org/protocol/muc#user");
	final IPacket invite = x.addChild("invite", null);
	invite.setAttribute("to", userJid.toString());
	final IPacket reason = invite.addChild("reason", null);
	reason.setText(reasonText);
	session.send(message);
	eventBus.fireEvent(new RoomInvitationSentEvent(userJid, reasonText));
    }

    public Occupant setOccupantPresence(final XmppURI uri, final String affiliation, final String role,
	    final Show show, final String statusMessage) {
	Occupant occupant = getOccupantByURI(uri);
	if (occupant == null) {
	    occupant = new Occupant(uri, affiliation, role, show, statusMessage);
	    occupantsByURI.put(occupant.getURI(), occupant);
	    eventBus.fireEvent(new OccupantChangedEvent(ChangeEventTypes.added, occupant));
	} else {
	    occupant.setAffiliation(affiliation);
	    occupant.setRole(role);
	    occupant.setShow(show);
	    occupant.setStatusMessage(statusMessage);
	    eventBus.fireEvent(new OccupantChangedEvent(ChangeEventTypes.modified, occupant));
	}
	return occupant;
    }

    /**
     * Update my status to other occupants.
     * 
     * @param statusMessage
     * @param show
     */
    public void setStatus(final String statusMessage, final Show show) {
	final Presence presence = Presence.build(statusMessage, show);
	presence.setTo(getURI());
	// presence.addChild("x", "http://jabber.org/protocol/muc");
	// presence.setPriority(0);
	session.send(presence);
    }

    /**
     * http://www.xmpp.org/extensions/xep-0045.html#subject-mod
     * 
     * @param subjectText
     */
    public void setSubject(final String subjectText) {
	final BasicStanza message = new BasicStanza("message", null);
	message.setFrom(session.getCurrentUser());
	message.setTo(getURI().getJID());
	message.setType(Message.Type.groupchat.toString());
	final IPacket subject = message.addChild("subject", null);
	subject.setText(subjectText);
	session.send(message);
    }

    @Override
    public String toString() {
	return "ROOM: " + getURI();
    }
}
