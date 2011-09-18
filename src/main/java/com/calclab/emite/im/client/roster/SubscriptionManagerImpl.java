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

package com.calclab.emite.im.client.roster;

import com.calclab.emite.core.client.events.PresenceReceivedEvent;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.MatcherFactory;
import com.calclab.emite.core.client.packet.PacketMatcher;
import com.calclab.emite.core.client.session.XmppSession;
import com.calclab.emite.core.client.stanzas.Presence;
import com.calclab.emite.core.client.stanzas.XmppURI;
import com.calclab.emite.core.client.stanzas.Presence.Type;
import com.calclab.emite.im.client.events.RosterItemChangedEvent;
import com.calclab.emite.im.client.events.SubscriptionRequestReceivedEvent;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * @see SubscriptionManager
 */
@Singleton
public class SubscriptionManagerImpl implements SubscriptionManager, PresenceReceivedEvent.Handler, RosterItemChangedEvent.Handler {
	protected static final PacketMatcher FILTER_NICK = MatcherFactory.byNameAndXMLNS("nick", "http://jabber.org/protocol/nick");
	
	private final EventBus eventBus;
	private final XmppSession session;
	private final XmppRoster roster;

	@Inject
	public SubscriptionManagerImpl(@Named("emite") final EventBus eventBus, final XmppSession session, final XmppRoster roster) {
		this.eventBus = eventBus;
		this.session = session;
		this.roster = roster;

		session.addPresenceReceivedHandler(this);
		roster.addRosterItemChangedHandler(this);
	}
	
	@Override
	public void onPresenceReceived(final PresenceReceivedEvent event) {
		final Presence presence = event.getPresence();
		if (presence.getType() == Type.subscribe) {
			final IPacket nick = presence.getFirstChild(FILTER_NICK);
			eventBus.fireEventFromSource(new SubscriptionRequestReceivedEvent(presence.getFrom(), nick.getText()), this);
		}
	}
	
	@Override
	public void onRosterItemChanged(final RosterItemChangedEvent event) {
		if (event.isAdded()) {
			final RosterItem item = event.getRosterItem();
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

	@Override
	public HandlerRegistration addSubscriptionRequestReceivedHandler(final SubscriptionRequestReceivedEvent.Handler handler) {
		return eventBus.addHandlerToSource(SubscriptionRequestReceivedEvent.TYPE, this, handler);
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
