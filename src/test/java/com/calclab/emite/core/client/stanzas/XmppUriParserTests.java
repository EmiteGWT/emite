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

package com.calclab.emite.core.client.stanzas;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class XmppUriParserTests {

	@Test
	public void shouldGetDomain() {
		assertEquals("host", XmppURIParser.getDomain("name@host"));
		assertEquals("host.com.net", XmppURIParser.getDomain("name@host.com.net"));
		assertEquals("host.com.net", XmppURIParser.getDomain("name@host.com.net/res"));
	}

	@Test
	public void shouldGetNode() {
		assertEquals("name", XmppURIParser.getNode("name@host"));
		assertEquals("name", XmppURIParser.getNode("xmpp:name@host"));
	}

	@Test
	public void shouldGetResource() {
		assertEquals("res", XmppURIParser.getResource("name@host/res"));
		assertEquals("res", XmppURIParser.getResource("host/res"));
	}

	@Test
	public void shouldValidateDomain() {
		assertFalse(XmppURIParser.isValidDomain("simple"));
		assertTrue(XmppURIParser.isValidDomain("simple.net"));
		assertTrue(XmppURIParser.isValidDomain("localhost"));
	}

	@Test
	public void shouldValidateJID() {
		assertTrue(XmppURIParser.isValidJid("name@localhost"));
		assertTrue(XmppURIParser.isValidJid("name@host.ext"));
		assertFalse(XmppURIParser.isValidJid("name@host"));
		assertFalse(XmppURIParser.isValidJid("name"));
		assertFalse(XmppURIParser.isValidJid("name@host.net/name"));
	}
}
