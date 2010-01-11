package com.calclab.emite.core.client.xmpp.resource;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.testing.ConnectionTester;
import com.calclab.suco.testing.events.MockedListener;

public class ResourceBindingManagerTest {
    private ResourceBindingManager manager;
    private ConnectionTester connection;

    @Before
    public void beforeTests() {
	connection = new ConnectionTester();
	manager = new ResourceBindingManager(connection);
    }

    @Test
    public void shouldEventIfBindedSucceed() {
	final MockedListener<XmppURI> onBindedListener = new MockedListener<XmppURI>();
	manager.onBinded(onBindedListener);
	connection.receives("<iq type='result' id='bind-resource'>" + "<bind xmlns='urn:ietf:params:xml:ns:xmpp-bind'>"
		+ "<jid>somenode@example.com/someresource</jid></bind></iq>");

	assertTrue(onBindedListener.isCalledWithEquals(uri("somenode@example.com/someresource")));

    }

    @Test
    public void shouldPerformBinding() {
	manager.bindResource("resource");
	assertTrue(connection.hasSent(new IQ(IQ.Type.set).Includes("bind", "urn:ietf:params:xml:ns:xmpp-bind")));
    }
}
