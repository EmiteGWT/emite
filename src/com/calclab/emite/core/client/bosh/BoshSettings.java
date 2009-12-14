/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008-2009 The emite development team (see CREDITS for details)
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
package com.calclab.emite.core.client.bosh;

/**
 * Bosh connection settings
 */
public class BoshSettings {
    public final String hostName;
    public final String httpBase;
    public final String version;
    public final int maxRequests;
    public final int hold;
    public final int wait;

    public BoshSettings(final String httpBase, final String hostName) {
	this(httpBase, hostName, "1.6", 60, 1, 2);
    }

    public BoshSettings(final String httpBase, final String hostName, final String version, final int wait,
	    final int hold, final int maxRequests) {
	this.httpBase = httpBase;
	this.hostName = hostName;
	this.version = version;
	this.wait = wait;
	this.hold = hold;
	this.maxRequests = maxRequests;
    }

}
