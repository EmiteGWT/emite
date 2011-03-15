package com.calclab.emite.im.client.chat;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.xtesting.services.TigaseXMLService;

public class PairChatSelectionStrategyTests {

    private PairChatSelectionStrategy strategy;

    @Before
    public void beforeTests() {
        strategy = new PairChatSelectionStrategy();
    }

    @Test
    public void shouldAssignToSameIfResourceChanged() {
        ChatProperties chatProperties = new ChatProperties(uri("user@domain"));
        assertTrue(strategy.isAssignable(chatProperties, new ChatProperties(uri("user@domain"))));
        assertTrue(strategy.isAssignable(chatProperties, new ChatProperties(uri("user@domain/res1"))));
        assertTrue(strategy.isAssignable(chatProperties, new ChatProperties(uri("user@domain/res2"))));
    }

    @Test
    public void shouldExtractFromProperty() {
        Message message = new Message("body", uri("recipient@domain"), uri("sender@domain"));
        ChatProperties properties = strategy.extractProperties(message);
        assertNotNull(properties);
        assertEquals(uri("sender@domain"), properties.getUri());
    }

    @Test
    public void shouldInitiateCreationWhenMessageBody() {
        ChatProperties properties = strategy.extractProperties(new Message("body"));
        assertTrue(properties.shouldCreateNewChat());
    }

    /* Based on real facts ;) */
    @Test
    public void shouldNotInitiateCreationIfMessageHasInvitation() {
        IPacket stanza = TigaseXMLService.toPacket("<message to='test1@localhost' " + "from='room@conference.localhost' xmlns='jabber:client' "
                + "type='normal'><x xmlns='http://jabber.org/protocol/muc#user'>" + "<invite from='test1@localhost/emite-1291918896669'><reason />"
                + "</invite></x><x jid='room@conference.localhost' " + "xmlns='jabber:x:conference' />"
                + "<body>test1@localhost/emite-1291918896669 invites you to the room room@conference.localhost</body></message>");
        ChatProperties properties = strategy.extractProperties(new Message(stanza));
        assertFalse(properties.shouldCreateNewChat());
    }

    @Test
    public void shouldNotInitiateCreationWhenNotBody() {
        ChatProperties properties = strategy.extractProperties(new Message((String) null));
        assertFalse(properties.shouldCreateNewChat());
    }

}
