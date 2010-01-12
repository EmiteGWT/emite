package com.calclab.emite.xep.privacylists.client;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xtesting.SessionTester;

public class PrivacyListManagerTests {

    private SessionTester session;
    private PrivacyListsManager manager;

    @Before
    public void beforeTests() {
	session = new SessionTester();
	manager = new PrivacyListsManager(session);
    }

    @Test
    public void shouldBlockUserBasedOnJID() {
	manager.blockUserBasedOnJID("myList", XmppURI.uri("name@domain/resource"), 7);
	session.verifyIQSent("<iq type='set'><query xmlns='jabber:iq:privacy'><list name='myList'>"
		+ "<item type='jid' value='name@domain' action='deny' order='7'></item></list></query></iq>");
    }
}
