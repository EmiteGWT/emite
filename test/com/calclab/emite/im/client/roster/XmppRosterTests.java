package com.calclab.emite.im.client.roster;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.xmpp.session.XmppSession.SessionStates;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.core.client.xmpp.stanzas.IQ.Type;
import com.calclab.emite.xtesting.XmppSessionTester;
import com.calclab.emite.xtesting.handlers.RosterItemChangedTestHandler;

public class XmppRosterTests {

    private XmppSessionTester session;
    private XmppRosterLogic roster;
    private XmppURI me;

    @Before
    public void beforeTests() {
	me = XmppURI.uri("me@domain");
	session = new XmppSessionTester();
	session.setLoggedIn(me);
	roster = new XmppRosterLogic(session);
    }

    @Test
    public void shouldChangeItemPresenceAndFireEvent() {
	setRosterItems(item("other@domain"));
	RosterItemChangedTestHandler handler = new RosterItemChangedTestHandler();
	roster.addRosterItemChangedHandler(handler);
	session.receives("<presence from='other@domain'>"
		+ "<show>dnd</show><status>message</status><priority>3</priority></presence>");
	final RosterItem item = roster.getItemByJID(uri("other@domain"));
	assertEquals(Presence.Show.dnd, item.getShow());
	assertEquals("message", item.getStatus());
	assertTrue(handler.isCalledOnce());
    }

    @Test
    public void shouldChangeItemPresenceFromSeveralResources() {
	setRosterItems(item("other@domain"));
	session.receives("<presence from='other@domain/resource1'>"
		+ "<show>dnd</show><status>message</status><priority>3</priority></presence>");
	session.receives("<presence from='other@domain/resource2'>"
		+ "<show>away</show><status>message</status><priority>3</priority></presence>");
	assertTrue(roster.getItemByJID(uri("other@domain")).isAvailable());
	session.receives("<presence type='unavailable' from='other@domain/resource1' />");
	assertTrue(roster.getItemByJID(uri("other@domain")).isAvailable());
    }

    @Test
    public void shouldRetrieveRoster() {
	setRosterItems(item("one@domain"), item("two@domain"));
	assertEquals(2, roster.getItems().size());
    }

    private RosterItem item(String jid) {
	XmppURI uri = XmppURI.uri(jid);
	return new RosterItem(uri, SubscriptionState.both, uri.getNode(), null);
    }

    private void setRosterItems(RosterItem... items) {
	session.setSessionState(SessionStates.loggedIn);
	IQ iq = new IQ(Type.result);
	IPacket query = iq.addQuery("jabber:iq:roster");
	for (RosterItem item : items) {
	    item.addStanzaTo(query);
	}
	session.answer(iq);
    }
}
