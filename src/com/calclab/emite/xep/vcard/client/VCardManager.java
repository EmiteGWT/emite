package com.calclab.emite.xep.vcard.client;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xep.vcard.client.events.VCardResponseHandler;
import com.calclab.suco.client.events.Listener;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * The manager to VCards. Implements http://xmpp.org/extensions/xep-0054.html
 * 
 */
public interface VCardManager {

    /**
     * Use addVCardResponseHandler
     * 
     * @param listener
     */
    @Deprecated
    void addOnVCardReceived(Listener<VCardResponse> listener);

    /**
     * Adds a handler to know when a VCardResponse is received
     * 
     * @param handler
     * @return
     */
    HandlerRegistration addVCardResponseHandler(VCardResponseHandler handler);

    /**
     * Use the other getUserVCard
     * 
     * @param userJid
     * @param listener
     */
    @Deprecated
    void getUserVCard(XmppURI userJid, Listener<VCardResponse> listener);

    /**
     * Gets the VCard of the given user JID
     * 
     * @param userJid
     * @param listener
     */
    void getUserVCard(XmppURI userJid, VCardResponseHandler handler);

    /**
     * Use the other requestOwnVCard
     * 
     * @param listener
     */
    @Deprecated
    void requestOwnVCard(Listener<VCardResponse> listener);

    /**
     * Gets the current logged in user's vcard
     * 
     * @param handler
     */
    void requestOwnVCard(VCardResponseHandler handler);

    /**
     * Use the other updateOwnVCard
     * 
     * @param vcard
     * @param listener
     */
    @Deprecated
    void updateOwnVCard(VCard vcard, Listener<VCardResponse> listener);

    /**
     * Updates the current logged in user's vcard
     * 
     * @param vcard
     * @param handler
     */
    void updateOwnVCard(VCard vcard, VCardResponseHandler handler);
}
