package com.calclab.emite.xep.disco.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.xtesting.services.TigaseXMLService;

public class IdentityTest {
    private TigaseXMLService xmler;

    @Before
    public void beforeTest() {
	this.xmler = new TigaseXMLService();
    }

    @Test
    public void shouldParsePacket() {
	final Identity identity = Identity.fromPacket(xmler
		.toXML("<identity category='pubsub' type='pep' name='publish' />"));
	assertNotNull(identity);
	assertEquals("pubsub", identity.category);
	assertEquals("pep", identity.type);
	assertEquals("publish", identity.name);
    }
}
