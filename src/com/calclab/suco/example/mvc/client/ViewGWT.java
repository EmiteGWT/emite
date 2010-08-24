package com.calclab.suco.example.mvc.client;

import com.calclab.suco.client.events.Event0;
import com.calclab.suco.client.events.Listener0;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ViewGWT extends VerticalPanel implements View {
    private Button button;
    private Event0 clicked;

    public ViewGWT() {
	this.clicked = new Event0("clicked");
	this.button = new Button("click me!");
	this.button.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		clicked.fire();
	    }
	});
	this.add(button);
	this.print("Running...");
    }

    public void print(String text) {
	this.add(new Label(text));
    }

    @Override
    public void onclicked(Listener0 listener) {
	clicked.add(listener);
    }

}
