package com.calclab.emite.im.client.chat;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xtesting.XmppSessionTester;
import com.calclab.emite.xtesting.handlers.ChatChangedTestHandler;

public abstract class AbstractChatManagerTest {
	protected static final XmppURI MYSELF = uri("self@domain");
	protected static final XmppURI OTHER = uri("other@domain");
	protected ChatManager manager;
	protected XmppSessionTester session;

	@Before
	public void beforeTests() {
		session = new XmppSessionTester();
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
		final ChatChangedTestHandler handler = new ChatChangedTestHandler("closed");
		manager.addChatChangedHandler(handler);
		manager.close(chat);
		assertTrue(handler.isCalledOnce());
	}

	@Test
	public void shouldEventWhenChatCreated() {
		final ChatChangedTestHandler handler = new ChatChangedTestHandler("created");
		manager.addChatChangedHandler(handler);
		manager.open(OTHER);
		assertTrue(handler.isCalledOnce());
	}

	protected abstract ChatManager createChatManager();
}
