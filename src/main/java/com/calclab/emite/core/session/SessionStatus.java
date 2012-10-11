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

package com.calclab.emite.core.session;

/**
 * Different (and common) session status. The different status paths are:
 * <ul>
 * <li>Successfull login: (disconnected) - connecting - authorized - loggedIn -
 * ready</li>
 * <li>Unsuccessfull login: (disconnected) - connecting - notAuthorized -
 * disconected</li>
 * <li>Loging out: (ready) - loggingOut - disconnected</li>
 * </ul>
 * 
 * Session can have other status different from this ones.
 */
public enum SessionStatus {
	/**
	 * The authorization was successfull. You can NOT send stanzas using the
	 * session (stanzas will be queued). If you need to send stanzas, use the
	 * connection object directly
	 */
	authorized,

	/**
	 * You are logged in. This is the first status when you can send stanzas.
	 */
	loggedIn,

	/**
	 * Start login process. You can NOT send stanzas using session (you should
	 * use the connection directly)
	 */
	connecting,

	/**
	 * We are disconnected. You can NOT send stanzas.
	 */
	disconnected, error, notAuthorized,

	/**
	 * The session is ready to use. All the queued stanzas are sent just before
	 * this status.
	 */
	ready,
	
	/**
	 * We are logging out. Last oportunity to send stanzas (i.e: last presence).
	 * session.getCurrentUser() returns the current user;
	 */
	loggingOut,

	/**
	 * We are resuming a session. When resuming a session you only receive
	 * "resuming" and "ready" (not loggedIn)
	 */
	resume,
	
	/**
	 * The session is binded
	 */
	binded,

	/**
	 * The roster is ready
	 */
	rosterReady;

	/**
	 * Helper function to determine if the given status is disconnected
	 */
	public static final boolean isDisconnected(final SessionStatus status) {
		return disconnected.equals(status);
	}

	/**
	 * Helper function to determine if the given status is ready
	 */
	public static final boolean isReady(final SessionStatus status) {
		return ready.equals(status) || rosterReady.equals(status);
	}

}