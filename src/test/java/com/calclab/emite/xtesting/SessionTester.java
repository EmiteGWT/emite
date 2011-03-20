package com.calclab.emite.xtesting;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.session.SessionImpl;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;

/**
 * Object of this class are used to test against session (any emite component).
 * 
 * This sessions allow you to simulate reception of stanzas. It also allows to
 * ask (and query) about the stazas that had been send.
 * 
 */
public class SessionTester extends SessionImpl implements Session {

    private final XmppSessionTester tester;

    public SessionTester() {
	this(new XmppSessionTester());
    }

    public SessionTester(String user) {
	this(new XmppSessionTester(user));
    }

    public SessionTester(XmppSessionTester delegate) {
	super(delegate);
	this.tester = delegate;
    }

    public void answer(IQ iq) {
	tester.answer(iq);
    }

    public void answer(String iq) {
	tester.answer(iq);
    }

    public void answerSuccess() {
	tester.answerSuccess();
    }

    public void receives(Message message) {
	tester.receives(message);
    }

    public void receives(Presence presence) {
	tester.receives(presence);
    }

    public void receives(String xml) {
	tester.receives(xml);
    }

    public void setLoggedIn(String uri) {
	tester.setLoggedIn(uri);
    }

    public void setLoggedIn(XmppURI uri) {
	tester.setLoggedIn(uri);
    }

    public void verifyIQSent(IQ iq) {
	tester.verifyIQSent(iq);
    }

    public void verifyIQSent(String iq) {
	tester.verifyIQSent(iq);
    }

    public void verifyNotSent(String xml) {
	tester.verifyNotSent(xml);

    }

    public void verifySent(IPacket packet) {
	tester.verifySent(packet);
    }

    public void verifySent(String xml) {
	tester.verifySent(xml);
    }

}
