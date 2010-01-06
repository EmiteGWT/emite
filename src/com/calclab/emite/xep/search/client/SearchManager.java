package com.calclab.emite.xep.search.client;

import java.util.HashMap;
import java.util.List;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.events.Listener;

/**
 * Search information repositories on the Jabber network.
 *
 * Implements XEP-0055: Jabber Search
 * 
 * @see http://xmpp.org/extensions/xep-0055.html
 */
public interface SearchManager {

    /**
     * Request search fields
     * TODO
     * 
     * @param from
     * @param to
     * @param onResult
     */
    void requestSearchFields(final XmppURI from, final XmppURI to, Listener<List<String>> onResult);

    /**
     * Perform search
     * TODO
     * 
     * @param from
     * @param to
     * @param query
     * @param onResult
     */
    void search(final XmppURI from, final XmppURI to, HashMap<String, String> query, Listener<List<Item>> onResult);

}