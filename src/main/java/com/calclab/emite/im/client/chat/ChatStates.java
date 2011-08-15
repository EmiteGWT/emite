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

package com.calclab.emite.im.client.chat;

/**
 * Common chat states. Chat states are not limited to this ones.
 */
public class ChatStates {
	/**
	 * the chat is ready to be used
	 */
	public static final String ready = "ready";
	/**
	 * the chat is opened but can't be used (maybe waiting for a server
	 * confirmation or because the session is closed or the connection is lost)
	 */
	public static final String locked = "locked";

	public static final boolean isLocked(final String state) {
		return locked.equals(state);
	}

	public static final boolean isReady(final String state) {
		return ready.equals(state);
	}
}