package com.calclab.emite.xfunctional.client.ui;

import com.calclab.emite.xfunctional.client.FunctionalTest;
import com.calclab.emite.xfunctional.client.TestOutput;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class TestSummary extends Composite {

    private static TestSummaryUiBinder uiBinder = GWT.create(TestSummaryUiBinder.class);

    interface TestSummaryUiBinder extends UiBinder<Widget, TestSummary> {
    }
    


    @UiField
    Label name;
    
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
}
