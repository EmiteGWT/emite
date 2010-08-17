package com.calclab.emite.core.client.bosh;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.calclab.emite.core.client.conn.ConnectionSettings;
import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.xtesting.BasicEmiteEventBus;
import com.calclab.emite.xtesting.ServicesTester;
import com.calclab.emite.xtesting.matchers.IsPacketLike;

public class XmppBoshConnectionTests {

    private final ServicesTester services;
    private final XmppBoshConnection connection;

    public XmppBoshConnectionTests() {
	services = new ServicesTester();
	final EmiteEventBus eventBus = new BasicEmiteEventBus();
	connection = new XmppBoshConnection(eventBus, services);
    }

    @Test
    public void shouldSendInitialBody() {
	connection.setSettings(new ConnectionSettings("httpBase", "localhost"));
	connection.connect();
	assertEquals(1, services.requestSentCount());
	final IsPacketLike matcher = IsPacketLike.build("<body to='localhost' "
		+ "content='text/xml; charset=utf-8' xmlns:xmpp='urn:xmpp:xbosh' "
		+ " ack='1' hold='1' secure='true' xml:lang='en' "
		+ "xmpp:version='1.0' wait='60' xmlns='http://jabber.org/protocol/httpbind' />");
	assertTrue(matcher.matches(services.getSentPacket(0), System.out));
    }
}
