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
package com.calclab.emite.im.client.roster;

import com.calclab.emite.core.client.events.PresenceEvent;
import com.calclab.emite.core.client.events.PresenceHandler;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.MatcherFactory;
import com.calclab.emite.core.client.packet.PacketMatcher;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.core.client.xmpp.stanzas.Presence.Type;
import com.calclab.emite.im.client.roster.events.RosterItemChangedEvent;
import com.calclab.emite.im.client.roster.events.RosterItemChangedHandler;
import com.calclab.emite.im.client.roster.events.SubscriptionRequestReceivedEvent;
import com.calclab.emite.im.client.roster.events.SubscriptionRequestReceivedHandler;
import com.calclab.suco.client.events.Listener2;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @see SubscriptionManager
 */
@Singleton
public class SubscriptionManagerImpl implements SubscriptionManager {
    protected static final PacketMatcher FILTER_NICK = MatcherFactory.byNameAndXMLNS("nick",
	    "http://jabber.org/protocol/nick");
    private final XmppSession session;
    private final XmppRoster roster;

    @Inject
    public SubscriptionManagerImpl(final XmppSession session, final XmppRoster roster) {
	this.session = session;
	this.roster = roster;

	session.addPresenceReceivedHandler(new PresenceHandler() {
	    @Override
	    public void onPresence(PresenceEvent event) {
		Presence presence = event.getPresence();
		if (presence.getType() == Type.subscribe) {
		    final IPacket nick = presence.getFirstChild(FILTER_NICK);
		    session.getEventBus().fireEvent(
			    new SubscriptionRequestReceivedEvent(presence.getFrom(), nick.getText()));
		}
	    }
	});

	// use bind instead of roster for better testing
	RosterItemChangedEvent.bind(session.getEventBus(), new RosterItemChangedHandler() {
	    @Override
	    public void onRosterItemChanged(RosterItemChangedEvent event) {
		if (event.isAdded()) {
		    RosterItem item = event.getRosterItem();
		    if (item.getSubscriptionState() == SubscriptionState.none) {
			// && item.getAsk() == Type.subscribe) {
			requestSubscribe(item.getJID());
			item.setSubscriptionState(SubscriptionState.nonePendingIn);
		    } else if (item.getSubscriptionState() == SubscriptionState.from) {
			approveSubscriptionRequest(item.getJID(), item.getName());
			item.setSubscriptionState(SubscriptionState.fromPendingOut);
		    }
		}
	    }
	});

    }

    @Override
    public HandlerRegistration addSubscriptionRequestReceivedHandler(SubscriptionRequestReceivedHandler handler) {
	return SubscriptionRequestReceivedEvent.bind(session.getEventBus(), handler);
    }

    @Override
    public void approveSubscriptionRequest(final XmppURI jid, String nick) {
	nick = nick != null ? nick : jid.getNode();
	final RosterItem item = roster.getItemByJID(jid);
	if (item == null) {
	    // add the item to the roster
	    roster.requestAddItem(jid, nick);
	    // request a subscription to that entity of the roster
	    requestSubscribe(jid);
	}
	// answer "subscribed" to the subscrition request
	session.send(new Presence(Type.subscribed, session.getCurrentUserURI().getJID(), jid.getJID()));
    }

    @Override
    public void cancelSubscription(final XmppURI jid) {
	session.send(new Presence(Type.unsubscribed, session.getCurrentUserURI().getJID(), jid.getJID()));
    }

    @Override
    @Deprecated
    public void onSubscriptionRequested(final Listener2<XmppURI, String> listener) {
	addSubscriptionRequestReceivedHandler(new SubscriptionRequestReceivedHandler() {
	    @Override
	    public void onSubscriptionRequestReceived(SubscriptionRequestReceivedEvent event) {
		listener.onEvent(event.getFrom(), event.getNick());
	    }
	});
    }

    @Override
    public void refuseSubscriptionRequest(final XmppURI jid) {
	session.send(new Presence(Type.unsubscribed, session.getCurrentUserURI().getJID(), jid.getJID()));
    }

    @Override
    public void requestSubscribe(final XmppURI jid) {
	session.send(new Presence(Type.subscribe, session.getCurrentUserURI().getJID(), jid.getJID()));
    }

    @Override
    public void unsubscribe(final XmppURI jid) {
	session.send(new Presence(Type.unsubscribe, session.getCurrentUserURI().getJID(), jid.getJID()));
    }

}
