package com.calclab.emite.xxamples.im.echo.client;

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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ExampleEcho implements EntryPoint {

    private VerticalPanel output;

    @Override
    public void onModuleLoad() {
	output = new VerticalPanel();
	RootPanel.get("app").add(output);

	log("Example echo chat");
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

	ChatManager chatManager = Suco.get(ChatManager.class);
	final Chat chat = chatManager.open(uri(user));
	chat.onMessageReceived(new Listener<Message>() {
	    @Override
	    public void onEvent(Message msg) {
		String body = msg.getBody();
		log("Message received: " + body);
		chat.send(new Message(body + " at: " + System.currentTimeMillis()));
	    }
	});

    }

    private void log(String text) {
	output.add(new Label(text));
    }

}
