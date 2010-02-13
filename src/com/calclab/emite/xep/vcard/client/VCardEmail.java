package com.calclab.emite.xep.vcard.client;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.Packet;

public class VCardEmail extends VCardData {

    public static enum Type {
	INTERNET, x400
    }

    private static final String PREF = "PREF";
    private static final String USERID = "USERID";

    public VCardEmail() {
	this(new Packet(VCard.EMAIL).addChild(new Packet(USERID)));
	setType(Type.INTERNET);
    }

    public VCardEmail(final IPacket packet) {
	super(packet);
    }

    public String get() {
	return getValue(USERID);
    }

    public boolean isPref() {
	return hasChild(PREF);
    }

    public boolean isType(final Type type) {
	return hasChild(type.toString());
    }

    public void set(final String email) {
	setValue(USERID, email);
    }

    public void setPref() {
	if (!isPref()) {
	    addChild(PREF);
	}
    }

    public void setType(final Type type) {
	if (!hasChild(type.toString())) {
	    addChild(type.toString());
	}
    }

}
