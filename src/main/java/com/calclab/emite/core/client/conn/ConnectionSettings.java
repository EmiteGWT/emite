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

package com.calclab.emite.core.client.conn;

public class ConnectionSettings {
	public final String hostName;
	public final String httpBase;
	public final String version;
	public final int maxRequests;
	public final int hold;
	public final int wait;
	public final String routeHost;
	public final Integer routePort;
	public final boolean secure;

	public ConnectionSettings(final String httpBase, final String hostName) {
		this(httpBase, hostName, "1.6", 60, 1, 2, null, null, true);
	}

	public ConnectionSettings(final String httpBase, final String hostName, final String version, final int wait, final int hold, final int maxRequests) {
		this(httpBase, hostName, version, wait, hold, maxRequests, null, null, true);
	}

	public ConnectionSettings(final String httpBase, final String hostName, final String version, final int wait, final int hold, final int maxRequests,
			final String routeHost, final Integer routePort, final boolean secure) {
		this.httpBase = httpBase;
		this.hostName = hostName;
		this.version = version;
		this.wait = wait;
		this.hold = hold;
		this.maxRequests = maxRequests;
		this.routeHost = routeHost;
		this.routePort = routePort;
		this.secure = secure;
	}

	public ConnectionSettings(final String httpBase, final String hostName, final String routeHost, final Integer routePort, final boolean secure) {
		this(httpBase, hostName, "1.6", 60, 1, 2, routeHost, routePort, secure);
	}
}
