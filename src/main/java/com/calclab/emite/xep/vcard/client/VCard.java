/*
 * ((e)) emite: A pure Google Web Toolkit XMPP library
 * Copyright (c) 2008-2011 The Emite development team
 * 
 * This file is part of Emite.
 *
 * Emite is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * Emite is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with Emite.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.calclab.emite.xep.vcard.client;

import static com.calclab.emite.core.client.packet.MatcherFactory.byName;

import java.util.ArrayList;
import java.util.List;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.NoPacket;
import com.calclab.emite.core.client.packet.Packet;

/**
 * VCard java representation
 */
public class VCard extends VCardData {

	public static enum Data {
		FN, NICKNAME, URL, BDAY, TITLE, ROLE, JABBERID, DESC
	}

	public static enum Name {
		FAMILY, GIVEN, MIDDLE
	}

	public static final String VCARD = "vCard";
	public static final String DATA_XMLS = "vcard-temp";
	public static final String ORG = "ORG";
	public static final String ADR = "ADR";
	public static final String TEL = "TEL";
	public static final String EMAIL = "EMAIL";
	private static final String N = "N";

	private IPacket nameChild;
	private List<VCardTelephone> telephones;
	private List<VCardAddress> addresses;
	private List<VCardEmail> emails;

	public VCard() {
		super(new Packet(VCARD, DATA_XMLS));
		createRequiredFields();
	}

	public VCard(final IPacket packet) {
		super(packet);
		createRequiredFields();
	}

	public void addAddresses(final VCardAddress address) {
		addresses.add(address);
		addChild(address);
	}

	public void addEmail(final VCardEmail email) {
		emails.add(email);
		addChild(email);
	}

	public void addTelephone(final VCardTelephone telefone) {
		telephones.add(telefone);
		addChild(telefone);
	}

	public List<VCardAddress> getAddresses() {
		return addresses;
	}

	public String getDescription() {
		return getValue(Data.DESC);
	}

	public String getDisplayName() {
		return getValue(Data.FN);
	}

	public List<VCardEmail> getEmails() {
		return emails;
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

	public String getName(final Name name) {
		return getN().getFirstChild(byName(name.toString())).getText();
	}

	public String getNickName() {
		return getValue(Data.NICKNAME);
	}

	public VCardOrganization getOrganization() {
		return new VCardOrganization(getFirstChild(byName(ORG)));
	}

	public List<VCardTelephone> getTelephones() {
		return telephones;
	}

	public String getTitle() {
		return getValue(Data.TITLE);
	}

	public String getURL() {
		return getValue(Data.URL);
	}

	public void getURL(final String text) {
		setValue(Data.URL, text);
	}

	public String getValue(final Data data) {
		return getValue(data.toString());
	}

	public void setDescription(final String text) {
		setValue(Data.DESC.toString(), text);
	}

	public void setDisplayName(final String text) {
		setValue(Data.FN, text);
	}

	public void setFamilyName(final String text) {
		setName(Name.FAMILY, text);
	}

	public void setGivenName(final String text) {
		setName(Name.GIVEN, text);
	}

	public void setJabberID(final String text) {
		setValue(Data.JABBERID, text);
	}

	public void setMiddleName(final String text) {
		setName(Name.MIDDLE, text);
	}

	public void setName(final Name subname, final String text) {
		if (getN() == NoPacket.INSTANCE) {
			nameChild = addChild(N);
		}
		nameChild.setTextToChild(subname.toString(), text);
	}

	public void setNickName(final String text) {
		setValue(Data.NICKNAME, text);
	}

	public void setOrganization(final VCardOrganization orga) {
		removeChild(getOrganization());
		addChild(orga);
	}

	public void setTitle(final String text) {
		setValue(Data.TITLE, text);
	}

	public void setURL(final String url) {
		setValue(Data.URL, url);
	}

	public void setValue(final Data data, final String text) {
		setValue(data.toString(), text);
	}

	/**
	 * Currently there is no method for partial updates of a vCard, and the
	 * entire vCard must be sent to the server in order to update any part of
	 * the vCard.
	 */
	private void createRequiredFields() {
		for (final Data data : Data.values()) {
			getOrCreateChild(data.toString());
		}
		parseAddresses();
		parseEmails();
		parseTelephones();
	}

	private IPacket getN() {
		if (nameChild == null) {
			nameChild = getFirstChild(byName(N));
		}
		return nameChild;
	}

	private void parseAddresses() {
		addresses = new ArrayList<VCardAddress>();
		final List<? extends IPacket> children = getChildren(byName(ADR));
		for (final IPacket child : children) {
			addresses.add(new VCardAddress(child));
		}
	}

	private void parseEmails() {
		emails = new ArrayList<VCardEmail>();
		final List<? extends IPacket> children = getChildren(byName(EMAIL));
		for (final IPacket child : children) {
			emails.add(new VCardEmail(child));
		}
	}

	private void parseTelephones() {
		telephones = new ArrayList<VCardTelephone>();
		final List<? extends IPacket> children = getChildren(byName(TEL));
		for (final IPacket child : children) {
			telephones.add(new VCardTelephone(child));
		}
	}
}
