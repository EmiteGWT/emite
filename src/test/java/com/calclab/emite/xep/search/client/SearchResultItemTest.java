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

package com.calclab.emite.xep.search.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.calclab.emite.xtesting.services.TigaseXMLService;

public class SearchResultItemTest {

	@Test
	public void shouldHaveFirstIfPresent() {
		final SearchResultItem result = SearchResultItem.parse(TigaseXMLService.toPacket("<item jid='juliet@capulet.com'>" + "<first>Juliet</first></item>"));
		assertEquals("juliet@capulet.com", result.getJid().toString());
		assertNull(result.getEmail());
		assertEquals("Juliet", result.getFirst());
		assertNull(result.getLast());
		assertNull(result.getNick());
	}

	@Test
	public void shouldHaveJIDIfPresent() {
		final SearchResultItem result = SearchResultItem.parse(TigaseXMLService.toPacket("<item jid='juliet@capulet.com'>" + "</item>"));
		assertEquals("juliet@capulet.com", result.getJid().toString());
		assertNull(result.getEmail());
		assertNull(result.getFirst());
		assertNull(result.getLast());
		assertNull(result.getNick());
	}

	@Test
	public void shouldHaveLastIfPresent() {
		final SearchResultItem result = SearchResultItem.parse(TigaseXMLService.toPacket("<item jid='juliet@capulet.com'>" + "<last>Capulet</last></item>"));
		assertEquals("juliet@capulet.com", result.getJid().toString());
		assertNull(result.getEmail());
		assertNull(result.getFirst());
		assertEquals("Capulet", result.getLast());
		assertNull(result.getNick());
	}
}
