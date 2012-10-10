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

import javax.annotation.concurrent.Immutable;

/**
 * BOSH connection settings.
 */
@Immutable
public final class ConnectionSettings {
	
	/**
	 * Maximum number of open BOSH requests.
	 */
	public static final int MAX_REQUESTS = 2;

	private final String httpBase;
	private final String hostName;
	private final String routeHost;
	private final int routePort;
	private final boolean secure;
	private final int wait;
	private final int hold;

	/**
	 * Create new connection settings with default values.
	 * 
	 * @param hostName host name of the XMPP server
	 */
	public ConnectionSettings(final String hostName) {
		this("/http-bind", hostName, hostName, 5222, true, 60, 1);
	}

	/**
	 * Create new connection settings.
	 * 
	 * @param httpBase BOSH URL
	 * @param hostName host name of the XMPP server
	 * @param routeHost host name of the routing XMPP server
	 * @param routePort port of the routing XMPP server
	 * @param secure use secure connection
	 * @param wait wait time (in seconds) before responding to a request
	 * @param hold maximum number of waiting requests
	 */
	public ConnectionSettings(final String httpBase, final String hostName, final String routeHost, final int routePort, final boolean secure, final int wait, final int hold) {
		this.httpBase = httpBase;
		this.hostName = hostName;
		this.routeHost = routeHost;
		this.routePort = routePort;
		this.secure = secure;
		this.wait = wait;
		this.hold = hold;
	}

	/**
	 * @return httpBase
	 */
	public final String getHttpBase() {
		return httpBase;
	}

	/**
	 * @return hostName
	 */
	public final String getHostName() {
		return hostName;
	}

	/**
	 * @return routeHost
	 */
	public final String getRouteHost() {
		return routeHost;
	}

	/**
	 * @return routePort
	 */
	public final int getRoutePort() {
		return routePort;
	}

	/**
	 * @return secure
	 */
	public final boolean isSecure() {
		return secure;
	}

	/**
	 * @return wait
	 */
	public final int getWait() {
		return wait;
	}

	/**
	 * @return hold
	 */
	public final int getHold() {
		return hold;
	}
	
}
