package com.calclab.emite.xfunctional.client;

import com.calclab.emite.core.client.bosh.Connection;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.xfunctional.client.TestOutput.Level;
import com.calclab.emite.xfunctional.client.tests.core.TestConnection;
import com.calclab.emite.xfunctional.client.ui.EmiteTesterPanel;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.events.Listener;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class EmiteFunctionalTests implements EntryPoint {

    @Override
    public void onModuleLoad() {
	final EmiteTesterPanel tester = new EmiteTesterPanel();

	tester.add(new TestConnection());

	Connection connection = Suco.get(Connection.class);
	connection.onStanzaReceived(new Listener<IPacket>() {
	    @Override
	    public void onEvent(IPacket stanza) {
		tester.print(Level.debug, "RECEIVED: " + stanza.toString());
	    }
	});

	connection.onStanzaSent(new Listener<IPacket>() {
	    @Override
	    public void onEvent(IPacket stanza) {
		tester.print(Level.debug, "SENT: " + stanza.toString());
	    }
	});

	tester.print(Level.info, "Click over a test to run.");

	RootLayoutPanel.get().add(tester);
    }

}
