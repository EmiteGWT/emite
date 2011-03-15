package com.calclab.emite.core.client.packet;

import org.junit.Test;

public class PacketTest {
    @Test
    public void testPacket() {
	PacketTestSuite.runPacketTests(new AbstractHelper() {
	    public IPacket createPacket(final String name) {
		return new Packet(name);
	    }
	});
    }

}
