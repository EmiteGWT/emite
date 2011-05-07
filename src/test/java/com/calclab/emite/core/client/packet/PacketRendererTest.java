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

import org.junit.Test;

/**
 * @author Ash
 * 
 */
public class PacketRendererTest {

	/**
	 * Test method for
	 * {@link com.calclab.emite.core.client.packet.PacketRenderer#toString(com.calclab.emite.core.client.packet.IPacket)}
	 * .
	 */
	@Test
	public void testToStringIPacket() {
		final Packet testPacket = new Packet("test");
		final Packet testChild = new Packet("child");
		final Packet testChildWithText = new Packet("childWithText");

		testChildWithText.setText("\"<&>'");

		testPacket.setAttribute("attr", "\"<&>'");

		testPacket.addChild(testChild);
		testPacket.addChild(testChildWithText);

		final String result = PacketRenderer.toString(testPacket);

		// We should probably do something cleverer here really as the xml may
		// not necessarily always have to come out the same to still be correct
		assertEquals("XML has not been rendered as expected",
				"<test attr=\"&quot;&lt;&amp;&gt;&#39;\"><child /><childWithText>&quot;&lt;&amp;&gt;&#39;</childWithText></test>", result);
	}

}
