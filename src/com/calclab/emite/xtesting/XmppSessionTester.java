package com.calclab.emite.xtesting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import com.calclab.emite.core.client.bosh.StreamSettings;
import com.calclab.emite.core.client.events.IQReceivedEvent;
import com.calclab.emite.core.client.events.MessageReceivedEvent;
import com.calclab.emite.core.client.events.PresenceReceivedEvent;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.xmpp.session.Credentials;
import com.calclab.emite.core.client.xmpp.session.IQResponseHandler;
import com.calclab.emite.core.client.xmpp.session.XmppSessionBoilerPlate;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.core.client.xmpp.stanzas.IQ.Type;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xtesting.matchers.EmiteAsserts;
import com.calclab.emite.xtesting.matchers.IsPacketLike;
import com.calclab.emite.xtesting.services.TigaseXMLService;

public class XmppSessionTester extends XmppSessionBoilerPlate {

    private XmppURI currentUser;
    private final TigaseXMLService xmler;
    private final ArrayList<IPacket> sent;
    private IPacket lastIQSent;
    private IQResponseHandler lastIQResponseHandler;

    public XmppSessionTester() {
	this((XmppURI) null);
    }

    /**
     * Create a new SessionTester and login if user provided
     * 
     * @param user
     *            optional user to login
     */
    public XmppSessionTester(final String user) {
	this(XmppURI.uri(user));
    }

    /**
     * Create a new SessionTester and login if user provided
     * 
     * @param user
     *            optional user to login
     */
    public XmppSessionTester(final XmppURI user) {
	super(EmiteTestsEventBus.create("et"));
	xmler = new TigaseXMLService();
	sent = new ArrayList<IPacket>();
	if (user != null) {
	    setLoggedIn(user);
	}
    }

    public void answer(final IPacket iq) {
	lastIQResponseHandler.onIQ(new IQ(iq));
    }

    public void answer(final String iq) {
	answer(xmler.toXML(iq));
    }

    public void answerSuccess() {
	answer(new IQ(Type.result));
    }

    @Override
    public XmppURI getCurrentUserURI() {
	return currentUser;
    }

    @Override
    public boolean isLoggedIn() {
	return isReady();
    }

    @Override
    public boolean isReady() {
	return currentUser != null;
    }

    @Override
    public void login(final Credentials credentials) {
	setLoggedIn(credentials.getXmppUri());
    }

    @Override
    public void logout() {
	if (currentUser != null) {
	    setSessionState(SessionStates.loggingOut);
	    currentUser = null;
	    setSessionState(SessionStates.disconnected);
	}
    }

    @Override
    public StreamSettings pause() {
	return null;
    }

    public void receives(final Message message) {
	eventBus.fireEvent(new MessageReceivedEvent(message));
    }

    public void receives(final Presence presence) {
	eventBus.fireEvent(new PresenceReceivedEvent(presence));
    }

    public void receives(final String received) {
	final IPacket stanza = xmler.toXML(received);
	final String name = stanza.getName();
	if (name.equals("message")) {
	    eventBus.fireEvent(new MessageReceivedEvent(new Message(stanza)));
	} else if (name.equals("presence")) {
	    eventBus.fireEvent(new PresenceReceivedEvent(new Presence(stanza)));
	} else if (name.equals("iq")) {
	    eventBus.fireEvent(new IQReceivedEvent(new IQ(stanza)));
	} else {
	    throw new RuntimeException("WHAT IS THIS? (" + name + "): " + stanza.toString());
	}

    }

    @Override
    public void resume(final XmppURI userURI, final StreamSettings settings) {
    }

    @Override
    public void send(final IPacket packet) {
	sent.add(packet);
    }

    @Override
    public void sendIQ(final String category, final IQ iq, final IQResponseHandler iqHandler) {
	lastIQSent = iq;
	lastIQResponseHandler = iqHandler;
    }

    public void setCurrentUser(final XmppURI currentUser) {
	this.currentUser = currentUser;
    }

    public void setLoggedIn(final String uri) {
	setLoggedIn(XmppURI.uri(uri));
    }

    public void setLoggedIn(final XmppURI userURI) {
	currentUser = userURI;
	setSessionState(SessionStates.loggedIn);
    }

    public void setReady() {
	setSessionState(SessionStates.ready);
    }

    @Override
    public void setSessionState(String state) {
	super.setSessionState(state);
    }

    public IQResponseHandler verifyIQSent(final IPacket iq) {
	assertNotNull(lastIQSent);
	EmiteAsserts.assertPacketLike(iq, lastIQSent);
	return lastIQResponseHandler;
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
