package com.calclab.emite.xfunctional.client;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.xfunctional.client.ui.TestRunnerView;
import com.calclab.emite.xfunctional.client.ui.TestRunnerView.Level;

public class Context {
    private final TestResult test;
    private final TestRunnerView view;
    private final Session session;

    public Context(Session session, TestResult currentTest, TestRunnerView view) {
	this.session = session;
	this.test = currentTest;
	this.view = view;
    }

    public void assertEquals(String description, Object expected, Object actual) {
	boolean isValid = expected.equals(actual);
	addAssertion(description, isValid);
    }

    public void fail(String description) {
	addAssertion(description, false);
    }

    public Session getSession() {
	return session;
    }

    public TestResult getTestResult() {
	return test;
    }

    public void info(String message) {
	view.print(Level.info, message);
    }

    public void success(String description) {
	addAssertion(description, true);
    }

    public void test(String testName, FunctionalTest test) {
	info("Running test: " + testName);
	test.run(this);
    }

    private void addAssertion(String description, boolean isValid) {
	if (test != null) {
	    test.addAssertion(isValid);
	} else {
	    view.print(Level.fail, "error interno - no tenemos test!");
	}

	Level level = isValid ? Level.success : Level.fail;
	String prefix = isValid ? "OK: " : "FAIL :";
	view.print(level, prefix + description);
    }

}
