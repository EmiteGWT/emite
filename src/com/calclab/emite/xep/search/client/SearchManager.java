package com.calclab.emite.xep.search.client;

import java.util.HashMap;
import java.util.List;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.events.Listener;

public interface SearchManager {

    void reqSearchFields(final XmppURI from, final XmppURI to, Listener<List<String>> onResult);

    void search(final XmppURI from, final XmppURI to, HashMap<String, String> query, Listener<List<Item>> onResult);

}