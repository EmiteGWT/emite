package com.calclab.emite.xxamples.im.pingpongchat.client;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PingPongChatWidget extends VerticalPanel implements PingPongChatDisplay {

    private final Button login;
    private final Button logout;
    private final VerticalPanel panel;

    public PingPongChatWidget() {
	login = new Button("Login");
	logout = new Button("Logout");
	FlowPanel buttons = new FlowPanel();
	buttons.add(login);
	buttons.add(logout);
	add(buttons);
	panel = new VerticalPanel();
	add(panel);

    }

    public void addLoginClickHandler(ClickHandler handler) {
	login.addClickHandler(handler);
    }

    public void addLogoutClickHandler(ClickHandler handler) {
	logout.addClickHandler(handler);
    }

    @Override
    public void print(final String text, final String style) {
	final Label label = new Label(text);
	label.addStyleName(style);
	panel.insert(label, 0);
    }
}
