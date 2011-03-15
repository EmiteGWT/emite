package com.calclab.emite.xep.vcard.client;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.Packet;

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

    public VCardTelephone() {
	this(new Packet(VCard.TEL));
    }

    public VCardTelephone(final IPacket packet) {
	super(packet);
    }

    public String getNumber() {
	return getValue(Data.NUMBER);
    }

    public String getValue(final Data data) {
	return getValue(data.toString());
    }

    public boolean hasPlace(final Place place) {
	return hasChild(place.toString());
    }

    public boolean hasService(final Service service) {
	return hasChild(service.toString());
    }

    public void setNumber(final String number) {
	setValue(Data.NUMBER.toString(), number);
    }
}
