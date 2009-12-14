package com.calclab.emite.core.client.xmpp.resource;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.bosh.ConnectionTestHelper;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.testing.events.MockedListener;

public class ResourceBindingManagerTest {
    private ResourceBindingManager manager;
    private ConnectionTestHelper helper;

    @Before
    public void beforeTests() {
	helper = new ConnectionTestHelper();
	manager = new ResourceBindingManager(helper.getConnection());
    }

    @Test
    public void shouldEventIfBindedSucceed() {
	final MockedListener<XmppURI> onBindedListener = new MockedListener<XmppURI>();
	manager.onBinded(onBindedListener);
	helper.simulateReception("<iq type='result' id='bind-resource'><bind xmlns='urn:ietf:params:xml:ns:xmpp-bind'>"
		+ "<jid>somenode@example.com/someresource</jid></bind></iq>");

	assertTrue(onBindedListener.isCalledWithEquals(uri("somenode@example.com/someresource")));

    }

    @Test
    public void shouldPerformBinding() {
	manager.bindResource("resource");
	helper.verifySentLike(new IQ(IQ.Type.set).Includes("bind", "urn:ietf:params:xml:ns:xmpp-bind"));
    }
}
