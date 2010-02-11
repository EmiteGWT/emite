package com.calclab.emite.xep.vcard.client;

import com.calclab.emite.core.client.packet.IPacket;

public class VCardOrganization extends VCardData {
    public VCardOrganization(IPacket packet) {
	super(packet);
    }

    public String getName() {
	return getValue("ORGNAME");
    }

    public String getUnit() {
	return getValue("ORGUNIT");
    }

}
