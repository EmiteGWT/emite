package com.calclab.emite.xfunctional.client.ui;

import com.calclab.emite.xfunctional.client.TestResult;
import com.calclab.emite.xfunctional.client.TestRunner;
import com.calclab.emite.xfunctional.client.TestResult.State;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class TestSummary extends Composite {

    interface Styles extends CssResource {

	String failed();

	String notRunned();

	String running();

	String succeed();

    }

    interface TestSummaryUiBinder extends UiBinder<Widget, TestSummary> {
    }

    private static TestSummaryUiBinder uiBinder = GWT.create(TestSummaryUiBinder.class);

    @UiField
    Label name;
    @UiField
    Styles style;

    private final TestResult test;
    private final TestRunner runner;

    public TestSummary(TestResult testResult, TestRunner runner) {
	this.test = testResult;
	this.runner = runner;
	initWidget(uiBinder.createAndBindUi(this));
	name.setText(testResult.getName());
    }

    @UiHandler("name")
    public void onExecute(ClickEvent event) {
	runner.run(test);
    }

    public void setState(State state) {
	name.getElement().addClassName(getClassName(state));
    }

    private String getClassName(State state) {
	if (state == State.running) {
	    return style.running();
	} else if (state == State.succeed) {
	    return style.succeed();
	} else if (state == State.failed) {
	    return style.failed();
	}
	return style.notRunned();
    }
}
