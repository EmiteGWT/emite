package com.calclab.emite.core.client.packet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class NoPacketTest {

    @Test
    public void testNoPacket() {
	final IPacket noPacket = NoPacket.INSTANCE;
	assertSame(noPacket, noPacket.addChild("node", "xmlns"));
	assertNull(noPacket.getText());
	assertSame(noPacket, noPacket.getFirstChild("anyChildren"));
	assertEquals(0, noPacket.getChildren().size());
	assertEquals(0, noPacket.getChildren(MatcherFactory.byName("anyChildren")).size());
	assertFalse(noPacket.removeChild(new Packet("some")));
    }
}
