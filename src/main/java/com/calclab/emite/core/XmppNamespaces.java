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

package com.calclab.emite.core;

/**
 * Registry of all XMPP namespaces used by Emite.
 */
public final class XmppNamespaces {

	/** {@value} */
	public static final String CLIENT = "jabber:client";
	/** {@value} */
	public static final String SERVER = "jabber:server";

	/** {@value} */
	public static final String DATA = "jabber:x:data";
	/** {@value} */
	public static final String DELAY_LEGACY = "jabber:x:delay";
	/** {@value} */
	public static final String ROSTER = "jabber:iq:roster";
	/** {@value} */
	public static final String REGISTER = "jabber:iq:register";
	/** {@value} */
	public static final String SEARCH = "jabber:iq:search";
	/** {@value} */
	public static final String PRIVATE = "jabber:iq:private";
	/** {@value} */
	public static final String PRIVACY = "jabber:iq:privacy";

	/** {@value} */
	public static final String XBOSH = "urn:xmpp:xbosh";
	/** {@value} */
	public static final String DELAY = "urn:xmpp:delay";
	/** {@value} */
	public static final String SESSION = "urn:ietf:params:xml:ns:xmpp-session";
	/** {@value} */
	public static final String BIND = "urn:ietf:params:xml:ns:xmpp-bind";
	/** {@value} */
	public static final String SASL = "urn:ietf:params:xml:ns:xmpp-sasl";

	/** {@value} */
	public static final String DISCO_INFO = "http://jabber.org/protocol/disco#info";
	/** {@value} */
	public static final String DISCO_ITEMS = "http://jabber.org/protocol/disco#items";

	/** {@value} */
	public static final String MUC = "http://jabber.org/protocol/muc";
	/** {@value} */
	public static final String MUC_ADMIN = "http://jabber.org/protocol/muc#admin";
	/** {@value} */
	public static final String MUC_OWNER = "http://jabber.org/protocol/muc#owner";
	/** {@value} */
	public static final String MUC_USER = "http://jabber.org/protocol/muc#user";

	/** {@value} */
	public static final String HTTPBIND = "http://jabber.org/protocol/httpbind";
	/** {@value} */
	public static final String CHATSTATES = "http://jabber.org/protocol/chatstates";
	/** {@value} */
	public static final String NICK = "http://jabber.org/protocol/nick";

	private XmppNamespaces() {
	}

}
