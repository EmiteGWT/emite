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

package com.calclab.emite.xep.disco;

import com.calclab.emite.core.XmppURI;

/**
 * The DiscoveryManager service (implements XEP-0030)
 * 
 * @see http://xmpp.org/extensions/xep-0030.html
 */
public interface DiscoveryManager {
	
	/**
	 * Sends a info query to the specified target uri. The handler (if any) will
	 * be called when a info result arrives. Notice that a
	 * DiscoveryInfoResultEvent can be a NOT successful response
	 * 
	 * @param targetUri
	 *            the target uri (required)
	 * @param handler
	 *            the handler (can be null)
	 * @see http://xmpp.org/extensions/xep-0030.html#info
	 */
	void sendInfoQuery(XmppURI targetUri, DiscoveryInfoCallback handler);

	/**
	 * Send a items query to the specified target uri. The handler (if any) wil
	 * be called when a items result arrives. Notice that a
	 * DiscoveryItemsResultEvent can be a NOT successful response
	 * 
	 * @param targetUri
	 *            the target uri (required)
	 * @param handler
	 *            the handler (can be null)
	 * @see http://xmpp.org/extensions/xep-0030.html#items
	 */
	void sendItemsQuery(XmppURI targetUri, DiscoveryItemsCallback handler);

}
