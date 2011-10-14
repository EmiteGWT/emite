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

import java.io.PrintStream;
import java.util.List;
import java.util.Map;

import com.calclab.emite.core.client.xml.HasXML;
import com.calclab.emite.core.client.xml.XMLBuilder;
import com.calclab.emite.core.client.xml.XMLPacket;

public class IsPacketLike {
	public static IsPacketLike build(final String xml) {
		return new IsPacketLike(XMLBuilder.fromXML(xml));
	}

	private final XMLPacket original;

	public IsPacketLike(final HasXML expected) {
		original = expected.getXML();
	}

	public boolean matches(final HasXML actual, final PrintStream out) {
		final String result = areEquals(original, actual.getXML());
		out.print(result);
		return result == null;
	}

	private String areContained(final XMLPacket expectedChild, final List<XMLPacket> children) {
		final int total = children.size();
		for (int index = 0; index < total; index++) {
			final XMLPacket actual = children.get(index);
			final String result = areEquals(expectedChild, actual);
			if (result == null)
				return null;
		}
		return fail("child is not contained in children", expectedChild.toString(), toString(children));
	}

	private String areEquals(final XMLPacket expected, final XMLPacket actual) {
		if (actual == null)
			return fail("element", expected.toString(), "[null]");
		if (expected.getTagName().equals(actual.getTagName())) {
			final Map<String, String> atts = expected.getAttributes();
			for (final String name : atts.keySet()) {
				if (expected.getAttribute(name) != null || !expected.getAttribute(name).equals(actual.getAttribute(name)))
					return fail("attribute " + name, expected.getAttribute(name), actual.getAttribute(name));
			}
		} else
			return fail("element name", expected.getTagName(), actual.getTagName());

		final String expectedText = expected.getText();
		if (expectedText != null && !expectedText.equals(actual.getText()))
			return fail("text value", expectedText, actual.getText());

		final List<XMLPacket> expChildren = expected.getChildren();
		final List<XMLPacket> actChildren = actual.getChildren();

		for (final XMLPacket expectedChild : expChildren) {
			final String result = areContained(expectedChild, actChildren);
			if (result != null)
				return result;
		}
		return null;
	}

	private String fail(final String target, final String expected, final String actual) {
		return "failed on " + target + ". expected: " + expected + " but was " + actual;
	}

	private String toString(final List<XMLPacket> children) {
		final StringBuilder buffer = new StringBuilder("[");
		for (final XMLPacket child : children) {
			buffer.append(child.toString());
		}
		buffer.append("]");
		return buffer.toString();
	}
}
