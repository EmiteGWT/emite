package com.calclab.emite.xep.disco.client;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.Packet;

public class FeatureTest {

    @Test
    public void shouldParsePacket() {
	final IPacket packet = new Packet("feature").With("var", "protocol");
	final Feature feature = Feature.fromPacket(packet);
	assertEquals("protocol", feature.var);
    }
}
