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

package com.calclab.emite.core.client.packet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class NoPacketTest {

	@Test
	public void testNoPacket() {
		final IPacket noPacket = NoPacket.INSTANCE;
		assertSame(noPacket, noPacket.addChild("node", "xmlns"));
		assertNull(noPacket.getText());
		assertSame(noPacket, noPacket.getFirstChild("anyChildren"));
		assertEquals(0, noPacket.getChildren().size());
		assertEquals(0, noPacket.getChildren(MatcherFactory.byName("anyChildren")).size());
		assertFalse(noPacket.removeChild(new Packet("some")));
	}
}
