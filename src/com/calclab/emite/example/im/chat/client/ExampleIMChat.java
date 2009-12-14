package com.calclab.emite.example.im.chat.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ExampleIMChat implements EntryPoint{

    private VerticalPanel panel;

    @Override
    public void onModuleLoad() {
	panel = new VerticalPanel();
	TextBox field = new TextBox();
	panel.add(field);
	
	log("Example IM Chat");
    }

    private void log(String text) {
	panel.add(new Label(text));
    }

}
