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
