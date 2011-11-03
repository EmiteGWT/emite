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

import javax.annotation.concurrent.Immutable;

/**
 * http://xmpp.org/extensions/xep-0045.html#enter-history
 */
@Immutable
public final class HistoryOptions {

	/**
	 * Limit the total number of characters in the history to "X" (where the
	 * character count is the characters of the complete XML stanzas, not only
	 * their XML character data).
	 */
	private final int maxChars;

	/**
	 * Limit the total number of messages in the history to "X".
	 */
	private final int maxStanzas;
	
	/**
	 * Send only the messages received in the last "X" seconds.
	 */
	private final long seconds;

	/**
	 * Send only the messages received since the datetime specified (which MUST
	 * conform to the DateTime profile specified in XMPP Date and Time Profiles
	 * [13]).
	 */
	private final Date since;

	public HistoryOptions() {
		this(-1, -1, -1, null);
	}

	public HistoryOptions(final int maxChars, final int maxStanzas, final long seconds, final Date since) {
		this.maxChars = maxChars;
		this.maxStanzas = maxStanzas;
		this.seconds = seconds;
		this.since = since;
	}

	public final int getMaxChars() {
		return maxChars;
	}

	public final int getMaxStanzas() {
		return maxStanzas;
	}

	public final long getSeconds() {
		return seconds;
	}

	public final Date getSince() {
		return since;
	}

}
