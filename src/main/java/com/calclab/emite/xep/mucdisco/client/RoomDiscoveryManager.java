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

package com.calclab.emite.xep.mucdisco.client;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xep.disco.client.FeatureSupportedCallback;

/**
 * Implements some discovery use cases from
 * http://xmpp.org/extensions/xep-0045.html
 */
public interface RoomDiscoveryManager {

	/**
	 * Discover the room items of the given entity
	 * 
	 * @param targetUri
	 *            the uri of the entity
	 * @param callback
	 *            a callback with the room items
	 * 
	 * @see ExistingRoomsCallback
	 * @see http://xmpp.org/extensions/xep-0045.html#disco-rooms
	 */
	void discoverRooms(XmppURI targetUri, ExistingRoomsCallback callback);

	/**
	 * Discover if the given entity support muc
	 * 
	 * @param targetUri
	 *            the uri of the entity
	 * @param callback
	 *            a callback to know if the feature is supported
	 * 
	 * @see FeatureSupportedCallback
	 * @see http://xmpp.org/extensions/xep-0045.html#disco-component
	 */
	void isMucSupported(XmppURI targetUri, FeatureSupportedCallback callback);

}
