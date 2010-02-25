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
	assertTrue(XmppUriParser.isValidDomain("simple"));
    }

    @Test
    public void shouldValidateSimpleJID() {
	assertValidJID("name@host");
	assertValidJID("name@host.ext");
	assertInvalidJid("name");
	assertInvalidJid("name@host/name");
    }

    private void assertInvalidJid(final String uri) {
	assertFalse(XmppUriParser.isValidJid(uri));
    }

    private void assertValidJID(final String uri) {
	assertTrue(XmppUriParser.isValidJid(uri));
    }
}
