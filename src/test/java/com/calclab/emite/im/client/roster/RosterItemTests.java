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

package com.calclab.emite.im.client.roster;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.Packet;
import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.emite.xtesting.matchers.EmiteAsserts;
import com.calclab.emite.xtesting.services.TigaseXMLService;

public class RosterItemTests {

	@Test
	public void shouldConvertToStanza() {
		final RosterItem item = new RosterItem(uri("name@domain/RESOURCE"), null, "TheName", null);
		item.addToGroup("group1");
		item.addToGroup("group2");
		EmiteAsserts.assertPacketLike("<item jid='name@domain' name='TheName'>" + "<group>group1</group><group>group2</group></item>",
				item.addStanzaTo(new Packet("all")));
	}

	@Test
	public void shouldIgnoreEmptyGroups() {
		final RosterItem item = new RosterItem(uri("name@domain/RESOURCE"), null, "TheName", null);
		item.addToGroup(null);
		item.addToGroup(" ");
		assertEquals(0, item.getGroups().size());
	}

	@Test
	public void shouldParseStanza() {
		final RosterItem item = RosterItem.parse(p("<item jid='romeo@example.net' ask='subscribe' name='R' subscription='both'>"
				+ "<group>Friends</group><group>X</group></item>"));
		assertEquals("R", item.getName());
		assertEquals("R", item.getName());
		assertEquals(Presence.Type.subscribe, item.getAsk());
		assertEquals(2, item.getGroups().size());
		assertTrue(item.getGroups().contains("Friends"));
		assertTrue(item.getGroups().contains("X"));
	}

	private IPacket p(final String xml) {
		final IPacket packet = TigaseXMLService.toPacket(xml);
		return packet;
	}
}
