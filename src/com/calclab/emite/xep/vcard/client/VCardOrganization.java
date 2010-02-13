package com.calclab.emite.xep.vcard.client;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.Packet;

public class VCardOrganization extends VCardData {
    public static enum Data {
	ORGNAME, ORGUNIT
    }

    public VCardOrganization() {
	this(new Packet(VCard.ORG));
    }

    public VCardOrganization(final IPacket packet) {
	super(packet);
    }

    public String getData(final Data data) {
	return getValue(data.toString());
    }

    public void setData(final Data data, final String value) {
	setValue(data.toString(), value);
    }

}
