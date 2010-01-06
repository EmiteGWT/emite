package com.calclab.emite.xfunctional.client;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.session.Session.State;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xfunctional.client.ui.TestRunnerView;
import com.calclab.emite.xfunctional.client.ui.TestRunnerView.Level;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.events.Listener;

public class TestRunner implements Context {
    private final Session session;
    private TestResult currentTest;
    private final TestRunnerView view;

    public TestRunner(TestRunnerView view) {
	this.view = view;

	this.session = Suco.get(Session.class);
	session.onStateChanged(new Listener<State>() {
	    @Override
	    public void onEvent(State state) {
		if (state == State.ready && currentTest != null) {
		    performTest();
		} else if (state == State.disconnected && currentTest != null) {
		    endTest();
		}

	    }
	});
    }

    @Override
    public void assertEquals(String description, Object expected, Object actual) {
	boolean isValid = expected.equals(actual);
	addAssertion(description, isValid);
    }

    @Override
    public Session getSession() {
	return session;
    }

    public void run(TestResult test) {
	this.currentTest = test;
	currentTest.start();
	test.getTest().beforeLogin(this);
	session.login(XmppURI.jid(view.getUserJID()), view.getUserPassword());
    }

    @Override
    public void success(String description) {
	addAssertion(description, true);
    }

    private void addAssertion(String description, boolean isValid) {
	currentTest.addAssertion(isValid);
	Level level = isValid ? Level.success : Level.fail;
	String prefix = isValid ? "OK: " : "FAIL :";
	view.print(level, prefix + description);
    }

    private void endTest() {
	currentTest.getTest().afterLogin(this);
    }

    private void performTest() {
	currentTest.getTest().duringLogin(this);
	currentTest.finish();
	currentTest = null;
    }
}
