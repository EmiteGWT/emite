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

package com.calclab.emite.xtesting;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.roster.RosterItem;

public class RosterItemHelper {

	public static RosterItem createItem(final String jid, final String name, final boolean isAvailable, final String... groups) {
		final RosterItem item = new RosterItem(XmppURI.uri(jid), null, name, null);
		for (final String group : groups) {
			item.addToGroup(group);
		}
		final String resource = item.getJID().getResource();
		item.setAvailable(isAvailable, resource);
		return item;
	}

	public static RosterItem createItem(final String jid, final String name, final String... groups) {
		return createItem(jid, name, false, groups);
	}

}
