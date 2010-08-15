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

import java.util.HashMap;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.MatcherFactory;
import com.calclab.emite.core.client.packet.NoPacket;
import com.calclab.emite.core.client.packet.PacketMatcher;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.stanzas.BasicStanza;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.Stanza;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.AbstractChatManager;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.im.client.chat.ChatProperties;
import com.calclab.emite.xep.disco.client.DiscoveryManager;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.events.Event;
import com.calclab.suco.client.events.Listener;

public class RoomManagerImpl extends AbstractChatManager implements RoomManager {

    private static final PacketMatcher FILTER_X = MatcherFactory.byNameAndXMLNS("x",
	    "http://jabber.org/protocol/muc#user");
    private static final PacketMatcher FILTER_INVITE = MatcherFactory.byName("invite");
    private static final String HISTORY_OPTIONS_PROP = "history.options";
    private final HashMap<XmppURI, Room> roomsByJID;
    private final Event<RoomInvitation> onInvitationReceived;
    private HistoryOptions defaultHistoryOptions;

    public RoomManagerImpl(final XmppSession session) {
	super(session, new DefaultRoomSelectionStrategy());
	onInvitationReceived = new Event<RoomInvitation>("roomManager:onInvitationReceived");
	roomsByJID = new HashMap<XmppURI, Room>();

    }

    @Override
    public void acceptRoomInvitation(final RoomInvitation invitation) {
	final XmppURI roomURI = invitation.getRoomURI();
	final XmppURI uri = XmppURI.uri(roomURI.getNode(), roomURI.getHost(), session.getCurrentUser().getNode());
	open(uri);
    }

    @Override
    public void close(final Chat whatToClose) {
	final Room room = roomsByJID.remove(whatToClose.getURI().getJID());
	if (room != null) {
	    room.close();
	    super.close(room);
	}
    }

    @Override
    public Room getChat(final XmppURI uri) {
	return roomsByJID.get(uri.getJID());
    }

    public HistoryOptions getDefaultHistoryOptions() {
	return defaultHistoryOptions;
    }

    public void onInvitationReceived(final Listener<RoomInvitation> listener) {
	onInvitationReceived.add(listener);
    }

    @Override
    public Room open(final XmppURI uri, final HistoryOptions historyOptions) {
	final ChatProperties properties = new ChatProperties(uri);
	properties.setData(HISTORY_OPTIONS_PROP, historyOptions);
	return (Room) openChat(properties, true);
    }

    @Override
    public void requestRoomDiscovery(final XmppURI hostUri, final RoomDiscoveryListener listener) {
	final DiscoveryManager discoManager = Suco.get(DiscoveryManager.class);
	discoManager.sendDiscoItemsQuery(session.getCurrentUser(), hostUri, listener);
    }

    public void setDefaultHistoryOptions(final HistoryOptions defaultHistoryOptions) {
	this.defaultHistoryOptions = defaultHistoryOptions;
    }

    // FIXME: joder!
    private void eventMessage(final Message message) {
	IPacket child;
	if (message.getType() == Message.Type.groupchat) {
	    final Room room = getChat(message.getFrom().getJID());
	    if (room != null) {
		room.receive(message);
	    }
	} else if ((child = message.getFirstChild(FILTER_X).getFirstChild(FILTER_INVITE)) != NoPacket.INSTANCE) {
	    handleRoomInvitation(message.getFrom(), new BasicStanza(child));
	}

    }

    private void handleRoomInvitation(final XmppURI roomURI, final Stanza invitation) {
	onInvitationReceived.fire(new RoomInvitation(invitation.getFrom(), roomURI, invitation.getFirstChild("reason")
		.getText()));
    }

    @Override
    protected void addChat(final Chat chat) {
	final XmppURI jid = chat.getURI().getJID();
	roomsByJID.put(jid, (Room) chat);
	super.addChat(chat);
    }

    @Override
    protected Chat createChat(final ChatProperties properties) {
	return new Room(session, properties);
    }
}
