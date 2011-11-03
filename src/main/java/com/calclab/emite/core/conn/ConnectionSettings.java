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

package com.calclab.emite.core.conn;

public class ConnectionSettings {
	public static int MAX_REQUESTS = 2;
	public static int DEFAULT_WAIT = 60;
	public static int DEFAULT_HOLD = 1;

	public final String httpBase;
	public final String hostName;
	public final String routeHost;
	public final int routePort;
	public final boolean secure;
	public final int wait;
	public final int hold;

	public ConnectionSettings(final String hostName) {
		this("/http-bind", hostName, hostName, 5222, true, DEFAULT_WAIT, DEFAULT_HOLD);
	}

	public ConnectionSettings(final String httpBase, final String hostName, final String routeHost, final int routePort, final boolean secure, final int wait, final int hold) {
		this.httpBase = httpBase;
		this.hostName = hostName;
		this.routeHost = routeHost;
		this.routePort = routePort;
		this.secure = secure;
		this.wait = wait;
		this.hold = hold;
	}
}
