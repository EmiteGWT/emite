package com.calclab.emite.xfunctional.client.tests;

import java.util.HashMap;
import java.util.List;

import com.calclab.emite.browser.client.PageAssist;
import com.calclab.emite.core.client.xmpp.session.ResultListener;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xep.dataforms.client.Field;
import com.calclab.emite.xep.dataforms.client.Form;
import com.calclab.emite.xep.dataforms.client.Item;
import com.calclab.emite.xep.dataforms.client.Form.Type;
import com.calclab.emite.xep.disco.client.DiscoveryManager;
import com.calclab.emite.xep.search.client.SearchFields;
import com.calclab.emite.xep.search.client.SearchManager;
import com.calclab.emite.xep.search.client.SearchResultItem;
import com.calclab.emite.xfunctional.client.Context;
import com.calclab.emite.xfunctional.client.FunctionalTest;
import com.calclab.suco.client.Suco;

public class SearchTests extends BasicTestSuite {

    FunctionalTest requestFields = new FunctionalTest() {
        @Override
        public void run(final Context ctx) {
            ctx.info("Requesting fields...");

            search.requestSearchFields(new ResultListener<SearchFields>() {
                @Override
                public void onFailure(final String message) {
                    ctx.success("Search fields retrieved");
                    ctx.info("No fields retrieved");
                    session.logout();
                }

                @Override
                public void onSuccess(final SearchFields fields) {
                    ctx.success("Search fields retrieved");
                    for (final String name : fields.getFieldNames()) {
                        ctx.info("Field retrieved: " + name);
                    }
                    session.logout();
                }
            });
        }

    };

    FunctionalTest requestFormFields = new FunctionalTest() {
        @Override
        public void run(final Context ctx) {
            ctx.info("Requesting form fields...");

            search.requestSearchForm(new ResultListener<Form>() {
                @Override
                public void onFailure(final String message) {
                    ctx.fail("No fields retrieved");
                    ctx.info("No fields retrieved");
                    session.logout();
                }

                @Override
                public void onSuccess(final Form form) {
                    final Form queryForm = new Form(Type.submit);
                    for (final Field field : form.getFields()) {
                        ctx.info("Field retrieved: " + field.getVar());
                        // We can search using the same fields
                        if (field.getVar().equals("search")) {
                            queryForm.addField(field.Value("*test*"));
                        } else {
                            queryForm.addField(field);
                        }

                    }
                    // Other form it to add add new fields:
                    // queryForm.addField(new
                    // Field(FieldType.HIDDEN).Var("FORM_TYPE").Value(SearchManagerImpl.IQ_SEARCH));
                    // queryForm.addField(new
                    // Field().Var("search").Value("*test*"));
                    // queryForm.addField(new
                    // Field().Var("Username").Value("1"));
                    search.search(queryForm, new ResultListener<Form>() {
                        @Override
                        public void onFailure(final String message) {
                            ctx.fail("No search retrieved");
                            ctx.info("No search retrieved");
                            session.logout();
                        }

                        @Override
                        public void onSuccess(final Form response) {
                            ctx.info("Search result retrieved");
                            for (final Item item : response.getItems()) {
                                for (final Field field : item.getFields()) {
                                    ctx.info("Item retrieved '" + field.getVar() + "' value: " + field.getValues());
                                }
                            }
                            ctx.success("Search result retrieved");
                            session.logout();
                        }
                    });

                }
            });
        }

    };

    FunctionalTest performSearch = new FunctionalTest() {
        @Override
        public void run(final Context ctx) {
            ctx.info("Performing search...");

            final HashMap<String, String> query = new HashMap<String, String>();
            query.put("nick", "test*");

            search.search(query, new ResultListener<List<SearchResultItem>>() {

                @Override
                public void onFailure(final String message) {
                    ctx.fail("Search failed");
                    session.logout();

                }

                @Override
                public void onSuccess(final List<SearchResultItem> searchResultItems) {
                    ctx.success("Search result retrieved");
                    session.logout();
                    for (final SearchResultItem searchResultItem : searchResultItems) {
                        ctx.info("Result: " + searchResultItem.getNick());
                    }

                }
            });

        }
    };

    private final Session session;
    private final SearchManager search;

    public SearchTests() {
        session = Suco.get(Session.class);
        search = Suco.get(SearchManager.class);
    }

    @Override
    public void beforeLogin(final Context ctx) {
        final DiscoveryManager discoveryManager = Suco.get(DiscoveryManager.class);
        discoveryManager.setActive(false);

        final String searchHost = PageAssist.getMeta("emite.searchHost");
        ctx.info("Using " + searchHost + " as search host. Configured at emite.searchHost meta parameter in html page");
        search.setHost(XmppURI.uri(searchHost));
    }

    @Override
    public void registerTests() {
        add("Request search fields", requestFields);
        add("Using forms", requestFormFields);
        add("Perform search", performSearch);
    }

}
