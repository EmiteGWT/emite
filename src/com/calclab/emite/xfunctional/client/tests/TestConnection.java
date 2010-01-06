package com.calclab.emite.xfunctional.client.tests;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xfunctional.client.DelayedCode;
import com.calclab.emite.xfunctional.client.FunctionalTest;
import com.calclab.suco.client.Suco;

public class TestConnection extends FunctionalTest {

    @Override
    public String getName() {
	return "Connection test";
    }

    @Override
    public void run() {
	testBegins();
	final Session session = Suco.get(Session.class);

	equal("State should be disconnected", Session.State.disconnected, session.getState());
	info("Connecting as test1@localhost");
	session.login(XmppURI.uri("test1@localhost"), "test1");

	delay(2000, new DelayedCode() {
	    @Override
	    public void run() {
		equal("State should be connected", Session.State.ready, session.getState());
		session.logout();
		delay(100, new DelayedCode() {
		    @Override
		    public void run() {
			testEnds();
		    }
		});
	    }
	});
    }

}
