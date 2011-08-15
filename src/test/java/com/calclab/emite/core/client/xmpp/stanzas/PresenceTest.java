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
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.calclab.emite.core.client.packet.Packet;
import com.calclab.emite.core.client.xmpp.stanzas.Presence.Show;
import com.calclab.emite.core.client.xmpp.stanzas.Presence.Type;

public class PresenceTest {

	@Test
	public void shouldGetPriority() {
		Presence presence = new Presence();
		assertEquals(0, presence.getPriority());
		presence = new Presence(new Packet("presence").With(new Packet("priority").WithText("5")));
		assertEquals(5, presence.getPriority());
		presence = new Presence(new Packet("presence").With(new Packet("priority").WithText("not valid")));
		assertEquals(0, presence.getPriority());
	}

	@Test
	public void shouldGetShow() {
		Presence presence = new Presence();
		assertEquals(Show.notSpecified, presence.getShow());
		presence = new Presence(new Packet("presence").With(new Packet("show").WithText(Show.chat.toString())));
		assertEquals(Show.chat, presence.getShow());
		presence = new Presence(new Packet("presence").With(new Packet("show").WithText("not valid show")));
		assertEquals(Show.unknown, presence.getShow());
	}

	@Test
	public void shouldGetStatus() {
		Presence presence = new Presence();
		assertNull(presence.getStatus());
		presence = new Presence(new Packet("presence").With(new Packet("status").WithText("the status")));
		assertEquals("the status", presence.getStatus());
	}

	@Test
	public void shouldGetType() {
		Presence presence = new Presence();
		assertEquals(null, presence.getType());
		presence = new Presence(new Packet("presence").With("type", Type.probe.toString()));
		assertEquals(Type.probe, presence.getType());
		presence = new Presence(new Packet("presence").With("type", "not valid"));
		assertEquals(Type.error, presence.getType());
	}

	@Test
	public void shouldHandleNotSpecifiedPresence() {
		final Presence presence = new Presence();
		presence.setShow(Show.away);
		presence.setShow(null);
		assertEquals(Presence.Show.notSpecified, presence.getShow());
		assertEquals("<presence />", presence.toString());
	}

	@Test
	public void shouldSetPriority() {
		final Presence presence = new Presence();
		presence.setPriority(1);
		assertEquals(1, presence.getPriority());
	}

	@Test
	public void shouldSetShow() {
		final Presence presence = new Presence();
		for (final Show value : Show.values()) {
			presence.setShow(value);
			if (value == Show.unknown) {
				assertEquals(Show.notSpecified, presence.getShow());
			} else {
				assertEquals(value, presence.getShow());
			}
		}
	}

	/**
	 * Show values unknown and show should never be serialized to the stanza.
	 * Issue 264
	 */
	@Test
	public void shouldSetShowCorrectly() {
		final Presence presence = new Presence();
		presence.setShow(Show.unknown);
		assertEquals(-1, presence.toString().indexOf("unknown"));
	}

	@Test
	public void shouldSetStatus() {
		final Presence presence = new Presence();
		presence.setStatus("the status");
		assertEquals("the status", presence.getStatus());
		presence.setStatus("the status2");
		assertEquals("the status2", presence.getStatus());
	}

	@Test
	public void shouldSetType() {
		final Presence presence = new Presence();
		for (final Type type : Type.values()) {
			presence.setType(type.toString());
			assertEquals(type, presence.getType());
		}
	}
}
