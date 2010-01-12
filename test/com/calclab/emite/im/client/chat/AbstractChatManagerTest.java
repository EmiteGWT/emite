package com.calclab.emite.im.client.chat;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xtesting.SessionTester;
import com.calclab.suco.testing.events.MockedListener;

public abstract class AbstractChatManagerTest {
    protected static final XmppURI MYSELF = uri("self@domain");
    protected static final XmppURI OTHER = uri("other@domain");
    protected PairChatManager manager;
    protected SessionTester session;

    @Before
    public void beforeTests() {
	session = new SessionTester();
	manager = createChatManager();
	session.login(MYSELF, null);
    }

    @Test
    public void shouldBeInitiatedByMeIfIOpenAChat() {
	final Chat chat = manager.open(uri("other@domain/resource"));
	assertTrue(chat.isInitiatedByMe());
    }

    @Test
    public void shouldEventWhenAChatIsClosed() {
	final Chat chat = manager.open(uri("other@domain/resource"));
	final MockedListener<Chat> listener = new MockedListener<Chat>();
	manager.onChatClosed(listener);
	manager.close(chat);
	assertTrue(listener.isCalledOnce());
    }

    @Test
    public void shouldEventWhenChatCreated() {
	final MockedListener<Chat> listener = new MockedListener<Chat>();
	manager.onChatCreated(listener);
	manager.open(OTHER);
	assertTrue(listener.isCalledOnce());
    }

    protected abstract PairChatManager createChatManager();
}
