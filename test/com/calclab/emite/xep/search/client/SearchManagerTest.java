package com.calclab.emite.xep.search.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.xmpp.session.Session.State;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.testing.MockedSession;
import com.calclab.suco.testing.events.MockedListener;

public class SearchManagerTest {

    private static final XmppURI HOST = XmppURI.uri("characters.shakespeare.lit");
    private MockedSession session;
    private SearchManager manager;

    @Before
    public void setUp() {
	session = new MockedSession();
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
	final MockedListener<SearchResult<List<String>>> listener = new MockedListener<SearchResult<List<String>>>();
	manager.requestSearchFields(listener);
	session.verifyIQSent("<iq type='get' from='romeo@montague.net/home' to='search.service'"
		+ "xml:lang='en'> <query xmlns='jabber:iq:search'/> </iq>");
	session
		.answer("<iq type='result' from='characters.shakespeare.lit' to='romeo@montague.net/home' id='search1' xml:lang='en'>"
			+ " <query xmlns='jabber:iq:search'> <instructions>"
			+ " Fill in one or more fields to search for any matching Jabber users."
			+ " </instructions> <first/> <last/> <nick/> <email/>" + " </query></iq>");
	assertTrue(listener.isCalledOnce());
	final List<String> fields = listener.getValue(0).data;
	assertTrue(fields.contains("first"));
	assertTrue(fields.contains("last"));
	assertTrue(fields.contains("nick"));
	assertTrue(fields.contains("email"));
	assertFalse(fields.contains("instructions"));
    }

    @Test
    public void shouldReturnAnEmptyListIfNotResultFounded() {
	final MockedListener<SearchResult<List<Item>>> listener = new MockedListener<SearchResult<List<Item>>>();
	manager.search(session.getCurrentUser(), HOST, new HashMap<String, String>(), listener);
	session
		.answer("<iq type='result' from='characters.shakespeare.lit' to='romeo@montague.net/home' id='search2' xml:lang='en'>"
			+ "<query xmlns='jabber:iq:search'/></iq>");
	assertTrue(listener.isCalledOnce());
	final List<Item> list = listener.getValue(0).data;
	assertTrue(list.isEmpty());
    }

    @Test
    public void testSearch() {
	final MockedListener<SearchResult<List<Item>>> listener = new MockedListener<SearchResult<List<Item>>>();
	final HashMap<String, String> query = new HashMap<String, String>();
	query.put("last", "Capulet");
	manager.search(session.getCurrentUser(), HOST, query, listener);
	session
		.verifyIQSent("<iq type='set' from='romeo@montague.net/home' to='characters.shakespeare.lit' xml:lang='en'>"
			+ "<query xmlns='jabber:iq:search'> <last>Capulet</last> </query></iq>");
	session
		.answer("<iq type='result' from='characters.shakespeare.lit' to='romeo@montague.net/home' id='search2' xml:lang='en'>"
			+ "<query xmlns='jabber:iq:search'><item jid='juliet@capulet.com'>"
			+ "<first>Juliet</first><last>Capulet</last><nick>JuliC</nick>"
			+ "<email>juliet@shakespeare.lit</email></item>"
			+ "<item jid='tybalt@shakespeare.lit'><first>Tybalt</first>"
			+ "<last>Capulet</last><nick>ty</nick>"
			+ "<email>tybalt@shakespeare.lit</email></item></query></iq>");
	assertTrue(listener.isCalledOnce());
	final List<Item> list = listener.getValue(0).data;
	final Item item1 = list.get(0);
	final Item item2 = list.get(1);
	assertEquals(2, list.size());
	assertEquals("Juliet", item1.getFirst());
	assertEquals("Capulet", item1.getLast());
	assertEquals("JuliC", item1.getNick());
	assertEquals("juliet@shakespeare.lit", item1.getEmail());
	assertEquals("juliet@capulet.com", item1.getJid().toString());
	assertEquals("tybalt@shakespeare.lit", item2.getJid().toString());
    }

}
