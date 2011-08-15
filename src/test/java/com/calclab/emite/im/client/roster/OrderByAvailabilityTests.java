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

import static org.junit.Assert.assertEquals;

import java.util.Comparator;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.xmpp.stanzas.Presence.Show;
import com.calclab.emite.xtesting.RosterItemHelper;

public class OrderByAvailabilityTests {

	private RosterItem chat;
	private RosterItem dnd;
	private RosterItem away;
	private RosterItem xa;
	private Comparator<RosterItem> comparator;
	private RosterItem available;
	private RosterItem unavailable;

	@Before
	public void setupItems() {
		comparator = RosterItemsOrder.byAvailability;

		available = RosterItemHelper.createItem("unknown@test", "unknown", true, "group");
		chat = RosterItemHelper.createItem("chat@test", "chat", true, "group");
		chat.setShow(Show.chat);
		dnd = RosterItemHelper.createItem("dnd@test", "dnd", true, "group");
		dnd.setShow(Show.dnd);
		away = RosterItemHelper.createItem("away@test", "away", true, "group");
		away.setShow(Show.away);
		xa = RosterItemHelper.createItem("away@test", "away", true, "group");
		xa.setShow(Show.xa);

		unavailable = RosterItemHelper.createItem("unavailable@test", "unavailable", false, "group");
	}

	@Test
	public void shouldSortAvailableFirst() {
		assertSamePosition(chat, available);
		assertFirstItemFirst(chat, away);
		assertFirstItemFirst(chat, dnd);
		assertFirstItemFirst(chat, xa);
		assertFirstItemFirst(chat, unavailable);
	}

	@Test
	public void shouldSortAwayAndXaAfterAvailable() {
		assertSecondItemFirst(away, available);
		assertSecondItemFirst(away, chat);
		assertSamePosition(away, xa);
		assertFirstItemFirst(away, dnd);
		assertFirstItemFirst(away, unavailable);
	}

	@Test
	public void shouldSortDndAfterAway() {
		assertSecondItemFirst(dnd, available);
		assertSecondItemFirst(dnd, chat);
		assertSecondItemFirst(dnd, away);
		assertSecondItemFirst(dnd, xa);
		assertFirstItemFirst(dnd, unavailable);
	}

	private void assertFirstItemFirst(final RosterItem item1, final RosterItem item2) {
		Assert.assertTrue(comparator.compare(item1, item2) < 0);
	}

	private void assertSamePosition(final RosterItem item1, final RosterItem item2) {
		assertEquals(0, comparator.compare(item1, item2));
	}

	private void assertSecondItemFirst(final RosterItem item1, final RosterItem item2) {
		Assert.assertTrue(comparator.compare(item1, item2) > 0);
	}
}
