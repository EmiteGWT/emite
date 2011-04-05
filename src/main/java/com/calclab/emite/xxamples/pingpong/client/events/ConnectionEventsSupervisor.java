package com.calclab.emite.xxamples.pingpong.client.events;

import com.calclab.emite.core.client.conn.ConnectionStateChangedEvent;
import com.calclab.emite.core.client.conn.ConnectionStateChangedHandler;
import com.calclab.emite.core.client.conn.StanzaEvent;
import com.calclab.emite.core.client.conn.StanzaHandler;
import com.calclab.emite.core.client.conn.XmppConnection;
import com.calclab.emite.xxamples.pingpong.client.PingPongDisplay;
import com.calclab.emite.xxamples.pingpong.client.PingPongDisplay.Style;
import com.google.inject.Inject;

public class ConnectionEventsSupervisor {

    @Inject
    public ConnectionEventsSupervisor(XmppConnection connection, final PingPongDisplay display) {
	connection.addStanzaReceivedHandler(new StanzaHandler() {
	    @Override
	    public void onStanza(StanzaEvent event) {
		display.print("IN: " + event.getStanza(), Style.stanzaReceived);
	    }
	});

	connection.addStanzaSentHandler(new StanzaHandler() {
	    @Override
	    public void onStanza(StanzaEvent event) {
		display.print("OUT: " + event.getStanza(), Style.stanzaSent);
	    }
	});

	connection.addConnectionStateChangedHandler(new ConnectionStateChangedHandler() {
	    @Override
	    public void onStateChanged(ConnectionStateChangedEvent event) {
		display.print("Connection state: " + event.getState(), Style.info);
	    }
	});
    }

}
