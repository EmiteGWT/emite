package com.calclab.emite.xep.vcard.client;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.Packet;

public class VCardAddress extends VCardData {

    public static enum Data {
	EXTADD, STREET, LOCALITY, REGION, PCODE, CTRY
    }
    public static enum Place {
	WORK, HOME
    }

    public VCardAddress() {
	this(new Packet(VCard.ADR));
    }

    public VCardAddress(final IPacket packet) {
	super(packet);
    }

    public String getData(final Data data) {
	return getValue(data.toString());
    }

    public boolean hasPlace(final Place place) {
	return hasChild(place.toString());
    }

    public void setData(final Data data, final String value) {
	setValue(data.toString(), value);
    }

}
