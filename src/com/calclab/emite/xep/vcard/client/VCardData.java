package com.calclab.emite.xep.vcard.client;

import static com.calclab.emite.core.client.packet.MatcherFactory.byName;

import com.calclab.emite.core.client.packet.DelegatedPacket;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.NoPacket;

public abstract class VCardData extends DelegatedPacket {

    public VCardData(final IPacket packet) {
	super(packet);
    }

    public String getValue(final String nodeName) {
	return getOrCreateChild(nodeName).getText();
    }

    public void setValue(final String nodeName, final String text) {
	setTextToChild(nodeName, text);
    }

    protected IPacket getOrCreateChild(final String nodeName) {
	IPacket firstChild = getFirstChild(byName(nodeName));
	if (firstChild == NoPacket.INSTANCE) {
	    firstChild = addChild(nodeName);
	}
	return firstChild;
    }

}
