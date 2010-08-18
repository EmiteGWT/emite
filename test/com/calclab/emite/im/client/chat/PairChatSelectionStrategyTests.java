package com.calclab.emite.im.client.chat;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.xmpp.stanzas.Message;

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

    @Test
    public void shouldNotInitiateCreationWhenNotBody() {
	ChatProperties properties = strategy.extractProperties(new Message((String) null));
	assertFalse(properties.shouldCreateNewChat());
    }

}
