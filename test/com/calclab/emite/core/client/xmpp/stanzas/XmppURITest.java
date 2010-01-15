package com.calclab.emite.core.client.xmpp.stanzas;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class XmppURITest {

    @Test
    public void checkUriEqualsNoResourceOtherWithoutNode() {
	final XmppURI uri = uri("test@localhost/resource1");
	final XmppURI uri2 = uri("localhost/resource2");
	assertFalse(uri.equalsNoResource(uri2));
    }

    @Test
    public void checkUriEqualsNoResourceWithoutNode() {
	final XmppURI uri = uri("localhost/resource1");
	final XmppURI uri2 = uri("localhost/resource2");
	assertTrue(uri.equalsNoResource(uri2));
    }

    @Test
    public void checkUriEqualsNoResourceWithoutNodeOtherWithNode() {
	final XmppURI uri = uri("localhost/resource1");
	final XmppURI uri2 = uri("test@localhost/resource2");
	assertFalse(uri.equalsNoResource(uri2));
    }

    @Test
    public void checkUriEqualsNoResourceWithoutResource() {
	final XmppURI uri = uri("localhost");
	final XmppURI uri2 = uri("localhost");
	assertTrue(uri.equalsNoResource(uri2));
    }

    @Test
    public void checkUriFormat() {
	final XmppURI uri = uri("xmpp:test@example/res");
	assertEquals("test", uri.getNode());
	assertEquals("example", uri.getHost());
	assertEquals("res", uri.getResource());
    }

    public void checkUriFormatWithOnlyAtFails() {
	assertNull(uri("xmpp:@"));
    }

    public void checkUriFormatWithOnlyHostFails() {
	assertNull(uri("xmpp:@example"));
    }

    public void checkUriFormatWithOnlyNodeFails() {
	assertNull(uri("xmpp:test@"));
    }

    public void checkUriFormatWithOnlySlashFails() {
	assertNull(uri("xmpp:/"));
    }

    public void checkUriFormatWithoutHost() {
	assertNull(uri("xmpp:test@/res"));
    }

    public void checkUriFormatWithoutNodeFails() {
	assertNull(uri("xmpp:@example/res"));
    }

    @Test
    public void equalsShouldBeIndependentOfPrefix() {
	final XmppURI uri1 = uri("xmpp:test@example/res");
	final XmppURI uri2 = uri("test@example/res");
	assertEquals(uri1, uri2);
	assertEquals(uri1.hashCode(), uri2.hashCode());
    }

    @Test
    public void shouldBeCaseInsensitive() {
	final XmppURI uri1 = uri("xmpp:test@EXAMPLE/res");
	final XmppURI uri2 = uri("tesT@example/reS");
	assertEquals(uri1, uri2);
    }

    @Test
    public void shouldCacheURIS() {
	final XmppURI uri1 = uri("xmpp:test@example/res");
	final XmppURI uri2 = uri("xmpp:test@example/res");
	assertSame(uri1, uri2);
    }

    @Test
    public void shouldCreateHostURI() {
	final XmppURI uri = uri("node@domain/resource");
	final XmppURI host = uri.getHostURI();
	assertNotNull(host);
	assertFalse(host.hasNode());
	assertFalse(host.hasResource());
	assertEquals("node@domain/resource", uri.toString());
    }

    @Test
    public void shouldParseNull() {
	assertNull(uri(null));
    }

    @Test
    public void shouldParseURIWithoutNode() {
	final XmppURI uri = uri("domain/res");
	assertFalse(uri.hasNode());
	assertNull(uri.getNode());
	assertEquals("domain", uri.getHost());
	assertEquals("res", uri.getResource());
	assertEquals("domain/res", uri.toString());
    }

    @Test
    public void shouldParseWithNoPrefix() {
	final XmppURI uri = uri("test@example/res");
	assertEquals("test", uri.getNode());
	assertEquals("example", uri.getHost());
	assertEquals("res", uri.getResource());
    }

    public void shouldParseWithNoResource() {
	final XmppURI uri = uri("xmpp:test@example");
	assertFalse(uri.hasResource());
    }

}
