package com.calclab.emite.xxamples.im.echo.client;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;

import com.calclab.emite.browser.client.PageAssist;
import com.calclab.emite.core.client.events.MessageEvent;
import com.calclab.emite.core.client.events.MessageHandler;
import com.calclab.emite.core.client.events.StateChangedEvent;
import com.calclab.emite.core.client.events.StateChangedHandler;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.im.client.chat.ChatManager;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * A simple echo client
 */
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

	EchoGinjector ginjector = GWT.create(EchoGinjector.class);

	XmppSession session = ginjector.getXmppSession();

	session.addSessionStateChangedHandler(true, new StateChangedHandler() {
	    @Override
	    public void onStateChanged(StateChangedEvent event) {
		String state = event.getState();
		log("Current state: " + state);
	    }
	});

	ChatManager chatManager = ginjector.getChatManager();
	final Chat chat = chatManager.open(uri(user));
	chat.addMessageReceivedHandler(new MessageHandler() {
	    @Override
	    public void onMessage(MessageEvent event) {
		String body = event.getMessage().getBody();
		log("Message received: " + body);
		chat.send(new Message(body + " at: " + System.currentTimeMillis()));
	    }
	});
    }

    private void log(String text) {
	output.add(new Label(text));
    }

}
