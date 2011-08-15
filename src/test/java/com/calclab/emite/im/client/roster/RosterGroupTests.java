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

import org.junit.Before;
import org.junit.Test;

public class RosterGroupTests {

	// private XmppRoster roster;

	@Before
	public void setup() {
		// roster = new XmppRosterLogic(new XmppSessionTester());
	}

	@Test
	public void shouldFireItemChanged() {
		// final RosterItem item = RosterTester.createItem("test1@dom", "test1",
		// "A", "B");
		// roster.storeItem(item);
		// final MockedListener<RosterItem> itemChangeListener = new
		// MockedListener<RosterItem>();
		// for (final String name : roster.getGroupNames()) {
		// roster.getRosterGroup(name).onItemChanged(itemChangeListener);
		// }
		// roster.fireItemChanged(item);
		// assertEquals(3, itemChangeListener.getCalledTimes());
	}

	@Test
	public void shouldHaveCreateGroupsWhenStoreItems() {
		// final MockedListener<RosterGroup> groupAddedListener = new
		// MockedListener<RosterGroup>();
		// roster.onGroupAdded(groupAddedListener);
		// roster.storeItem(RosterTester.createItem("test1@dom", "test1", "A",
		// "B"));
		// roster.storeItem(RosterTester.createItem("test2@dom", "test2", "A"));
		// roster.storeItem(RosterTester.createItem("test3@dom", "test3"));
		// assertEquals(3, groupAddedListener.getCalledTimes());
		//
		// final RosterGroup all = roster.getRosterGroup(null);
		// assertNotNull(all);
		// assertEquals(3, all.getSize());
		//
		// assertNotNull(roster.getRosterGroup("A"));
		// assertEquals(2, roster.getRosterGroup("A").getSize());
		// assertNotNull(roster.getRosterGroup("B"));
		// assertEquals(1, roster.getRosterGroup("B").getSize());
	}

}
