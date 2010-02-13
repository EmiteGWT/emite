package com.calclab.emite.xep.vcard.client;

import static com.calclab.emite.core.client.packet.MatcherFactory.byName;

import com.calclab.emite.core.client.packet.DelegatedPacket;
import com.calclab.emite.core.client.packet.IPacket;

public abstract class VCardData extends DelegatedPacket {

    public VCardData(final IPacket packet) {
	super(packet);
    }

    public String getValue(final String nodeName) {
	return getFirstChild(byName(nodeName)).getText();
    }

    public void setValue(final String nodeName, final String text) {
	setTextToChild(nodeName, text);
    }

}
