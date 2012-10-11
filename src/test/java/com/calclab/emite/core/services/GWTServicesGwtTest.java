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

package com.calclab.emite.core.services;

import java.util.List;

import org.junit.Test;

import com.calclab.emite.base.xml.XMLBuilder;
import com.calclab.emite.base.xml.XMLPacket;
import com.google.gwt.junit.client.GWTTestCase;

public class GWTServicesGwtTest extends GWTTestCase {

	@Override
	public String getModuleName() {
		return "com.calclab.emite.core.EmiteCore";
	}

	@Test
	public void testToXML() {
		final String textXml = "<test attr=\"&quot;&lt;&amp;&gt;&#39;\"><child /><childWithText>&quot;&lt;&amp;&gt;&#39;</childWithText></test>";

		final XMLPacket result = XMLBuilder.fromXML(textXml);

		assertEquals("Root has wrong number of attributes", 1, result.getAttributes().size());
		assertEquals("Attribute not correct", "\"<&>'", result.getAttribute("attr"));

		final List<XMLPacket> children = result.getChildren();

		assertEquals("First child not found", "child", children.get(0).getTagName());
		assertEquals("First child has wrong number of children", 0, children.get(0).getChildren().size());
		assertEquals("First child has wrong number of attributes", 0, children.get(0).getAttributes().size());

		assertEquals("Second child not found", "childWithText", children.get(1).getTagName());
		assertEquals("Second child has wrong number of children", 1, children.get(1).getChildren().size());
		assertEquals("Second child has wrong number of attributes", 0, children.get(1).getAttributes().size());

		assertEquals("Second child has wrong text node", "\"<&>'", children.get(1).getText());
	}

}
