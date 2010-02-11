package com.calclab.emite.xep.vcard.client;

import com.calclab.emite.core.client.packet.IPacket;

public class VCardAddress extends VCardData {

    public static enum Data {
	EXTADD, STREET, LOCALITY, REGION, PCODE, CTRY
    }
    public static enum Place {
	WORK, HOME
    }

    public VCardAddress(IPacket packet) {
	super(packet);
    }

    public String getData(Data data) {
	return getValue(data.toString());
    }

    public boolean hasPlace(Place place) {
	return packet.hasChild(place.toString());
    }

}
