package com.calclab.emite.core.client.xmpp.resource;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.xtesting.XmppConnectionTester;

public class ResourceBindingManagerTest {
    private ResourceBindingManager manager;
    private XmppConnectionTester connection;
    private EmiteEventBus eventBus;
    private ResourceBindResultEvent currentEvent;

    @Before
    public void beforeTests() {
	connection = new XmppConnectionTester();
	eventBus = connection.getEventBus();
	manager = new ResourceBindingManager(connection);
    }

    @Test
    public void shouldEventIfBindedSucceed() {
	currentEvent = null;
	eventBus.addHandler(ResourceBindResultEvent.getType(), new ResourceBindResultHandler() {
	    @Override
	    public void onBinded(final ResourceBindResultEvent event) {
		currentEvent = event;
	    }
	});

	connection.receives("<iq type='result' id='bind-resource'>" + "<bind xmlns='urn:ietf:params:xml:ns:xmpp-bind'>"
		+ "<jid>somenode@example.com/someresource</jid></bind></iq>");

	assertNotNull(currentEvent);
	assertEquals(uri("somenode@example.com/someresource"), currentEvent.getXmppUri());
    }

    @Test
    public void shouldPerformBinding() {
	manager.bindResource("resource");
	assertTrue(connection.hasSent(new IQ(IQ.Type.set).Includes("bind", "urn:ietf:params:xml:ns:xmpp-bind")));
    }
}
