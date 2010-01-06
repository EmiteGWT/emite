package com.calclab.emite.xfunctional.client.ui;

import com.calclab.emite.xfunctional.client.FunctionalTest;
import com.calclab.emite.xfunctional.client.TestOutput;
import com.calclab.emite.xfunctional.client.FunctionalTest.State;
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

    private final TestOutput helper;

    private final FunctionalTest test;

    public TestSummary(FunctionalTest test, TestOutput helper) {
	this.test = test;
	this.helper = helper;
	initWidget(uiBinder.createAndBindUi(this));
	name.setText(test.getName());
    }

    @UiHandler("name")
    public void onExecute(ClickEvent event) {
	test.run(helper);
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
