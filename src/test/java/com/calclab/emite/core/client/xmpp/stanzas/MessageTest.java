package com.calclab.emite.core.client.xmpp.stanzas;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.calclab.emite.core.client.packet.MatcherFactory;
import com.calclab.emite.core.client.packet.Packet;
import com.calclab.emite.core.client.xmpp.stanzas.Message.Type;

public class MessageTest {
    @Test
    public void shouldAddSubject() {
	final Message message = new Message("message", uri("user2@domain/r"), uri("user1@domain/r"))
		.Subject("the subject");
	assertEquals("the subject", message.getSubject());
    }

    @Test
    public void shouldNotAddBodyIfNotSpecified() {
	final Message message = new Message();
	assertNull(message.getBody());
	assertEquals(0, message.getChildren(MatcherFactory.byName("body")).size());
	final Message message2 = new Message(null, uri("other@domain"), uri("me@domain"), Message.Type.chat);
	assertEquals(0, message2.getChildren(MatcherFactory.byName("body")).size());
    }

    @Test
    public void shouldRetrieveSubject() {
	final Message message = new Message(new Packet("message").With(new Packet("subject", null)
		.WithText("the subject")));
	assertEquals("the subject", message.getSubject());
    }

    @Test
    public void shouldReturnNullThread() {
	final Message message = new Message("message", uri("user2@domain/r"), uri("user1@domain/r"));
	assertEquals(null, message.getThread());
    }

    @Test
    public void shouldReturnUnkownType() {
	final Message message = new Message(new Packet("message").With("type", "invalid-here"));
	assertSame(Type.normal, message.getType());
    }

    @Test
    public void shouldTypeNotSpecifiedType() {
	final Message message = new Message(new Packet("message"));
	final Type type = message.getType();
	assertSame(Type.normal, type);
    }
}
