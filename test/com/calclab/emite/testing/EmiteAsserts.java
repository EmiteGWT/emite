package com.calclab.emite.testing;

import static com.calclab.emite.testing.MockitoEmiteHelper.toXML;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.calclab.emite.core.client.packet.IPacket;

public class EmiteAsserts {

    public static void assertNotPacketLike(final IPacket expectedPacket, final IPacket actualPacket) {
	final IsPacketLike m = new IsPacketLike(expectedPacket);
	assertFalse("" + actualPacket + " should not match " + expectedPacket, m.matches(actualPacket, System.out));
    }

    public static void assertNotPacketLike(final String expected, final String actual) {
	final IPacket expectedPacket = toXML(expected);
	final IPacket actualPacket = toXML(actual);
	assertNotPacketLike(expectedPacket, actualPacket);
    }

    public static void assertPacketLike(final IPacket expectedPacket, final IPacket actualPacket) {
	final IsPacketLike m = new IsPacketLike(expectedPacket);
	assertTrue("" + actualPacket + " didn't match " + expectedPacket, m.matches(actualPacket, System.out));
    }

    public static void assertPacketLike(final String expected, final IPacket actual) {
	assertPacketLike(toXML(expected), actual);
    }

    public static void assertPacketLike(final String expected, final String actual) {
	final IPacket expectedPacket = toXML(expected);
	final IPacket actualPacket = toXML(actual);
	assertPacketLike(expectedPacket, actualPacket);
    }
}
