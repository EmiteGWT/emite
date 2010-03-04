package com.calclab.emite.core.client.xmpp.stanzas;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class XmppUriParserTests {

    @Test
    public void shouldGetDomain() {
	assertEquals("host", XmppUriParser.getDomain("name@host"));
	assertEquals("host.com.net", XmppUriParser.getDomain("name@host.com.net"));
	assertEquals("host.com.net", XmppUriParser.getDomain("name@host.com.net/res"));
    }

    @Test
    public void shouldGetNode() {
	assertEquals("name", XmppUriParser.getNode("name@host"));
	assertEquals("name", XmppUriParser.getNode("xmpp:name@host"));
    }

    @Test
    public void shouldGetResource() {
	assertEquals("res", XmppUriParser.getResource("name@host/res"));
	assertEquals("res", XmppUriParser.getResource("host/res"));
    }

    @Test
    public void shouldValidateDomain() {
	assertFalse(XmppUriParser.isValidDomain("simple"));
	assertTrue(XmppUriParser.isValidDomain("simple.net"));
	assertTrue(XmppUriParser.isValidDomain("localhost"));
    }

    @Test
    public void shouldValidateJID() {
	assertTrue(XmppUriParser.isValidJid("name@localhost"));
	assertTrue(XmppUriParser.isValidJid("name@host.ext"));
	assertFalse(XmppUriParser.isValidJid("name@host"));
	assertFalse(XmppUriParser.isValidJid("name"));
	assertFalse(XmppUriParser.isValidJid("name@host.net/name"));
    }
}
