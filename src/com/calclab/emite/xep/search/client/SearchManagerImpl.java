package com.calclab.emite.xep.search.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.MatcherFactory;
import com.calclab.emite.core.client.packet.PacketMatcher;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.session.Session.State;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.core.client.xmpp.stanzas.IQ.Type;
import com.calclab.emite.xep.search.client.SearchResult.Status;
import com.calclab.suco.client.events.Listener;

public class SearchManagerImpl implements SearchManager {
    private static final String XML_LANG = "xml:lang";
    private static final String SEARCH_CATEGORY = "search";
    private static final String IQ_SEARCH = "jabber:iq:search";
    private final Session session;
    private final PacketMatcher filterQuery;
    private XmppURI host;

    public SearchManagerImpl(final Session session) {
	this.session = session;
	this.filterQuery = MatcherFactory.byNameAndXMLNS("query", IQ_SEARCH);
    }

    @Override
    public void requestSearchFields(final Listener<SearchResult<List<String>>> listener) {
	if (session.getState() == State.ready) {
	    final XmppURI from = session.getCurrentUser();
	    final IQ iq = new IQ(Type.get, host);
	    iq.setFrom(from);
	    iq.setAttribute(XML_LANG, "en");
	    iq.addQuery(IQ_SEARCH);
	    session.sendIQ(SEARCH_CATEGORY, iq, new Listener<IPacket>() {
		public void onEvent(final IPacket received) {
		    final IQ response = new IQ(received);
		    SearchResult<List<String>> result = new SearchResult<List<String>>();
		    if (IQ.isSuccess(response)) {
			result.status = Status.success;
			result.data = processFieldsResults(from, response.getFirstChild(filterQuery));
		    } else {
			result.status = Status.fail;
		    }
		    listener.onEvent(result);
		}
	    });
	} else {
	    throw new RuntimeException("You should be connected before use this service.");
	}
    }

    public void search(final XmppURI from, final XmppURI to, final HashMap<String, String> query,
	    final Listener<SearchResult<List<Item>>> onResult) {
	final IQ iq = new IQ(Type.set, to);
	iq.setFrom(from);
	iq.setAttribute(XML_LANG, "en");
	final IPacket queryPacket = iq.addQuery(IQ_SEARCH);
	for (final String field : query.keySet()) {
	    queryPacket.addChild(field, null).setText(query.get(field));
	}
	session.sendIQ(SEARCH_CATEGORY, iq, new Listener<IPacket>() {
	    public void onEvent(final IPacket received) {
		final IQ response = new IQ(received);
		SearchResult<List<Item>> result = new SearchResult<List<Item>>();
		if (IQ.isSuccess(response)) {
		    result.status = Status.success;
		    result.data = processResults(from, response.getFirstChild(filterQuery));
		} else {
		    result.status = Status.fail;
		}
		onResult.onEvent(result);
	    }
	});
    }

    @Override
    public void setHost(XmppURI host) {
	this.host = host;
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
}
