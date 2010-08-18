package com.calclab.emite.xxamples.pingpong.client;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class PingPongChatWidget extends VerticalPanel implements PingPongDisplay {

    private final Button login;
    private final Button logout;
    private final VerticalPanel panel;
    private final VerticalPanel header;
    private final Button clear;
    private final Label currentUser;
    private final Label sessionStatus;

    public PingPongChatWidget() {
	header = new VerticalPanel();
	add(header);
	login = new Button("Login");
	logout = new Button("Logout");
	clear = new Button("Clear");
	currentUser = new Label("no user yet.");
	sessionStatus = new Label("no session status yet.");
	FlowPanel buttons = new FlowPanel();
	buttons.add(currentUser);
	buttons.add(sessionStatus);
	buttons.add(login);
	buttons.add(logout);
	buttons.add(clear);
	add(buttons);
	panel = new VerticalPanel();
	add(panel);
    }

    @Override
    public void addClearClickHandler(ClickHandler handler) {
	clear.addClickHandler(handler);
    }

    @Override
    public void addLoginClickHandler(ClickHandler handler) {
	login.addClickHandler(handler);
    }

    @Override
    public void addLogoutClickHandler(ClickHandler handler) {
	logout.addClickHandler(handler);
    }

    @Override
    public Widget asWidget() {
	return this;
    }

    @Override
    public void clearOutput() {
	panel.clear();
    }

    @Override
    public Label getCurrentUser() {
	return currentUser;
    }

    @Override
    public Label getSessionStatus() {
	return sessionStatus;
    }

    @Override
    public void print(final String text, final String style) {
	final Label label = new Label(text);
	label.addStyleName(style);
	panel.insert(label, 0);
    }

    @Override
    public void printHeader(String text, String style) {
	final Label label = new Label(text);
	label.addStyleName(style);
	header.add(label);

    }
}
