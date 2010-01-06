package com.calclab.emite.xfunctional.client.ui;

import com.calclab.emite.xfunctional.client.FunctionalTest;
import com.calclab.emite.xfunctional.client.TestOutput;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class EmiteTesterPanel extends Composite implements TestOutput {

    interface EmiteTesterPanelUiBinder extends UiBinder<Widget, EmiteTesterPanel> {
    }

    interface TestStyles extends CssResource {
	String debug();

	String fail();

	String info();

	String success();
    }

    private static EmiteTesterPanelUiBinder uiBinder = GWT.create(EmiteTesterPanelUiBinder.class);

    @UiField
    FlowPanel tests, output;
    @UiField
    TestStyles style;

    public EmiteTesterPanel() {
	initWidget(uiBinder.createAndBindUi(this));
    }

    public void add(FunctionalTest test) {
	tests.add(new TestSummary(test, this));
    }

    @Override
    public void print(Level level, String message) {
	HTML html = new HTML(message);
	html.getElement().addClassName(getInfoStyle(level));
	output.add(html);
    }

    private String getInfoStyle(Level level) {
	if (Level.info == level) {
	    return style.info();
	} else if (Level.debug == level) {
	    return style.debug();
	} else if (Level.fail == level) {
	    return style.fail();
	} else if (Level.success == level) {
	    return style.success();
	}
	return style.debug();
    }

}
