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

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.MatcherFactory;
import com.calclab.emite.core.client.packet.PacketMatcher;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.BasicStanza;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.core.client.xmpp.stanzas.Presence.Type;
import com.calclab.emite.im.client.chat.AbstractChat;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.suco.client.events.Event;
import com.calclab.suco.client.events.Event2;
import com.calclab.suco.client.events.Listener;
import com.calclab.suco.client.events.Listener2;

/**
 * A Room implementation. You can create rooms using RoomManager
 * 
 * @see RoomManager
 */
public class Room extends AbstractChat implements Chat {
    private static final PacketMatcher ROOM_CREATED = MatcherFactory.byNameAndXMLNS("x",
	    "http://jabber.org/protocol/muc#user");
    private final HashMap<XmppURI, Occupant> occupantsByURI;
    private final Event<Occupant> onOccupantModified;
    private final Event<Collection<Occupant>> onOccupantsChanged;
    private final Event2<Occupant, String> onSubjectChanged;
    private final Session session;

    /**
     * Create a new room. The roomURI MUST include the nick (as the resource)
     * 
     * @param session
     *            the session
     * @param roomURI
     *            the room uri with the nick specified in the resource part
     */
    Room(final Session session, final XmppURI roomURI, final XmppURI starter) {
	super(session, roomURI, starter);
	this.session = session;
	this.occupantsByURI = new HashMap<XmppURI, Occupant>();
	this.onOccupantModified = new Event<Occupant>("room:onOccupantModified");
	this.onOccupantsChanged = new Event<Collection<Occupant>>("room:onOccupantsChanged");
	this.onSubjectChanged = new Event2<Occupant, String>("room:onSubjectChanged");

	// @see http://www.xmpp.org/extensions/xep-0045.html#createroom
	session.onPresence(new Listener<Presence>() {
	    public void onEvent(final Presence presence) {
		final XmppURI occupantURI = presence.getFrom();
		if (roomURI.equalsNoResource(occupantURI)) {
		    handlePresence(occupantURI, presence);
		}
	    }
	});

	session.onStateChanged(new Listener<Session>() {
	    @Override
	    public void onEvent(Session session) {
		Session.State state = session.getState();
		if (Session.State.loggedIn == state) {
		} else if (Session.State.loggingOut == state) {
		    close();
		}
	    }
	});

	final Presence presence = new Presence(null, null, roomURI);
	presence.addChild("x", "http://jabber.org/protocol/muc");
	presence.setPriority(0);
	session.send(presence);

    }

    /**
     * Exit and locks the current room. This is done automatically when the
     * session logouts
     * 
     * @see http://www.xmpp.org/extensions/xep-0045.html#exit
     */
    public void close() {
	if (state == State.ready) {
	    session.send(new Presence(Type.unavailable, null, getURI()));
	    setState(State.locked);
	}
    }

    public String getID() {
	return uri.toString();
    }

    public Occupant getOccupantByURI(final XmppURI uri) {
	return occupantsByURI.get(uri);
    }

    public Object getOccupantsCount() {
	return occupantsByURI.size();
    }

    /**
     * To check if is an echo message
     * 
     * @param message
     * @return true if this message is a room echo
     */
    public boolean isComingFromMe(final Message message) {
	final String myNick = uri.getResource();
	final String messageNick = message.getFrom().getResource();
	return myNick.equals(messageNick);
    }

    public void onOccupantModified(final Listener<Occupant> listener) {
	onOccupantModified.add(listener);
    }

    public void onOccupantsChanged(final Listener<Collection<Occupant>> listener) {
	onOccupantsChanged.add(listener);
    }

    public void onSubjectChanged(final Listener2<Occupant, String> listener) {
	onSubjectChanged.add(listener);
    }

    public void removeOccupant(final XmppURI uri) {
	final Occupant occupant = occupantsByURI.remove(uri);
	if (occupant != null) {
	    onOccupantsChanged.fire(occupantsByURI.values());
	}
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
    public void sendInvitationTo(final String userJid, final String reasonText) {
	final BasicStanza message = new BasicStanza("message", null);
	message.setFrom(session.getCurrentUser());
	message.setTo(getURI().getJID());
	final IPacket x = message.addChild("x", "http://jabber.org/protocol/muc#user");
	final IPacket invite = x.addChild("invite", null);
	invite.setAttribute("to", userJid);
	final IPacket reason = invite.addChild("reason", null);
	reason.setText(reasonText);
	session.send(message);
    }

    public Occupant setOccupantPresence(final XmppURI uri, final String affiliation, final String role) {
	Occupant occupant = getOccupantByURI(uri);
	if (occupant == null) {
	    occupant = new Occupant(uri, affiliation, role);
	    occupantsByURI.put(occupant.getURI(), occupant);
	    onOccupantsChanged.fire(occupantsByURI.values());
	} else {
	    occupant.setAffiliation(affiliation);
	    occupant.setRole(role);
	    onOccupantModified.fire(occupant);
	}
	return occupant;
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
	return "ROOM: " + uri;
    }

    private void handlePresence(final XmppURI occupantURI, final Presence presence) {
	if (presence.hasAttribute("type", "unavailable")) {
	    this.removeOccupant(occupantURI);
	} else {
	    final List<? extends IPacket> children = presence.getChildren(ROOM_CREATED);
	    for (final IPacket child : children) {
		final IPacket item = child.getFirstChild("item");
		final String affiliation = item.getAttribute("affiliation");
		final String role = item.getAttribute("role");
		this.setOccupantPresence(occupantURI, affiliation, role);
		if (isNewRoom(child)) {
		    requestCreateInstantRoom();
		} else {
		    if (state != State.ready) {
			this.setState(Chat.State.ready);
		    }
		}
	    }
	}
    }

    private boolean isNewRoom(final IPacket xtension) {
	final String code = xtension.getFirstChild("status").getAttribute("code");
	return code != null && code.equals("201");
    }

    private void requestCreateInstantRoom() {
	final IQ iq = new IQ(IQ.Type.set, this.getURI().getJID());
	iq.addQuery("http://jabber.org/protocol/muc#owner").addChild("x", "jabber:x:data").With("type", "submit");
	session.sendIQ("rooms", iq, new Listener<IPacket>() {
	    public void onEvent(final IPacket received) {
		if (IQ.isSuccess(received)) {
		    setState(Chat.State.ready);
		}
	    }
	});
    }

    @Override
    protected void receive(final Message message) {
	final String subject = message.getSubject();
	if (subject != null) {
	    fireBeforeReceive(message);
	    onSubjectChanged.fire(occupantsByURI.get(message.getFrom()), subject);
	}
	if (message.getBody() != null) {
	    super.receive(message);
	}
    }
}
