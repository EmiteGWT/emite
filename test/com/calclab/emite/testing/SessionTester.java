package com.calclab.emite.testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import com.calclab.emite.core.client.bosh.StreamSettings;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.xmpp.session.AbstractSession;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.core.client.xmpp.stanzas.IQ.Type;
import com.calclab.emite.testing.services.TigaseXMLService;
import com.calclab.suco.client.events.Listener;

/**
 * Object of this class are used to test against session (any emite component).
 * 
 * This sessions allow you to simulate reception of stanzas. It also allows to
 * ask (and query) about the stazas that had been send.
 * 
 */
public class SessionTester extends AbstractSession {
    private XmppURI currentUser;
    private final TigaseXMLService xmler;
    private final ArrayList<IPacket> sent;
    private IPacket lastIQSent;
    private Listener<IPacket> lastIQListener;

    public SessionTester() {
	this((XmppURI) null);
    }

    public SessionTester(final String user) {
	this(XmppURI.uri(user));
    }

    public SessionTester(final XmppURI user) {
	xmler = new TigaseXMLService();
	sent = new ArrayList<IPacket>();
	if (user != null) {
	    setLoggedIn(user);
	}
    }

    public void answer(final IPacket iq) {
	lastIQListener.onEvent(iq);
    }

    public void answer(final String iq) {
	answer(xmler.toXML(iq));
    }

    public void answerSuccess() {
	answer(new IQ(Type.result));
    }

    public XmppURI getCurrentUser() {
	return currentUser;
    }

    public boolean isLoggedIn() {
	return currentUser != null;
    }

    public void login(final XmppURI uri, final String password) {
	setLoggedIn(uri);
    }

    public void logout() {
	setState(Session.State.loggingOut);
	currentUser = null;
	setState(Session.State.disconnected);
    }

    public StreamSettings pause() {
	return null;
    }

    public void receives(final Message message) {
	fireMessage(message);
    }

    public void receives(final Presence presence) {
	firePresence(presence);
    }

    public void receives(final String received) {
	final IPacket stanza = xmler.toXML(received);
	final String name = stanza.getName();
	if (name.equals("message")) {
	    fireMessage(new Message(stanza));
	} else if (name.equals("presence")) {
	    firePresence(new Presence(stanza));
	} else if (name.equals("iq") && (stanza.hasAttribute("type", "set") || stanza.hasAttribute("type", "get"))) {
	    fireIQ(new IQ(stanza));
	} else {
	    throw new RuntimeException("Not valid received: " + received);
	}

    }

    public void resume(final XmppURI userURI, final StreamSettings settings) {
    }

    public void send(final IPacket packet) {
	sent.add(packet);
    }

    public void sendIQ(final String id, final IQ iq, final Listener<IPacket> listener) {
	this.lastIQSent = iq;
	this.lastIQListener = listener;
    }

    public void setCurrentUser(final XmppURI currentUser) {
	this.currentUser = currentUser;
    }

    public void setLoggedIn(final String uri) {
	setLoggedIn(XmppURI.uri(uri));
    }

    public void setLoggedIn(final XmppURI userURI) {
	this.currentUser = userURI;
	setState(State.loggedIn);
    }

    public void setReady() {
	setState(State.ready);
    }

    @Override
    public void setState(State state) {
	super.setState(state);
    }

    public Listener<IPacket> verifyIQSent(final IPacket iq) {
	assertNotNull(lastIQSent);
	EmiteAsserts.assertPacketLike(iq, lastIQSent);
	return lastIQListener;
    }

    public void verifyIQSent(final String xml) {
	verifyIQSent(xmler.toXML(xml));
    }

    public void verifyNotSent(final IPacket packet) {
	assertNotContains(packet, sent);
    }

    public void verifyNotSent(final String xml) {
	verifyNotSent(xmler.toXML(xml));
    }

    public void verifySent(final IPacket packet) {
	assertContains(packet, sent);
    }

    public void verifySent(final String expected) {
	final IPacket packet = xmler.toXML(expected);
	verifySent(packet);
    }

    public void verifySentNothing() {
	assertEquals("number of sent stanzas", 0, sent.size());
    }

    private void assertContains(final IPacket expected, final ArrayList<IPacket> list) {
	final StringBuffer buffer = new StringBuffer();
	final boolean isContained = contains(expected, list, buffer);
	assertTrue("Expected " + expected + " contained in " + buffer, isContained);
    }

    private void assertNotContains(final IPacket expected, final ArrayList<IPacket> list) {
	final StringBuffer buffer = new StringBuffer();
	final boolean isContained = contains(expected, list, buffer);
	assertFalse("Expected " + expected + " contained in\n" + buffer, isContained);
    }

    private boolean contains(final IPacket expected, final ArrayList<IPacket> list, final StringBuffer buffer) {
	boolean isContained = false;
	final IsPacketLike matcher = new IsPacketLike(expected);
	for (final IPacket packet : list) {
	    buffer.append("[").append(packet.toString()).append("]");
	    isContained = isContained ? isContained : matcher.matches(packet, System.out);
	}
	return isContained;
    }
}
