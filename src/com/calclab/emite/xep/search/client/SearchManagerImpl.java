package com.calclab.emite.xep.search.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.MatcherFactory;
import com.calclab.emite.core.client.packet.PacketMatcher;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.core.client.xmpp.stanzas.IQ.Type;
import com.calclab.suco.client.events.Listener;

public class SearchManagerImpl implements SearchManager {
    private static final String XML_LANG = "xml:lang";
    private static final String SEARCH_CATEGORY = "search";
    private static final String IQ_SEARCH = "jabber:iq:search";
    private final Session session;
    private final PacketMatcher filterQuery;

    public SearchManagerImpl(final Session session) {
        this.session = session;
        this.filterQuery = MatcherFactory.byNameAndXMLNS("query", IQ_SEARCH);
    }

    public void requestSearchFields(final XmppURI from, final XmppURI to, final Listener<List<String>> onResult) {
        final IQ iq = new IQ(Type.get, to);
        iq.setFrom(from);
        iq.setAttribute(XML_LANG, "en");
        iq.addQuery(IQ_SEARCH);
        session.sendIQ(SEARCH_CATEGORY, iq, new Listener<IPacket>() {
            public void onEvent(final IPacket received) {
                final IQ result = new IQ(received);
                if (IQ.isSuccess(result)) {
                    onResult.onEvent(processFieldsResults(from, result.getFirstChild(filterQuery)));
                }
            }
        });
    }

    public void search(final XmppURI from, final XmppURI to, final HashMap<String, String> query,
            final Listener<List<Item>> onResult) {
        final IQ iq = new IQ(Type.set, to);
        iq.setFrom(from);
        iq.setAttribute(XML_LANG, "en");
        final IPacket queryPacket = iq.addQuery(IQ_SEARCH);
        for (final String field : query.keySet()) {
            queryPacket.addChild(field, null).setText(query.get(field));
        }
        session.sendIQ(SEARCH_CATEGORY, iq, new Listener<IPacket>() {
            public void onEvent(final IPacket received) {
                final IQ result = new IQ(received);
                if (IQ.isSuccess(result)) {
                    onResult.onEvent(processResults(from, result.getFirstChild(filterQuery)));
                }
            }
        });
    }

    protected List<Item> processResults(final XmppURI from, final IPacket query) {
        final List<Item> result = new ArrayList<Item>();
        for (final IPacket child : query.getChildren()) {
            if (child.getName().equals("item")) {
                final Item item = Item.parse(child);
                result.add(item);
            }
        }
        return result;
    }

    private List<String> processFieldsResults(final XmppURI from, final IPacket query) {
        final List<String> searchFields = new ArrayList<String>();
        for (final IPacket child : query.getChildren()) {
            if (!child.getName().equals("instructions")) {
                searchFields.add(child.getName());
            }
        }
        return searchFields;
    }
}
