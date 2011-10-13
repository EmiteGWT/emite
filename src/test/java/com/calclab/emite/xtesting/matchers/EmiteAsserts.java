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

import com.calclab.emite.core.client.xml.HasXML;
import com.calclab.emite.core.client.xml.XMLBuilder;
import com.calclab.emite.core.client.xml.XMLPacket;

public class EmiteAsserts {

	public static void assertNotPacketLike(final HasXML expectedPacket, final HasXML actualPacket) {
		final IsPacketLike m = new IsPacketLike(expectedPacket);
		assertFalse("" + actualPacket + " should not match " + expectedPacket, m.matches(actualPacket, System.out));
	}

	public static void assertNotPacketLike(final String expected, final String actual) {
		final XMLPacket expectedPacket = XMLBuilder.fromXML(expected);
		final XMLPacket actualPacket = XMLBuilder.fromXML(actual);
		assertNotPacketLike(expectedPacket, actualPacket);
	}

	public static void assertPacketLike(final HasXML expectedPacket, final HasXML actualPacket) {
		final IsPacketLike m = new IsPacketLike(expectedPacket);
		assertTrue("" + actualPacket + " didn't match " + expectedPacket, m.matches(actualPacket, System.out));
	}

	public static void assertPacketLike(final String expected, final XMLPacket actual) {
		assertPacketLike(XMLBuilder.fromXML(expected), actual);
	}

	public static void assertPacketLike(final String expected, final String actual) {
		assertPacketLike(XMLBuilder.fromXML(expected), XMLBuilder.fromXML(actual));
	}
}
