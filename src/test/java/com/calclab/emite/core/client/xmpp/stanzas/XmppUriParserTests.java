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

package com.calclab.emite.core.client.xmpp.stanzas;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class XmppUriParserTests {

	@Test
	public void shouldGetDomain() {
		assertEquals("host", XmppUriParser.getDomain("name@host"));
		assertEquals("host.com.net", XmppUriParser.getDomain("name@host.com.net"));
		assertEquals("host.com.net", XmppUriParser.getDomain("name@host.com.net/res"));
	}

	@Test
	public void shouldGetNode() {
		assertEquals("name", XmppUriParser.getNode("name@host"));
		assertEquals("name", XmppUriParser.getNode("xmpp:name@host"));
	}

	@Test
	public void shouldGetResource() {
		assertEquals("res", XmppUriParser.getResource("name@host/res"));
		assertEquals("res", XmppUriParser.getResource("host/res"));
	}

	@Test
	public void shouldValidateDomain() {
		assertFalse(XmppUriParser.isValidDomain("simple"));
		assertTrue(XmppUriParser.isValidDomain("simple.net"));
		assertTrue(XmppUriParser.isValidDomain("localhost"));
	}

	@Test
	public void shouldValidateJID() {
		assertTrue(XmppUriParser.isValidJid("name@localhost"));
		assertTrue(XmppUriParser.isValidJid("name@host.ext"));
		assertFalse(XmppUriParser.isValidJid("name@host"));
		assertFalse(XmppUriParser.isValidJid("name"));
		assertFalse(XmppUriParser.isValidJid("name@host.net/name"));
	}
}
