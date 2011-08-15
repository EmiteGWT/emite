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
import static com.calclab.emite.xtesting.XmppRosterHelper.setRosterItems;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xtesting.XmppSessionTester;
import com.calclab.emite.xtesting.handlers.RosterItemChangedTestHandler;

public class XmppRosterTests {

	private XmppSessionTester session;
	private XmppRosterLogic roster;
	private XmppURI me;

	@Before
	public void beforeTests() {
		me = XmppURI.uri("me@domain");
		session = new XmppSessionTester();
		session.setLoggedIn(me);
		roster = new XmppRosterLogic(session);
	}

	@Test
	public void shouldChangeItemPresenceAndFireEvent() {
		setRosterItems(session, newItem("other@domain"));
		final RosterItemChangedTestHandler handler = new RosterItemChangedTestHandler();
		roster.addRosterItemChangedHandler(handler);
		session.receives("<presence from='other@domain'>" + "<show>dnd</show><status>message</status><priority>3</priority></presence>");
		final RosterItem item = roster.getItemByJID(uri("other@domain"));
		assertEquals(Presence.Show.dnd, item.getShow());
		assertEquals("message", item.getStatus());
		assertTrue(handler.isCalledOnce());
	}

	@Test
	public void shouldChangeItemPresenceFromSeveralResources() {
		setRosterItems(session, newItem("other@domain"));
		session.receives("<presence from='other@domain/resource1'>" + "<show>dnd</show><status>message</status><priority>3</priority></presence>");
		session.receives("<presence from='other@domain/resource2'>" + "<show>away</show><status>message</status><priority>3</priority></presence>");
		assertTrue(roster.getItemByJID(uri("other@domain")).isAvailable());
		session.receives("<presence type='unavailable' from='other@domain/resource1' />");
		assertTrue(roster.getItemByJID(uri("other@domain")).isAvailable());
	}

	@Test
	public void shouldFireChangedItemEventsInGroups() {
		final RosterItem item = newItem("other@domain");
		item.addToGroup("simple");
		setRosterItems(session, item);
		final RosterGroup group = roster.getRosterGroup("simple");
		final RosterItemChangedTestHandler handler = new RosterItemChangedTestHandler();
		group.addRosterItemChangedHandler(handler);
		assertNotNull(group);
		session.receives("<presence from='other@domain'>" + "<show>dnd</show><status>message</status><priority>3</priority></presence>");
		assertTrue(handler.hasEvent());
	}

	@Test
	public void shouldRetrieveRoster() {
		setRosterItems(session, newItem("one@domain"), newItem("two@domain"));
		assertEquals(2, roster.getItems().size());
	}

	private RosterItem newItem(final String jid) {
		final XmppURI uri = XmppURI.uri(jid);
		return new RosterItem(uri, SubscriptionState.both, uri.getNode(), null);
	}

}
