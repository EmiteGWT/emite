package com.calclab.emite.xep.search.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.xmpp.session.ResultListener;
import com.calclab.emite.core.client.xmpp.session.Session.State;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xtesting.SessionTester;

public class SearchManagerTest {

    private SessionTester session;
    private SearchManager manager;

    @Before
    public void setUp() {
	session = new SessionTester();
	manager = new SearchManagerImpl(session);
	manager.setHost(XmppURI.uri("search.service"));
	session.setLoggedIn(XmppURI.uri("romeo@montague.net/home"));
	session.setState(State.ready);
    }

    /**
     * @see http://xmpp.org/extensions/xep-0055.html#usecases-search
     */
    @Test
    public void shouldRequestAndReceiveSearchFields() {
	manager.requestSearchFields(new ResultListener<SearchFields>() {
	    @Override
	    public void onFailure(String message) {
	    }

	    @Override
	    public void onSuccess(SearchFields response) {
		List<String> fields = response.getFieldNames();
		assertTrue(fields.contains("first"));
		assertTrue(fields.contains("last"));
		assertTrue(fields.contains("nick"));
		assertTrue(fields.contains("email"));
		assertFalse(fields.contains("instructions"));
	    }
	});
	session.verifyIQSent("<iq type='get' from='romeo@montague.net/home' to='search.service'"
		+ "xml:lang='en'> <query xmlns='jabber:iq:search'/> </iq>");
	session
		.answer("<iq type='result' from='characters.shakespeare.lit' to='romeo@montague.net/home' id='search1' xml:lang='en'>"
			+ " <query xmlns='jabber:iq:search'> <instructions>"
			+ " Fill in one or more fields to search for any matching Jabber users."
			+ " </instructions> <first/> <last/> <nick/> <email/>" + " </query></iq>");
    }

    @Test
    public void shouldReturnAnEmptyListIfNotResultFounded() {

	manager.search(new HashMap<String, String>(), new ResultListener<List<SearchResultItem>>() {
	    @Override
	    public void onFailure(String message) {
	    }

	    @Override
	    public void onSuccess(List<SearchResultItem> list) {
		assertTrue(list.isEmpty());
	    }
	});

	session
		.answer("<iq type='result' from='characters.shakespeare.lit' to='romeo@montague.net/home' id='search2' xml:lang='en'>"
			+ "<query xmlns='jabber:iq:search'/></iq>");
    }

    @Test
    public void testSearch() {
	final HashMap<String, String> query = new HashMap<String, String>();
	query.put("last", "Capulet");

	manager.search(query, new ResultListener<List<SearchResultItem>>() {
	    @Override
	    public void onFailure(String message) {
	    }

	    @Override
	    public void onSuccess(List<SearchResultItem> list) {
		final SearchResultItem searchResultItem1 = list.get(0);
		final SearchResultItem searchResultItem2 = list.get(1);
		assertEquals(2, list.size());
		assertEquals("Juliet", searchResultItem1.getFirst());
		assertEquals("Capulet", searchResultItem1.getLast());
		assertEquals("JuliC", searchResultItem1.getNick());
		assertEquals("juliet@shakespeare.lit", searchResultItem1.getEmail());
		assertEquals("juliet@capulet.com", searchResultItem1.getJid().toString());
		assertEquals("tybalt@shakespeare.lit", searchResultItem2.getJid().toString());
	    }
	});

	session.verifyIQSent("<iq type='set' from='romeo@montague.net/home' to='search.service' xml:lang='en'>"
		+ "<query xmlns='jabber:iq:search'> <last>Capulet</last> </query></iq>");
	session
		.answer("<iq type='result' from='characters.shakespeare.lit' to='romeo@montague.net/home' id='search2' xml:lang='en'>"
			+ "<query xmlns='jabber:iq:search'><item jid='juliet@capulet.com'>"
			+ "<first>Juliet</first><last>Capulet</last><nick>JuliC</nick>"
			+ "<email>juliet@shakespeare.lit</email></item>"
			+ "<item jid='tybalt@shakespeare.lit'><first>Tybalt</first>"
			+ "<last>Capulet</last><nick>ty</nick>"
			+ "<email>tybalt@shakespeare.lit</email></item></query></iq>");
    }

}
