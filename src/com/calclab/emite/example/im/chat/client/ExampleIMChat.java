package com.calclab.emite.example.im.chat.client;

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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.*;

public class ExampleIMChat implements EntryPoint{

    private VerticalPanel panel;

    @Override
    public void onModuleLoad() {
	panel = new VerticalPanel();
	RootPanel.get("app").add(panel);

	Session session = Suco.get(Session.class);
	session.onStateChanged(new Listener<State>() {
	    @Override
	    public void onEvent(State state) {
		log("Current state: " + state);
	    }
	});
	
	final ChatManager chatManager = Suco.get(ChatManager.class);
	TextBox field = new TextBox();
	panel.add(field);
	field.addChangeHandler(new ChangeHandler() {
	    @Override
	    public void onChange(ChangeEvent event) {
		Chat chat = chatManager.open(uri("test2@localhost"));
		chat.send(new Message(event.toString()));
	    }
	});
	
	log("Example IM Chat");
    }

    private void log(String text) {
	panel.add(new Label(text));
    }

}
