package com.calclab.emite.xep.vcard.client;

import static com.calclab.emite.core.client.packet.MatcherFactory.byName;

import com.calclab.emite.core.client.packet.IPacket;

public abstract class VCardData {
    protected final IPacket packet;

    public VCardData(IPacket packet) {
	this.packet = packet;
    }

    public String getValue(String nodeName) {
	return packet.getFirstChild(byName(nodeName)).getText();
    }

}
