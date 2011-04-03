package com.calclab.emite.xtesting.services;

import org.junit.Test;

import com.calclab.emite.core.client.packet.AbstractHelperTest;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.PacketTestSuite;

public class TigasePacketTest {

    @Test
    public void testPacket() {
	PacketTestSuite.runPacketTests(new AbstractHelperTest() {
	    @Override
	    public IPacket createPacket(final String name) {
		return new TigasePacket(name);
	    }
	});
    }
}
