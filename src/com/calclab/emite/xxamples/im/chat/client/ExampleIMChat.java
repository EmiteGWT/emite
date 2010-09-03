package com.calclab.emite.xxamples.im.chat.client;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;

import com.calclab.emite.browser.client.PageAssist;
import com.calclab.emite.core.client.CoreGinjector;
import com.calclab.emite.core.client.events.MessageEvent;
import com.calclab.emite.core.client.events.MessageHandler;
import com.calclab.emite.core.client.events.StateChangedEvent;
import com.calclab.emite.core.client.events.StateChangedHandler;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.im.client.ImGinjector;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.im.client.chat.ChatManager;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ExampleIMChat implements EntryPoint {

    /**
     * A custom injector with the desired functionalities (not the recommended
     * way for complex apps: use constructor injection)
     */
    private static interface ChatGinjector extends CoreGinjector, ImGinjector {

    }
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

	ChatGinjector ginjector = GWT.create(ChatGinjector.class);
	XmppSession session = ginjector.getXmppSession();

	session.addSessionStateChangedHandler(true, new StateChangedHandler() {
	    @Override
	    public void onStateChanged(StateChangedEvent event) {
		String state = event.getState();
		log("Current state: " + state);
	    }
	});

	final ChatManager chatManager = ginjector.getChatManager();
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
	chat.addMessageReceivedHandler(new MessageHandler() {
	    @Override
	    public void onMessage(MessageEvent event) {
		log("Message received: " + event.getMessage().getBody());
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
