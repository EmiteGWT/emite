package com.calclab.emite.xfunctional.client.ui;

import com.calclab.emite.xfunctional.client.FunctionalTest;
import com.calclab.emite.xfunctional.client.TestOutput;
import com.calclab.emite.xfunctional.client.FunctionalTest.State;
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
import com.google.gwt.user.client.ui.Widget;

public class TestRunner extends Composite implements TestOutput {

    interface EmiteTesterPanelUiBinder extends UiBinder<Widget, TestRunner> {
    }

    private static EmiteTesterPanelUiBinder uiBinder = GWT.create(EmiteTesterPanelUiBinder.class);

    @UiField
    FlowPanel tests, output;

    @UiField
    ListBox logLevel;

    @UiField
    Label status, sessionState;

    private String currentLevel;

    public TestRunner() {
	initWidget(uiBinder.createAndBindUi(this));
	logLevel.addItem("All");
	logLevel.addItem("Info");
	logLevel.addItem("Results");
	this.currentLevel = "All";
    }

    public void addTest(final FunctionalTest test) {
	final TestSummary summary = new TestSummary(test, this);
	test.onStateChanged(new Listener<FunctionalTest.State>() {
	    @Override
	    public void onEvent(FunctionalTest.State state) {
		if (state == State.running) {
		    setStatus("Running '" + test.getName() + "'...");
		} else if (state == State.failed) {
		    setStatus(test.getName() + " failed.");
		} else if (state == State.succeed) {
		    setStatus(test.getName() + " succeed.");
		}
		summary.setState(state);
	    }
	});
	summary.setState(test.getState());
	tests.add(summary);
    }

    @UiHandler("btnClear")
    public void onClear(ClickEvent e) {
	output.clear();
    }

    @UiHandler("logLevel")
    public void onLogLevelChanged(ChangeEvent e) {
	currentLevel = logLevel.getItemText(logLevel.getSelectedIndex());
	int total = output.getWidgetCount();
	for (int index = 0; index < total; index++) {
	    changeWidgetVisibility((OutputMessage) output.getWidget(index));
	}
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

    private void changeWidgetVisibility(OutputMessage message) {
	Level level = message.getLevel();
	boolean visibility = false;
	if ("All".equals(currentLevel)) {
	    visibility = true;
	} else if ("Info".equals(currentLevel)) {
	    visibility = (Level.debug != level);
	} else if ("Results".equals(currentLevel)) {
	    visibility = (Level.fail == level || Level.success == level);
	}
	message.setVisible(visibility);
    }

}
