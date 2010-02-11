package com.calclab.emite.xep.vcard.client;

import com.calclab.emite.core.client.packet.IPacket;

public class VCardTelephone extends VCardData {

    public static enum Data {
	POBOX, EXTADD, STREET, LOCALITY, REGION, PCODE, CTRY, NUMBER
    }
    public static enum Place {
	WORK, HOME
    }
    public static enum Service {
	POSTAL, PARCEL, DOM, INTL, PREF, VOICE
    }

    public VCardTelephone(IPacket packet) {
	super(packet);
    }

    public String getNumber() {
	return getValue(Data.NUMBER);
    }

    public String getValue(Data data) {
	return getValue(data.toString());
    }

    public boolean hasPlace(Place place) {
	return packet.hasChild(place.toString());
    }

    public boolean hasService(Service service) {
	return packet.hasChild(service.toString());
    }
}
