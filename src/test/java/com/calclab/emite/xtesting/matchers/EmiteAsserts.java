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

package com.calclab.emite.xtesting.matchers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.xtesting.services.TigaseXMLService;

public class EmiteAsserts {

	public static TigaseXMLService service = new TigaseXMLService();

	public static void assertNotPacketLike(final IPacket expectedPacket, final IPacket actualPacket) {
		final IsPacketLike m = new IsPacketLike(expectedPacket);
		assertFalse("" + actualPacket + " should not match " + expectedPacket, m.matches(actualPacket, System.out));
	}

	public static void assertNotPacketLike(final String expected, final String actual) {
		final IPacket expectedPacket = service.toXML(expected);
		final IPacket actualPacket = service.toXML(actual);
		assertNotPacketLike(expectedPacket, actualPacket);
	}

	public static void assertPacketLike(final IPacket expectedPacket, final IPacket actualPacket) {
		final IsPacketLike m = new IsPacketLike(expectedPacket);
		assertTrue("" + actualPacket + " didn't match " + expectedPacket, m.matches(actualPacket, System.out));
	}

	public static void assertPacketLike(final String expected, final IPacket actual) {
		assertPacketLike(service.toXML(expected), actual);
	}

	public static void assertPacketLike(final String expected, final String actual) {
		final IPacket expectedPacket = service.toXML(expected);
		final IPacket actualPacket = service.toXML(actual);
		assertPacketLike(expectedPacket, actualPacket);
	}
}
