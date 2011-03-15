package com.calclab.emite.xep.disco.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.xtesting.services.TigaseXMLService;

public class ItemTest {
    private TigaseXMLService xmler;

    @Before
    public void beforeTest() {
	this.xmler = new TigaseXMLService();
    }

    @Test
    public void shouldParsePacket() {
	final Item item = Item.fromPacket(xmler
		.toXML("<item jid='hello@whatever.com/home' name='hello' node='root' />"));
	assertNotNull(item);
	assertEquals("hello@whatever.com/home", item.jid);
	assertEquals("hello", item.name);
	assertEquals("root", item.node);
    }
}
