/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008-2009 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emite.core.client.packet;

import java.util.List;

/**
 * A multi-environment class to test packets
 */
public final class PacketTestSuite {

    public static interface Helper {
	public void assertEquals(Object expected, Object actual);

	public void assertTrue(String message, boolean condition);

	public IPacket createPacket(String nodeName);

	public void log(String message);
    }

    private static class HelperExtended implements Helper {
	private final Helper delegate;

	public HelperExtended(final Helper delegate) {
	    this.delegate = delegate;
	}

	public void assertEquals(final Object expected, final Object actual) {
	    delegate.assertEquals(expected, actual);
	}

	public void assertFalse(final boolean condition) {
	    assertTrue("", !condition);
	}

	public void assertNotNull(final Object o) {
	    assertTrue("should be NOT null: " + safeString(o), o != null);
	}

	public void assertNull(final Object value) {
	    assertTrue("should be null: " + safeString(value), value == null);
	}

	public void assertSame(final Object expected, final Object actual) {
	    assertTrue("should be same: " + safeString(expected) + " but was: " + safeString(actual),
		    expected == actual);
	}

	public void assertTrue(final boolean condition) {
	    assertTrue("", condition);
	}

	public void assertTrue(final String message, final boolean condition) {
	    delegate.assertTrue(message, condition);
	}

	public IPacket createPacket(final String nodeName) {
	    return delegate.createPacket(nodeName);
	}

	public void log(final String message) {
	    delegate.log(message);
	}

	public String safeString(final Object value) {
	    return value == null ? "[null]" : value.toString();
	}

    }

    public static void runPacketTests(final Helper utility) {
	final HelperExtended helper = new HelperExtended(utility);
	shouldNeverReturnNullWhenGetChildren(helper);
	shouldReturnNoPacketWhenGetFirstChild(helper);
	shouldSetAndClearTheAttributes(helper);
	shouldSetText(helper);
	shouldRemoveChildIfPresent(helper);
	shouldRenderAttributes(helper);
	shouldRenderChilds(helper);
	shouldRenderTextChildren(helper);
	shouldScapeText(helper);
    }

    private static void shouldNeverReturnNullWhenGetChildren(final HelperExtended helper) {
	helper.log("- shouldNeverReturnNullWhenGetChildren");
	final IPacket packet = helper.createPacket("root");
	final List<? extends IPacket> children = packet.getChildren();
	helper.assertNotNull(children);
	helper.assertEquals(0, children.size());
	helper.log("- test ends");
    }

    private static void shouldRemoveChildIfPresent(final HelperExtended helper) {
	helper.log("- shouldRemoveChildIfPresent");
	final IPacket root = helper.createPacket("packet");
	helper.assertFalse(root.removeChild(helper.createPacket("otherPacket")));
	final IPacket child = root.addChild("child", null);
	helper.assertEquals(1, root.getChildrenCount());
	helper.assertTrue(root.removeChild(child));
	helper.assertEquals(0, root.getChildrenCount());
	helper.log("- test ends");
    }

    private static void shouldRenderAttributes(final HelperExtended helper) {
	final IPacket packet = helper.createPacket("root").With("attribute", "value");
	helper.assertEquals("<root attribute=\"value\" />", PacketRenderer.toString(packet));
    }

    private static void shouldRenderChilds(final HelperExtended helper) {
	final IPacket packet = helper.createPacket("level0");
	final IPacket child = packet.addChild("level1", null);
	child.addChild("level2", null);
	helper.assertEquals("<level0><level1><level2 /></level1></level0>", PacketRenderer.toString(packet));
    }

    private static void shouldRenderTextChildren(final HelperExtended helper) {
	final IPacket root = helper.createPacket("root");
	root.setText("the text");
	helper.assertEquals("<root>the text</root>", PacketRenderer.toString(root));
    }

    private static void shouldReturnNoPacketWhenGetFirstChild(final HelperExtended helper) {
	helper.log("- shouldReturnNoPacketWhenGetFirstChild");
	final IPacket packet = helper.createPacket("root");
	final IPacket child = packet.getFirstChild("child");
	helper.assertNotNull(child);
	helper.assertSame(NoPacket.INSTANCE, child);
	helper.log("- test ends");
    }

    private static void shouldScapeText(final HelperExtended helper) {
	final IPacket packet = helper.createPacket("body");
	packet.setText("&");
	helper.assertEquals("<body>&amp;</body>", packet.toString());
    }

    private static void shouldSetAndClearTheAttributes(final HelperExtended helper) {
	helper.log("- shouldSetAndClearTheAttributes");
	final IPacket packet = helper.createPacket("packet");
	packet.setAttribute("name", "value");
	helper.assertEquals("value", packet.getAttribute("name"));
	packet.setAttribute("name", null);
	helper.assertNull(packet.getAttribute("name"));
	helper.assertFalse(packet.hasAttribute("name"));
	helper.log("- test ends");
    }

    private static void shouldSetText(final HelperExtended helper) {
	helper.log("- shouldSetAndClearTheAttributes");
	final IPacket packet = helper.createPacket("packet");
	packet.setText("text1");
	helper.assertEquals("text1", packet.getText());
	packet.setText("text2");
	helper.assertEquals("text2", packet.getText());
	helper.log("- test ends");
    }

}
