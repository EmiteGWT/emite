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
