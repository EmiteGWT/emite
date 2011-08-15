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

package com.calclab.emite.xep.avatar.client;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;

public class AvatarVCard {

	final private XmppURI from;
	private String photoHash;
	private String photo;
	private String photoType;

	public AvatarVCard(final XmppURI from) {
		this(from, null, null, null);
	}

	public AvatarVCard(final XmppURI from, final String photoHash, final String photo, final String photoType) {
		this.from = from;
		this.photoHash = photoHash;
		this.photo = photo;
		this.photoType = photoType;
	}

	public XmppURI getFrom() {
		return from;
	}

	public String getPhotoHash() {
		return photoHash;
	}

	public void setPhotoHash(final String photoHash) {
		this.photoHash = photoHash;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(final String photo) {
		this.photo = photo;
	}

	public String getPhotoType() {
		return photoType;
	}

	public void setPhotoType(final String photoType) {
		this.photoType = photoType;
	}

}
