package com.calclab.emite.xep.search.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.MatcherFactory;
import com.calclab.emite.core.client.packet.NoPacket;
import com.calclab.emite.core.client.packet.Packet;
import com.calclab.emite.core.client.packet.PacketMatcher;
import com.calclab.emite.core.client.xmpp.session.ResultListener;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.session.Session.State;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.core.client.xmpp.stanzas.IQ.Type;
import com.calclab.emite.xep.dataforms.client.Field;
import com.calclab.emite.xep.dataforms.client.FieldType;
import com.calclab.emite.xep.dataforms.client.Form;
import com.calclab.suco.client.events.Listener;

public class SearchManagerImpl implements SearchManager {
    private static final String SHOULD_BE_CONNECTED = "You should be connected before use this service.";
    public static final String IQ_SEARCH = "jabber:iq:search";
    private static final String XML_LANG = "xml:lang";
    private static final String SEARCH_CATEGORY = "search";
    private final Session session;
    private final PacketMatcher filterQuery;
    private XmppURI host;

    public SearchManagerImpl(final Session session) {
        this.session = session;
        this.filterQuery = MatcherFactory.byNameAndXMLNS("query", IQ_SEARCH);
    }

    @Override
    public void requestSearchFields(final ResultListener<SearchFields> listener) {
        requestGenericSearchFields(new Listener<IPacket>() {
            @Override
            public void onEvent(final IPacket ipacket) {
                final IQ response = new IQ(ipacket);
                if (IQ.isSuccess(response)) {
                    listener.onSuccess(processFieldsResults(session.getCurrentUser(),
                            response.getFirstChild(filterQuery)));
                } else {
                    // TODO
                    listener.onFailure(null);
                }
            }
        });
    }

    @Override
    public void requestSearchForm(final ResultListener<Form> listener) {
        requestGenericSearchFields(new Listener<IPacket>() {
            @Override
            public void onEvent(final IPacket received) {
                final IQ response = new IQ(received);
                if (IQ.isSuccess(response)) {
                    Form form = new Form(received);
                    if (form.x().equals(NoPacket.INSTANCE)) {
                        // This is not a extended search. Try to create a form
                        // with returned fields
                        final SearchFields fieldResults = processFieldsResults(session.getCurrentUser(),
                                response.getFirstChild(filterQuery));
                        form = new Form(Form.Type.form);
                        form.addInstruction(fieldResults.getInstructions());
                        for (final String fieldName : fieldResults.getFieldNames()) {
                            form.addField(new Field(FieldType.TEXT_SINGLE).Var(fieldName));
                        }
                    }
                    listener.onSuccess(form);
                } else {
                    // TODO
                    listener.onFailure(null);
                }
            }
        });
    }

    @Override
    public void search(final Form searchForm, final ResultListener<Form> listener) {
        searchGeneric(Arrays.asList((IPacket) searchForm), new Listener<IPacket>() {
            @Override
            public void onEvent(final IPacket received) {
                final IQ response = new IQ(received);
                if (IQ.isSuccess(response)) {
                    listener.onSuccess(new Form(response));
                } else {
                    // TODO
                    listener.onFailure(null);
                }
            }
        });
    }

    @Override
    public void search(final HashMap<String, String> query, final ResultListener<List<SearchResultItem>> listener) {
        final List<IPacket> queryPacket = new ArrayList<IPacket>();
        for (final String field : query.keySet()) {
            final Packet child = new Packet(field);
            child.setText(query.get(field));
            queryPacket.add(child);
        }
        searchGeneric(queryPacket, new Listener<IPacket>() {
            @Override
            public void onEvent(final IPacket received) {
                final IQ response = new IQ(received);
                if (IQ.isSuccess(response)) {
                    listener.onSuccess(processResults(session.getCurrentUser(), response.getFirstChild(filterQuery)));
                } else {
                    // TODO
                    listener.onFailure(null);
                }
            }
        });
    }

    @Override
    public void setHost(final XmppURI host) {
        this.host = host;
    }

    protected List<SearchResultItem> processResults(final XmppURI from, final IPacket query) {
        final List<SearchResultItem> result = new ArrayList<SearchResultItem>();
        for (final IPacket child : query.getChildren()) {
            if (child.getName().equals("item")) {
                final SearchResultItem searchResultItem = SearchResultItem.parse(child);
                result.add(searchResultItem);
            }
        }
        return result;
    }

    private SearchFields processFieldsResults(final XmppURI from, final IPacket query) {
        final SearchFields fields = new SearchFields();
        for (final IPacket child : query.getChildren()) {
            if (!child.getName().equals("instructions")) {
                fields.add(child.getName());
            } else {
                fields.setInstructions(child.getText());
            }
        }
        return fields;
    }

    private void requestGenericSearchFields(final Listener<IPacket> onResult) {
        if (session.getState() == State.ready) {
            final XmppURI from = session.getCurrentUser();
            final IQ iq = new IQ(Type.get, host).From(from).With(XML_LANG, "en");
            iq.addQuery(IQ_SEARCH);

            session.sendIQ(SEARCH_CATEGORY, iq, new Listener<IPacket>() {
                public void onEvent(final IPacket received) {
                    onResult.onEvent(received);
                }
            });
        } else {
            throw new RuntimeException(SHOULD_BE_CONNECTED);
        }
    }

    private void searchGeneric(final List<IPacket> queryChilds, final Listener<IPacket> onResult) {
        if (session.getState() == State.ready) {
            final IQ iq = new IQ(Type.set, host).From(session.getCurrentUser()).With(XML_LANG, "en");
            final IPacket queryPacket = iq.addQuery(IQ_SEARCH);
            for (final IPacket child : queryChilds) {
                queryPacket.addChild(child);
            }
            session.sendIQ(SEARCH_CATEGORY, iq, new Listener<IPacket>() {
                public void onEvent(final IPacket received) {
                    onResult.onEvent(received);
                }
            });
        } else {
            throw new RuntimeException(SHOULD_BE_CONNECTED);
        }
    }
}
