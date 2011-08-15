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

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.events.ChangedEvent.ChangeTypes;
import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.xmpp.stanzas.Presence.Type;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.roster.events.RosterItemChangedEvent;
import com.calclab.emite.xtesting.XmppSessionTester;
import com.calclab.emite.xtesting.handlers.SubscriptionRequestReceivedTestHandler;

public class SubscriptionManagerTests {

	private XmppSessionTester session;
	private SubscriptionManager manager;
	private XmppRoster roster;
	private EmiteEventBus eventBus;

	@Before
	public void beforeTests() {
		session = new XmppSessionTester();
		eventBus = session.getEventBus();
		roster = mock(XmppRoster.class);
		manager = new SubscriptionManagerImpl(session, roster);
		session.login(uri("user@local"), "anything");
	}

	@Test
	public void shouldApproveSubscriptionRequestsAndAddItemToTheRosterIfNotThere() {
		final XmppURI otherEntityJID = XmppURI.jid("other@domain");
		when(roster.getItemByJID(eq(otherEntityJID))).thenReturn(null);

		manager.approveSubscriptionRequest(otherEntityJID, "nick");
		verify(roster).requestAddItem(eq(otherEntityJID), eq("nick"));
		session.verifySent("<presence type='subscribed' to='other@domain' />");
		session.verifySent("<presence type='subscribe' to='other@domain' />");
	}

	@Test
	public void shouldCancelSubscription() {
		manager.cancelSubscription(uri("friend@domain"));
		session.verifySent("<presence from='user@local' to='friend@domain' type='unsubscribed' />");
	}

	@Test
	public void shouldFireSubscriptionRequests() {
		final SubscriptionRequestReceivedTestHandler handler = new SubscriptionRequestReceivedTestHandler();
		manager.addSubscriptionRequestReceivedHandler(handler);
		session.receives("<presence to='user@local' from='friend@domain' type='subscribe' />");
		assertEquals(1, handler.getCalledTimes());
	}

	@Test
	public void shouldSendSubscriptionRequest() {
		manager.requestSubscribe(uri("name@domain/RESOURCE"));
		session.verifySent("<presence from='user@local' to='name@domain' type='subscribe'/>");
	}

	@Test
	public void shouldSendSubscriptionRequestOnNewRosterItem_addRosterStep1() {

		// only NONE subscription
		final RosterItem item = new RosterItem(uri("name@domain"), SubscriptionState.both, "TheName", null);
		eventBus.fireEvent(new RosterItemChangedEvent(ChangeTypes.added, item));
		session.verifyNotSent("<presence />");

		final RosterItem item2 = new RosterItem(uri("name@domain"), SubscriptionState.none, "TheName", Type.subscribe);
		eventBus.fireEvent(new RosterItemChangedEvent(ChangeTypes.added, item2));
		session.verifySent("<presence from='user@local' to='name@domain' type='subscribe'/>");
	}

	@Test
	public void shouldUnsubscribe() {
		manager.unsubscribe(uri("friend@domain"));
		session.verifySent("<presence from='user@local' to='friend@domain' type='unsubscribe' />");
	}
}
