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

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xep.vcard.client.events.VCardResponseHandler;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * The manager to VCards. Implements http://xmpp.org/extensions/xep-0054.html
 * 
 */
public interface VCardManager {

	/**
	 * Adds a handler to know when a VCardResponse is received
	 * 
	 * @param handler
	 * @return
	 */
	HandlerRegistration addVCardResponseHandler(VCardResponseHandler handler);

	/**
	 * Gets the VCard of the given user JID
	 * 
	 * @param userJid
	 * @param listener
	 */
	void getUserVCard(XmppURI userJid, VCardResponseHandler handler);

	/**
	 * Gets the current logged in user's vcard
	 * 
	 * @param handler
	 */
	void requestOwnVCard(VCardResponseHandler handler);

	/**
	 * Updates the current logged in user's vcard
	 * 
	 * @param vcard
	 * @param handler
	 */
	void updateOwnVCard(VCard vcard, VCardResponseHandler handler);
}
