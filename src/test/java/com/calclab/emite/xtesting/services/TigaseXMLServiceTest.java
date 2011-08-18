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

package com.calclab.emite.xtesting.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.calclab.emite.core.client.packet.IPacket;

public class TigaseXMLServiceTest {

	final String response = "<body xmlns=\"http://jabber.org/protocol/httpbind\" xmlns:stream=\"http://etherx.jabber.org/streams\" "
			+ "authid=\"27343471\" sid=\"27343471\" secure=\"true\" requests=\"2\" inactivity=\"30\" polling=\"5\" wait=\"60\" ver=\"1.6\">"
			+ "<stream:features><mechanisms xmlns=\"urn:ietf:params:xml:ns:xmpp-sasl\"><mechanism>DIGEST-MD5</mechanism>"
			+ "<mechanism>PLAIN</mechanism><mechanism>ANONYMOUS</mechanism><mechanism>CRAM-MD5</mechanism>"
			+ "</mechanisms><compression xmlns=\"http://jabber.org/features/compress\"><method>zlib</method></compression>"
			+ "<bind xmlns=\"urn:ietf:params:xml:ns:xmpp-bind\"/><session xmlns=\"urn:ietf:params:xml:ns:xmpp-session\"/>" + "</stream:features></body>";

	@Test
	public void testReal() {
		final TigaseXMLService parser = new TigaseXMLService();
		final IPacket body = parser.toXML(response);
		final List<? extends IPacket> stanzas = body.getChildren();
		assertEquals(1, stanzas.size());
		final IPacket features = stanzas.get(0);
		assertTrue(features.getName().equals("stream:features") || features.getName().equals("features"));
		final IPacket mechanisms = features.getChildren().get(0);
		assertEquals("mechanisms", mechanisms.getName());
		final IPacket mec1 = mechanisms.getChildren().get(0);
		assertEquals("mechanism", mec1.getName());
		assertEquals("DIGEST-MD5", mec1.getText());
	}
}
