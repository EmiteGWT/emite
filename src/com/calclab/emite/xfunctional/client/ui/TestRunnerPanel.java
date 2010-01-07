package com.calclab.emite.xfunctional.client.ui;

import java.util.HashMap;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.xfunctional.client.FunctionalTest;
import com.calclab.emite.xfunctional.client.FunctionalTestSuite;
import com.calclab.emite.xfunctional.client.TestResult;
import com.calclab.emite.xfunctional.client.TestRunner;
import com.calclab.emite.xfunctional.client.TestResult.State;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.events.Listener;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class TestRunnerPanel extends Composite implements TestRunnerView {

    interface EmiteTesterPanelUiBinder extends UiBinder<Widget, TestRunnerPanel> {
    }

    private static EmiteTesterPanelUiBinder uiBinder = GWT.create(EmiteTesterPanelUiBinder.class);

    @UiField
    FlowPanel tests, output;

    @UiField
    ListBox logLevel;

    @UiField
    Label status, sessionState;

    @UiField
    TextBox userJID, userPassword;

    private String currentLevel;

    private final TestRunner runner;

    private final Session session;

    public TestRunnerPanel() {
	initWidget(uiBinder.createAndBindUi(this));
	this.session = Suco.get(Session.class);
	initLogLevels();
	this.runner = new TestRunner(this);
	new TestRunnerLogic(this);
    }

    public void addTestSuite(String name, FunctionalTestSuite testSuite) {
	HashMap<String, FunctionalTest> tests = testSuite.getTests();
	for (String testName : tests.keySet()) {
	    addTest(name + ": " + testName, tests.get(testName), testSuite);
	}
    }

    @Override
    public String getUserJID() {
	return userJID.getText();
    }

    @Override
    public String getUserPassword() {
	return userPassword.getText();
    }

    @UiHandler("btnClear")
    public void onClear(ClickEvent e) {
	output.clear();
    }

    @UiHandler("btnLogin")
    public void onLogin(ClickEvent e) {
    }

    @UiHandler("logLevel")
    public void onLogLevelChanged(ChangeEvent e) {
	currentLevel = logLevel.getItemText(logLevel.getSelectedIndex());
	int total = output.getWidgetCount();
	for (int index = 0; index < total; index++) {
	    changeWidgetVisibility((OutputMessage) output.getWidget(index));
	}
    }

    @UiHandler("btnLogout")
    public void onLogout(ClickEvent e) {
	session.logout();
    }

    @UiHandler("btnRunAll")
    public void onRunAll(ClickEvent e) {
    }

    @Override
    public void print(Level level, String message) {
	OutputMessage outputMessage = new OutputMessage(level, message);
	changeWidgetVisibility(outputMessage);
	output.add(outputMessage);
    }

    public void setSessionState(String state) {
	sessionState.setText(state);
    }

    public void setStatus(String message) {
	status.setText(message);
    }

    private void addTest(String name, FunctionalTest test, FunctionalTestSuite suite) {
	// assert test != null;
	GWT.log("adding: " + name, null);
	final TestResult result = new TestResult(name, test, suite);
	final TestSummary summary = new TestSummary(result, runner);
	result.onStateChanged(new Listener<State>() {
	    @Override
	    public void onEvent(State state) {
		if (state == State.running) {
		    String msg = "Running '" + result.getName() + "'...";
		    print(Level.info, msg);
		    setStatus(msg);
		} else if (state == State.failed) {
		    String msg = "FAIL: '" + result.getName() + "' -" + result.getSummary();
		    print(Level.fail, msg);
		    setStatus(result.getName() + " failed.");
		} else if (state == State.succeed) {
		    String msg = "SUCCESS: '" + result.getName() + "' -" + result.getSummary();
		    print(Level.success, msg);
		    setStatus(result.getName() + " succeed.");
		}
		summary.setState(state);

	    }
	});
	summary.setState(result.getState());
	tests.add(summary);
    }

    private void changeWidgetVisibility(OutputMessage message) {
	Level level = message.getLevel();
	boolean visibility = false;
	if ("All".equals(currentLevel)) {
	    visibility = true;
	} else if ("Stanzas".equals(currentLevel)) {
	    visibility = (Level.debug != level);
	} else if ("Info".equals(currentLevel)) {
	    visibility = (Level.debug != level && Level.stanzas != level);
	} else if ("Results".equals(currentLevel)) {
	    visibility = (Level.fail == level || Level.success == level);
	}
	message.setVisible(visibility);
    }

    private void initLogLevels() {
	logLevel.addItem("Results");
	logLevel.addItem("Info");
	logLevel.addItem("Stanzas");
	logLevel.addItem("All");
	logLevel.setSelectedIndex(2);
	this.currentLevel = logLevel.getItemText(logLevel.getSelectedIndex());
    }
}
