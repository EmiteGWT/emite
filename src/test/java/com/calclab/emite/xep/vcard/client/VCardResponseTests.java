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

package com.calclab.emite.xep.vcard.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.xtesting.services.TigaseXMLService;

public class VCardResponseTests {

	@Test
	public void shouldParseEmptyVCard() {
		final String VCARD_EMPTY = "<iq id='v1' to='stpeter@jabber.org/roundabout' type='result'>\n" + "<vCard xmlns='vcard-temp' /></iq>";
		final IPacket result = TigaseXMLService.toPacket(VCARD_EMPTY);
		final VCardResponse response = new VCardResponse(result);
		assertEquals(IQ.Type.result, response.getType());
		assertFalse(response.hasVCard());
		assertNull(response.getVCard());
	}

	@Test
	public void shouldParseItemNotFound() {
		final String ITEM_NOT_FOUND = "<iq id='v1'\n" + "    to='stpeter@jabber.org/roundabout'\n" + "    type='error'>\n" + "  <vCard xmlns='vcard-temp'/>\n"
				+ "  <error type='cancel'>\n" + "    <item-not-found xmlns='urn:ietf:params:xml:ns:xmpp-stanzas'/>\n" + "  </error>\n" + "</iq>";
		final IPacket result = TigaseXMLService.toPacket(ITEM_NOT_FOUND);
		final VCardResponse response = new VCardResponse(result);
		assertEquals(IQ.Type.error, response.getType());
		assertTrue(response.isError());
	}

	@Test
	public void shouldParseReturnsVCard() {
		final String VCARD_RESPONSE = "<iq id='v1' to='stpeter@jabber.org/roundabout' type='result'>\n"
				+ "<vCard xmlns='vcard-temp'><FN>Peter Saint-Andre</FN></vCard></iq>";
		final IPacket result = TigaseXMLService.toPacket(VCARD_RESPONSE);
		final VCardResponse response = new VCardResponse(result);
		assertEquals(IQ.Type.result, response.getType());
		assertTrue(response.isSuccess());
		assertTrue(response.hasVCard());
		assertNotNull(response.getVCard());
	}
}
