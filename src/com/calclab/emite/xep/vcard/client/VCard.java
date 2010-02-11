package com.calclab.emite.xep.vcard.client;

import static com.calclab.emite.core.client.packet.MatcherFactory.byName;

import java.util.ArrayList;
import java.util.List;

import com.calclab.emite.core.client.packet.IPacket;

public class VCard extends VCardData {

    public static enum Data {
	DESC, FN, JABBERID, NICKNAME, TITLE, URL
    }
    public static enum Name {
	FAMILY, GIVEN, MIDDLE
    }

    private IPacket nameChild;
    private List<VCardTelephone> telephones;
    private List<VCardAddress> addresses;

    public VCard(IPacket packet) {
	super(packet);
    }

    public List<VCardAddress> getAddresses() {
	if (addresses == null) {
	    addresses = new ArrayList<VCardAddress>();
	    List<? extends IPacket> children = packet.getChildren(byName("ADR"));
	    for (IPacket child : children) {
		addresses.add(new VCardAddress(child));
	    }
	}
	return addresses;
    }

    public String getDescription() {
	return getValue(Data.DESC);
    }

    public String getDisplayName() {
	return getValue(Data.FN);
    }

    public String getFamilyName() {
	return getName(Name.FAMILY);
    }

    public String getGivenName() {
	return getName(Name.GIVEN);
    }

    public String getJabberID() {
	return getValue(Data.JABBERID);
    }

    public String getMiddleName() {
	return getName(Name.MIDDLE);
    }

    public String getName(Name name) {
	return getName().getFirstChild(byName(name.toString())).getText();
    }

    public String getNickName() {
	return getValue(Data.NICKNAME);
    }

    public VCardOrganization getOrganization() {
	return new VCardOrganization(packet.getFirstChild(byName("ORG")));

    }

    public List<VCardTelephone> getTelephones() {
	if (telephones == null) {
	    telephones = new ArrayList<VCardTelephone>();
	    List<? extends IPacket> children = packet.getChildren(byName("TEL"));
	    for (IPacket child : children) {
		telephones.add(new VCardTelephone(child));
	    }
	}
	return telephones;
    }

    public String getTitle() {
	return getValue(Data.TITLE);
    }

    public String getURL() {
	return getValue(Data.URL);
    }

    public String getValue(Data data) {
	return getValue(data.toString());
    }

    private IPacket getName() {
	if (this.nameChild == null)
	    this.nameChild = packet.getFirstChild(byName("N"));
	return nameChild;
    }

}
