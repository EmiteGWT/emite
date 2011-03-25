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
