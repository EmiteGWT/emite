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

package com.calclab.emite.core.stanzas;

import static com.calclab.emite.core.XmppURI.uri;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.calclab.emite.core.stanzas.Stanza;

public class BasicStanzaTest {

	@Test
	public void shouldSetTextToChild() {
		final Stanza stanza = new Stanza("name", "xmlns");
		stanza.getXML().setChildText("child", "value");
		assertEquals("value", stanza.getXML().getFirstChild("child").getText());
		stanza.getXML().setChildText("child", null);
		assertSame(null, stanza.getXML().getFirstChild("child"));
	}

	@Test
	public void shouldSetTo() {
		final Stanza stanza = new Stanza("name", "xmlns");

		stanza.setTo(uri("name@domain/resource"));
		assertEquals("name@domain/resource", stanza.getTo().toString());
		stanza.setTo(null);
		assertNull(stanza.getTo());
	}
}
