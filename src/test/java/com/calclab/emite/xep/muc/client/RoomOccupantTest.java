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

package com.calclab.emite.xep.muc.client;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.calclab.emite.core.client.xmpp.stanzas.Presence.Show;

public class RoomOccupantTest {

	@Test
	public void shouldAnswerUnknownStatusWhenNotValid() {
		final Occupant occupant = new Occupant(uri("user@domain"), uri("room@domain/user"), "not valid affiliation", "not valid role", Show.unknown, "message");
		assertEquals(Occupant.Affiliation.none, occupant.getAffiliation());
		assertEquals(Occupant.Role.unknown, occupant.getRole());
		assertEquals(Show.unknown, occupant.getShow());
	}
}
