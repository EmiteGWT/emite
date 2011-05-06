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

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.Packet;

public class VCardEmail extends VCardData {

	public static enum Type {
		INTERNET, x400
	}

	private static final String PREF = "PREF";
	private static final String USERID = "USERID";

	public VCardEmail(final IPacket packet) {
		super(packet);
	}

	public VCardEmail(final String email, final boolean preferred) {
		this(new Packet(VCard.EMAIL).addChild(new Packet(USERID)));
		setType(Type.INTERNET);
		setUserId(email);
		setPreferred(preferred);
	}

	public String getUserId() {
		return getValue(USERID);
	}

	public boolean isPreferred() {
		return hasChild(PREF);
	}

	public boolean isType(final Type type) {
		return hasChild(type.toString());
	}

	public void setPreferred(final boolean preferred) {
		if (preferred && !isPreferred()) {
			addChild(PREF);
		} else if (!preferred && isPreferred()) {
			removeChild(getFirstChild(PREF));
		}
	}

	public void setType(final Type type) {
		if (!hasChild(type.toString())) {
			addChild(type.toString());
		}
	}

	public void setUserId(final String email) {
		setValue(USERID, email);
	}

}
