package com.calclab.emite.xep.search.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.MatcherFactory;
import com.calclab.emite.core.client.packet.PacketMatcher;
import com.calclab.emite.core.client.xmpp.session.ResultListener;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.session.Session.State;
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
    private XmppURI host;

    public SearchManagerImpl(final Session session) {
	this.session = session;
	this.filterQuery = MatcherFactory.byNameAndXMLNS("query", IQ_SEARCH);
    }

    @Override
    public void requestSearchFields(final ResultListener<SearchFields> listener) {
	if (session.getState() == State.ready) {
	    final XmppURI from = session.getCurrentUser();
	    final IQ iq = new IQ(Type.get, host).From(from).With(XML_LANG, "en");
	    iq.addQuery(IQ_SEARCH);

	    session.sendIQ(SEARCH_CATEGORY, iq, new Listener<IPacket>() {
		public void onEvent(final IPacket received) {
		    final IQ response = new IQ(received);
		    if (IQ.isSuccess(response)) {
			listener.onSuccess(processFieldsResults(from, response.getFirstChild(filterQuery)));
		    } else {
			// TODO
			listener.onFailure(null);
		    }
		}
	    });
	} else {
	    throw new RuntimeException("You should be connected before use this service.");
	}
    }

    @Override
    public void search(HashMap<String, String> query, ResultListener<List<Item>> listener) {
	if (session.getState() == State.ready) {
	    search(session.getCurrentUser(), host, query, listener);
	} else {
	    throw new RuntimeException("You should be connected before use this service.");
	}
    }

    @Override
    public void setHost(XmppURI host) {
	this.host = host;
    }

    private SearchFields processFieldsResults(final XmppURI from, final IPacket query) {
	SearchFields fields = new SearchFields();
	for (final IPacket child : query.getChildren()) {
	    if (!child.getName().equals("instructions")) {
		fields.add(child.getName());
	    } else {
		fields.setInstructions(child.getText());
	    }
	}
	return fields;
    }

    private void search(final XmppURI from, final XmppURI to, final HashMap<String, String> query,
	    final ResultListener<List<Item>> listener) {
	final IQ iq = new IQ(Type.set, to).From(from).With(XML_LANG, "en");
	final IPacket queryPacket = iq.addQuery(IQ_SEARCH);
	for (final String field : query.keySet()) {
	    queryPacket.addChild(field, null).setText(query.get(field));
	}

	session.sendIQ(SEARCH_CATEGORY, iq, new Listener<IPacket>() {
	    public void onEvent(final IPacket received) {
		final IQ response = new IQ(received);
		if (IQ.isSuccess(response)) {
		    listener.onSuccess(processResults(from, response.getFirstChild(filterQuery)));
		} else {
		    // TODO
		    listener.onFailure(null);
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
}
