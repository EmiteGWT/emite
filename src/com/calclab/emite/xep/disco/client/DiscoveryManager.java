/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008-2009 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emite.xep.disco.client;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xep.disco.client.events.DiscoveryInfoResultHandler;
import com.calclab.emite.xep.disco.client.events.DiscoveryItemsResultHandler;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * The DiscoveryManager service (implements XEP-0030)
 * 
 * @see http://xmpp.org/extensions/xep-0030.html
 */
public interface DiscoveryManager {

    /**
     * Adds a handler to know when a discovery information result has arrived
     * from the server (remember: some responses are cached)
     * 
     * @param handler
     * @return
     */
    HandlerRegistration addDiscoveryInfoResultHandler(DiscoveryInfoResultHandler handler);

    /**
     * A method to know if a given features are supported on a given entity. The
     * callback receives true if ALL features are implemented
     * 
     * @param targetUri
     *            the uri of the entity to query
     * @param callback
     *            the callback
     * @param featureNames
     *            the desired feature names
     */
    void areFeaturesSupported(XmppURI targetUri, FeatureSupportedCallback callback, String... featureName);

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
    void sendInfoQuery(XmppURI targetUri, DiscoveryInfoResultHandler handler);

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
    void sendItemsQuery(XmppURI targetUri, DiscoveryItemsResultHandler handler);

}
