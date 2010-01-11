package com.calclab.emite.xfunctional.client;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.session.Session.State;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xfunctional.client.ui.TestRunnerView;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.events.Listener;

public class TestRunner {
    private final Session session;
    private final TestRunnerView view;
    private Context ctx;

    public TestRunner(TestRunnerView view) {
	this.view = view;

	this.session = Suco.get(Session.class);
	session.onStateChanged(new Listener<Session>() {
	    @Override
	    public void onEvent(Session session) {
		State state = session.getState();
		if (state == State.ready && ctx != null) {
		    performTest();
		} else if (state == State.disconnected && ctx != null) {
		    endTest();
		}
	    }
	});
    }

    public void run(TestResult test) {
	this.ctx = new Context(session, test, view);
	ctx.getTestResult().start();
	test.getSuite().beforeLogin(ctx);
	session.login(XmppURI.jid(view.getUserJID()), view.getUserPassword());
    }

    private void endTest() {
	ctx.getTestResult().getSuite().afterLogin(ctx);
    }

    private void performTest() {
	TestResult testResult = ctx.getTestResult();
	FunctionalTest test = testResult.getTest();
	test.run(ctx);
	testResult.finish();
    }
}
