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

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.calclab.emite.core.client.packet.NoPacket;

public class BasicStanzaTest {

	@Test
	public void shouldSetTextToChild() {
		final BasicStanza stanza = new BasicStanza("name", "xmlns");
		stanza.setTextToChild("child", "value");
		assertEquals("value", stanza.getFirstChild("child").getText());
		stanza.setTextToChild("child", null);
		assertSame(NoPacket.INSTANCE, stanza.getFirstChild("child"));
	}

	@Test
	public void shouldSetTo() {
		final BasicStanza stanza = new BasicStanza("name", "xmlns");

		stanza.setTo(uri("name@domain/resource"));
		assertEquals("name@domain/resource", stanza.getToAsString());
		stanza.setTo((XmppURI) null);
		assertNull(stanza.getToAsString());
	}
}
