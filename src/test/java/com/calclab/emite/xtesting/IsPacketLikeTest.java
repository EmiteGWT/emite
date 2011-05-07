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

package com.calclab.emite.xtesting;

import static com.calclab.emite.xtesting.matchers.EmiteAsserts.assertNotPacketLike;
import static com.calclab.emite.xtesting.matchers.EmiteAsserts.assertPacketLike;

import org.junit.Test;

public class IsPacketLikeTest {

	@Test
	public void testAttributes() {
		assertPacketLike("<iq type='result' />", "<iq type='result' />");
		assertNotPacketLike("<iq type='result' />", "<iq type='result2' />");
		assertPacketLike("<iq type='result' />", "<iq type='result' value='something here' />");
	}

	@Test
	public void testChildren() {
		assertNotPacketLike("<iq><child att='value' /></iq>", "<iq></iq>");
		assertNotPacketLike("<iq><child att='value' /></iq>", "<iq><child/></iq>");
		assertPacketLike("<iq><child att='value' /></iq>", "<iq><child att='value' /><child2 /></iq>");
	}

	@Test
	public void testChildrenText() {
		assertNotPacketLike("<presence><show>chat</show></presence>", "<presence><show>dnd</show></presence>");
	}

	@Test
	public void testMultipleChildren() {
		assertPacketLike("<root><child2/></root>", "<root><child1/><child2/></root>");
		assertPacketLike("<root><child1/></root>", "<root><child1/><child2/></root>");
		assertNotPacketLike("<root><child3/></root>", "<root><child1/><child2/></root>");
	}

	@Test
	public void testNestedChildren() {
		assertPacketLike("<iq><child/></iq>", "<iq><child><subchild/></child></iq>");
	}

}
