package com.calclab.emite.xep.vcard.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.xtesting.services.TigaseXMLService;

public class VCardResponseTests {

    @Test
    public void shouldParseEmptyVCard() {
	String VCARD_EMPTY = "<iq id='v1' to='stpeter@jabber.org/roundabout' type='result'>\n"
		+ "<vCard xmlns='vcard-temp' /></iq>";
	IPacket result = TigaseXMLService.toPacket(VCARD_EMPTY);
	VCardResponse response = new VCardResponse(result);
	assertEquals(IQ.Type.result, response.getType());
	assertFalse(response.hasVCard());
	assertNull(response.getVCard());
    }

    @Test
    public void shouldParseItemNotFound() {
	String ITEM_NOT_FOUND = "<iq id='v1'\n" + "    to='stpeter@jabber.org/roundabout'\n"
		+ "    type='error'>\n" + "  <vCard xmlns='vcard-temp'/>\n"
		+ "  <error type='cancel'>\n"
		+ "    <item-not-found xmlns='urn:ietf:params:xml:ns:xmpp-stanzas'/>\n"
		+ "  </error>\n" + "</iq>";
	IPacket result = TigaseXMLService.toPacket(ITEM_NOT_FOUND);
	VCardResponse response = new VCardResponse(result);
	assertEquals(IQ.Type.error, response.getType());
	assertTrue(response.isError());
    }

    @Test
    public void shouldParseReturnsVCard() {
	String VCARD_RESPONSE = "<iq id='v1' to='stpeter@jabber.org/roundabout' type='result'>\n"
		+ "<vCard xmlns='vcard-temp'><FN>Peter Saint-Andre</FN></vCard></iq>";
	IPacket result = TigaseXMLService.toPacket(VCARD_RESPONSE);
	VCardResponse response = new VCardResponse(result);
	assertEquals(IQ.Type.result, response.getType());
	assertTrue(response.isSuccess());
	assertTrue(response.hasVCard());
	assertNotNull(response.getVCard());
    }
}
