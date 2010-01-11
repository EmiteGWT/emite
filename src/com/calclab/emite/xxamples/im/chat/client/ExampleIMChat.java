package com.calclab.emite.xxamples.im.chat.client;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;

import com.calclab.emite.browser.client.PageAssist;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.session.Session.State;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.events.Listener;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ExampleIMChat implements EntryPoint {

    private VerticalPanel output;
    private TextBox input;

    @Override
    public void onModuleLoad() {
	createUI();

	log("Example IM Chat");
	String self = PageAssist.getMeta("emite.user");
	log("Current user: " + self);
	final String user = PageAssist.getMeta("emite.chat");
	log("Chat with user: " + user);

	final Session session = Suco.get(Session.class);
	session.onStateChanged(new Listener<Session>() {
	    @Override
	    public void onEvent(Session session) {
		State state = session.getState();
		log("Current state: " + state);
	    }
	});

	final ChatManager chatManager = Suco.get(ChatManager.class);
	input.addChangeHandler(new ChangeHandler() {
	    @Override
	    public void onChange(ChangeEvent event) {
		String msg = input.getText();
		log("Message sent: " + msg);
		Chat chat = chatManager.open(uri(user));
		chat.send(new Message(msg));
		input.setText("");
	    }
	});

	Chat chat = chatManager.open(uri(user));
	chat.onMessageReceived(new Listener<Message>() {
	    @Override
	    public void onEvent(Message msg) {
		log("Message received: " + msg.getBody());
	    }
	});

    }

    private void createUI() {
	DockPanel dock = new DockPanel();
	input = new TextBox();
	dock.add(input, DockPanel.SOUTH);
	output = new VerticalPanel();
	dock.add(output, DockPanel.SOUTH);
	RootPanel.get("app").add(dock);
    }

    private void log(String text) {
	output.add(new Label(text));
    }

}
