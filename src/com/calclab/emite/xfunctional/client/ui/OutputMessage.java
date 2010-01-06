package com.calclab.emite.xfunctional.client.ui;

import com.calclab.emite.xfunctional.client.TestOutput.Level;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class OutputMessage extends Composite {

    interface OutputMessageUiBinder extends UiBinder<Widget, OutputMessage> {
    }

    interface Styles extends CssResource {
	String debug();

	String fail();

	String info();

	String success();
    }

    @UiField
    Styles style;
    @UiField
    Label body;

    private static OutputMessageUiBinder uiBinder = GWT.create(OutputMessageUiBinder.class);

    private final Level level;

    public OutputMessage(Level level, String body) {
	this.level = level;
	initWidget(uiBinder.createAndBindUi(this));
	getElement().addClassName(getInfoStyle(level));
	this.body.setText(body);
    }

    public Level getLevel() {
	return level;
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
