/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008-2010 The emite development team (see CREDITS for details)
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
package com.calclab.emite.xep.muc.client;

import java.util.Date;

/**
 * http://xmpp.org/extensions/xep-0045.html#enter-history
 * 
 * @author Rémi
 */
public class HistoryOptions {

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

    public HistoryOptions(int maxchars, int maxstanzas, long l, Date since) {
	super();
	this.maxchars = maxchars;
	this.maxstanzas = maxstanzas;
	this.seconds = l;
	this.since = since;
    }

}
