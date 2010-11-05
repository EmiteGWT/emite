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

import com.calclab.emite.core.client.events.MessageEvent;
import com.calclab.emite.core.client.events.MessageHandler;
import com.calclab.emite.core.client.events.PresenceEvent;
import com.calclab.emite.core.client.events.PresenceHandler;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.MatcherFactory;
import com.calclab.emite.core.client.packet.NoPacket;
import com.calclab.emite.core.client.packet.PacketMatcher;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.stanzas.BasicStanza;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.emite.core.client.xmpp.stanzas.Stanza;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.AbstractChatManager;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.im.client.chat.ChatProperties;
import com.calclab.emite.im.client.chat.ChatSelectionStrategy;
import com.calclab.emite.im.client.chat.Chat.ChatStates;
import com.calclab.emite.xep.muc.client.events.RoomInvitationEvent;
import com.calclab.emite.xep.muc.client.events.RoomInvitationHandler;
import com.calclab.suco.client.events.Listener;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * The default implementation of RoomManager. See RoomManager for javadoc.
 * 
 * @see RoomManager
 */
public class RoomChatManager extends AbstractChatManager implements RoomManager {

    private static final PacketMatcher FILTER_X = MatcherFactory.byNameAndXMLNS("x",
	    "http://jabber.org/protocol/muc#user");
    private static final PacketMatcher FILTER_INVITE = MatcherFactory.byName("invite");
    private static final String HISTORY_OPTIONS_PROP = "history.options";
    private final HashMap<XmppURI, Room> roomsByJID;
    private HistoryOptions defaultHistoryOptions;

    public RoomChatManager(final XmppSession session) {
	this(session, new RoomChatSelectionStrategy());
    }

    @Inject
    public RoomChatManager(final XmppSession session, @Named("Room") final ChatSelectionStrategy strategy) {
	super(session, strategy);
	roomsByJID = new HashMap<XmppURI, Room>();

	forwardPresenceToRooms();
	handleRoomInvitations();
    }

    @Override
    public void acceptRoomInvitation(final RoomInvitation invitation) {
	final XmppURI roomURI = invitation.getRoomURI();
	final XmppURI uri = XmppURI.uri(roomURI.getNode(), roomURI.getHost(), session.getCurrentUser().getNode());
	
	final ChatProperties properties = new ChatProperties(uri, invitation.getChatProperties());
	
	this.openChat(properties, true);
    }

    @Override
    public HandlerRegistration addRoomInvitationHandler(RoomInvitationHandler handler) {
	return RoomInvitationEvent.bind(session.getEventBus(), handler);
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

    @Override
    public HistoryOptions getDefaultHistoryOptions() {
	return defaultHistoryOptions;
    }

    /**
     * Use addRoomInvitationHandler
     */
    @Deprecated
    @Override
    public void onInvitationReceived(final Listener<RoomInvitation> listener) {
	addRoomInvitationHandler(new RoomInvitationHandler() {
	    @Override
	    public void onRoomInvitation(RoomInvitationEvent event) {
		listener.onEvent(event.getRoomInvitation());
	    }
	});
    }

    @Override
    public Room open(final XmppURI uri, final HistoryOptions historyOptions) {
	final ChatProperties properties = new ChatProperties(uri);
	properties.setData(HISTORY_OPTIONS_PROP, historyOptions);
	return (Room) openChat(properties, true);
    }

    @Override
    public void setDefaultHistoryOptions(final HistoryOptions defaultHistoryOptions) {
	this.defaultHistoryOptions = defaultHistoryOptions;
    }

    /**
     * Forward the presence messages to the room event bus.
     */
    private void forwardPresenceToRooms() {
	session.addPresenceReceivedHandler(new PresenceHandler() {
	    @Override
	    public void onPresence(PresenceEvent event) {
		Presence presence = event.getPresence();
		final ChatProperties properties = strategy.extractProperties(presence);
		if (properties != null) {
		    Chat chat = getChat(properties, false);
		    if (chat == null && properties.shouldCreateNewChat()) {
			// we need to create a chat for this incoming presence
			properties.setInitiatorUri(properties.getUri());
			chat = addNewChat(properties);
		    }
		    if (chat != null) {
			chat.getChatEventBus().fireEvent(event);
		    }
		}
	    }
	});
    }

    /**
     * Check if the incomming message is a room invitation to the user
     */
    private void handleRoomInvitations() {
	session.addMessageReceivedHandler(new MessageHandler() {
	    @Override
	    public void onMessage(MessageEvent event) {
		Message message = event.getMessage();
		IPacket child;
		if ((child = message.getFirstChild(FILTER_X).getFirstChild(FILTER_INVITE)) != NoPacket.INSTANCE) {
		    Stanza invitationStanza = new BasicStanza(child);
		    
		    // We extract the chat properties from the message
		    ChatProperties chatProperties = strategy.extractProperties(message);
		    
		    RoomInvitation invitation = new RoomInvitation(invitationStanza.getFrom(), message.getFrom(),
			    invitationStanza.getFirstChild("reason").getText(), chatProperties);
		    session.getEventBus().fireEvent(new RoomInvitationEvent(invitation));
		}
	    }
	});
    }

    @Override
    protected void addChat(final Chat chat) {
	final XmppURI jid = chat.getURI().getJID();
	roomsByJID.put(jid, (Room) chat);
	super.addChat(chat);
    }

    @Override
    protected Chat createChat(final ChatProperties properties) {
	if (properties.getState() == null) {
	    properties.setState(ChatStates.locked);
	}
	return new RoomChat(session, properties);
    }
}
