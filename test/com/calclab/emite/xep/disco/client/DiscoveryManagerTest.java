package com.calclab.emite.xep.disco.client;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xep.disco.client.DiscoveryManager.DiscoveryManagerResponse;
import com.calclab.emite.xtesting.SessionTester;
import com.calclab.suco.testing.events.MockedListener;

public class DiscoveryManagerTest {

    private DiscoveryManager manager;
    private SessionTester session;

    private static final String DISCO_RESULT = "<iq type='result'" +
        "	    from='plays.shakespeare.lit'" +
        "		    to='romeo@montague.net/orchard'" +
        "		    id='info1'>" +
        "		  <query xmlns='http://jabber.org/protocol/disco#info'>" +
        "		    <identity" +
        "		        category='conference'" +
        "		        type='text'" +
        "		        name='Play-Specific Chatrooms'/>" +
        "		    <identity" +
        "		        category='directory'" +
        "		        type='chatroom'" +
        "		        name='Play-Specific Chatrooms'/>" +
        "		    <feature var='http://jabber.org/protocol/disco#info'/>" +
        "		    <feature var='http://jabber.org/protocol/disco#items'/>" +
        "		    <feature var='http://jabber.org/protocol/muc'/>" +
        "		    <feature var='jabber:iq:register'/>" +
        "		    <feature var='jabber:iq:search'/>" +
        "		    <feature var='jabber:iq:time'/>" +
        "		    <feature var='jabber:iq:version'/>" +
        "		  </query>" +
        "		</iq>";

    private static final String DISCO_ITEMS_RESULT ="<iq type='result'" +
        "    from='shakespeare.lit'" +
        "    to='romeo@montague.net/orchard'" +
        "    id='items1'>" +
        "  <query xmlns='http://jabber.org/protocol/disco#items'>" +
        "    <item jid='people.shakespeare.lit'" +
        "          name='Directory of Characters'/>" +
        "    <item jid='plays.shakespeare.lit'" +
        "          name='Play-Specific Chatrooms'/>" +
        "    <item jid='mim.shakespeare.lit'" +
        "          name='Gateway to Marlowe IM'/>" +
        "    <item jid='words.shakespeare.lit'" +
        "          name='Shakespearean Lexicon'/>" +
        "    <item jid='globe.shakespeare.lit'" +
        "          name='Calendar of Performances'/>" +
        "    <item jid='headlines.shakespeare.lit'" +
        "          name='Latest Shakespearean News'/>" +
        "    <item jid='catalog.shakespeare.lit'" +
        "          name='Buy Shakespeare Stuff!'/>" +
        "    <item jid='en2fr.shakespeare.lit'" +
        "          name='French Translation Service'/>" +
        "  </query>" +
        "</iq>";

    @Before
    public void beforeTests() {
	session = new SessionTester();
	manager = new DiscoveryManager(session);
    }

    @Test
    public void shouldInformListeners() {
	final MockedListener<DiscoveryManagerResponse> listener = new MockedListener<DiscoveryManagerResponse>();
	XmppURI uri = XmppURI.uri("node", "localhost.localdomain", "resource");
	manager.sendDiscoQuery(uri, null, listener);
	session.answer(DISCO_RESULT);
	DiscoveryManagerResponse response = listener.getValue(0);
	assertEquals(2, response.getIdentities().size());
	assertEquals(7, response.getFeatures().size());
	assertNull(response.getItems());
	manager.sendDiscoItemsQuery(uri, null, listener);
	session.answer(DISCO_ITEMS_RESULT);
	response = listener.getValue(1);
	assertEquals(8, response.getItems().size());
	assertNull(response.getIdentities());
	assertNull(response.getFeatures());
    }
}
