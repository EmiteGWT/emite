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

package com.calclab.emite.xep.muc.client;

import java.util.Date;

/**
 * http://xmpp.org/extensions/xep-0045.html#enter-history
 * 
 * @author Rémi
 */
public class HistoryOptions {

	public static final String KEY = "RoomHistoryOptions";

	/**
	 * Limit the total number of characters in the history to "X" (where the
	 * character count is the characters of the complete XML stanzas, not only
	 * their XML character data).
	 */
	public int maxchars;

	/**
	 * Limit the total number of messages in the history to "X".
	 * 
	 */

	public int maxstanzas;
	/**
	 * Send only the messages received in the last "X" seconds.
	 * 
	 */
	public long seconds;

	/**
	 * Send only the messages received since the datetime specified (which MUST
	 * conform to the DateTime profile specified in XMPP Date and Time Profiles
	 * [13]).
	 * 
	 */
	public Date since;

	public HistoryOptions() {
		this(-1, -1, -1, null);
	}

	public HistoryOptions(final int maxchars, final int maxstanzas, final long l, final Date since) {
		super();
		this.maxchars = maxchars;
		this.maxstanzas = maxstanzas;
		seconds = l;
		this.since = since;
	}

}
