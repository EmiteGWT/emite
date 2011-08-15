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

import java.util.Comparator;

public class RosterItemsOrder {

	public static final Comparator<RosterItem> byName = new Comparator<RosterItem>() {
		@Override
		public int compare(final RosterItem item1, final RosterItem item2) {
			final String item1Name = item1.getName() == null ? item1.getJID().toString() : item1.getName();
			final String item2Name = item2.getName() == null ? item2.getJID().toString() : item2.getName();

			return item1Name.compareToIgnoreCase(item2Name);
		}
	};

	/**
	 * Available first. Inside availables, items with getShow() == Show.dnd last
	 */
	public static final Comparator<RosterItem> byAvailability = new Comparator<RosterItem>() {
		@Override
		public int compare(final RosterItem item1, final RosterItem item2) {
			final boolean av1 = item1.isAvailable();
			final boolean av2 = item2.isAvailable();

			if (av1 && !av2)
				return -1;
			else if (!av1 && av2)
				return 1;
			else if (av1 && av2)
				return getShowLevel(item2) - getShowLevel(item1);
			else
				return 0;

		}

		private int getShowLevel(final RosterItem item) {
			switch (item.getShow()) {
			case dnd:
				return 1;
			case away:
			case xa:
				return 2;
			default:
				return 3;
			}
		}
	};

	public static Comparator<RosterItem> groupedFirst = new Comparator<RosterItem>() {
		@Override
		public int compare(final RosterItem item1, final RosterItem item2) {
			final int item1Size = item1.getGroups().size();
			final int item2Size = item2.getGroups().size();

			if (item1Size == 0 && item2Size == 0 || item1Size > 0 && item2Size > 0)
				return 0;
			else if (item1Size == 0)
				return 1;
			else
				return -1;
		}
	};

	public static Comparator<RosterItem> order(final Comparator<RosterItem>... comparators) {
		return new Comparator<RosterItem>() {
			@Override
			public int compare(final RosterItem o1, final RosterItem o2) {
				int result;
				for (final Comparator<RosterItem> order : comparators) {
					result = order.compare(o1, o2);
					if (result != 0)
						return result;
				}
				return 0;
			}
		};
	}

}
