package com.calclab.emite.xfunctional.client;

import com.calclab.emite.core.client.bosh.Connection;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.session.Session.State;
import com.calclab.emite.xfunctional.client.tests.TestConnection;
import com.calclab.emite.xfunctional.client.tests.TestSearch;
import com.calclab.emite.xfunctional.client.ui.TestRunnerPanel;
import com.calclab.emite.xfunctional.client.ui.TestRunnerView.Level;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.events.Listener;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class TestRunnerApp implements EntryPoint {

    @Override
    public void onModuleLoad() {
	final TestRunnerPanel runner = new TestRunnerPanel();

	// add tests here
	runner.addTest(new TestConnection());
	runner.addTest(new TestSearch());

	setConnectionListeners(runner);
	setSessionListeners(runner);
	runner.setStatus("Click over a test to run.");

	RootLayoutPanel.get().add(runner);
    }

    private void setConnectionListeners(final TestRunnerPanel runner) {
	Connection connection = Suco.get(Connection.class);
	connection.onStanzaReceived(new Listener<IPacket>() {
	    @Override
	    public void onEvent(IPacket stanza) {
		runner.print(Level.debug, "RECEIVED: " + stanza.toString());
	    }
	});

	connection.onStanzaSent(new Listener<IPacket>() {
	    @Override
	    public void onEvent(IPacket stanza) {
		runner.print(Level.debug, "SENT: " + stanza.toString());
	    }
	});
    }

    private void setSessionListeners(final TestRunnerPanel runner) {
	Session session = Suco.get(Session.class);
	session.onStateChanged(new Listener<State>() {
	    @Override
	    public void onEvent(State state) {
		runner.setSessionState(state.toString());
	    }
	});
	runner.setSessionState(session.getState().toString());
    }

}
