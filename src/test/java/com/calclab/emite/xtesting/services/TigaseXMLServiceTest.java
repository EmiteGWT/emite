package com.calclab.emite.xtesting.services;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.calclab.emite.core.client.packet.IPacket;

public class TigaseXMLServiceTest {

    final String response = "<body xmlns=\"http://jabber.org/protocol/httpbind\" xmlns:stream=\"http://etherx.jabber.org/streams\" "
	    + "authid=\"27343471\" sid=\"27343471\" secure=\"true\" requests=\"2\" inactivity=\"30\" polling=\"5\" wait=\"60\" ver=\"1.6\">"
	    + "<stream:features><mechanisms xmlns=\"urn:ietf:params:xml:ns:xmpp-sasl\"><mechanism>DIGEST-MD5</mechanism>"
	    + "<mechanism>PLAIN</mechanism><mechanism>ANONYMOUS</mechanism><mechanism>CRAM-MD5</mechanism>"
	    + "</mechanisms><compression xmlns=\"http://jabber.org/features/compress\"><method>zlib</method></compression>"
	    + "<bind xmlns=\"urn:ietf:params:xml:ns:xmpp-bind\"/><session xmlns=\"urn:ietf:params:xml:ns:xmpp-session\"/>"
	    + "</stream:features></body>";

    @Test
    public void testReal() {
	final TigaseXMLService parser = new TigaseXMLService();
	final IPacket body = parser.toXML(response);
	final List<? extends IPacket> stanzas = body.getChildren();
	assertEquals(1, stanzas.size());
	final IPacket features = stanzas.get(0);
	assertEquals("stream:features", features.getName());
	final IPacket mechanisms = features.getChildren().get(0);
	assertEquals("mechanisms", mechanisms.getName());
	final IPacket mec1 = mechanisms.getChildren().get(0);
	assertEquals("mechanism", mec1.getName());
	assertEquals("DIGEST-MD5", mec1.getText());
    }
}
