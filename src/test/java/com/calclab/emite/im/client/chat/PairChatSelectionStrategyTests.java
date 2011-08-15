/*
 * ((e)) emite: A pure Google Web Toolkit XMPP library
 * Copyright (c) 2008-2011 The Emite development team
 * 
 * This file is part of Emite.
 *
 * Emite is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * Emite is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with Emite.  If not, see <http://www.gnu.org/licenses/>.
 */

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
		final ChatProperties chatProperties = new ChatProperties(uri("user@domain"));
		assertTrue(strategy.isAssignable(chatProperties, new ChatProperties(uri("user@domain"))));
		assertTrue(strategy.isAssignable(chatProperties, new ChatProperties(uri("user@domain/res1"))));
		assertTrue(strategy.isAssignable(chatProperties, new ChatProperties(uri("user@domain/res2"))));
	}

	@Test
	public void shouldExtractFromProperty() {
		final Message message = new Message("body", uri("recipient@domain"), uri("sender@domain"));
		final ChatProperties properties = strategy.extractProperties(message);
		assertNotNull(properties);
		assertEquals(uri("sender@domain"), properties.getUri());
	}

	@Test
	public void shouldInitiateCreationWhenMessageBody() {
		final ChatProperties properties = strategy.extractProperties(new Message("body"));
		assertTrue(properties.shouldCreateNewChat());
	}

	/* Based on real facts ;) */
	@Test
	public void shouldNotInitiateCreationIfMessageHasInvitation() {
		final IPacket stanza = TigaseXMLService.toPacket("<message to='test1@localhost' " + "from='room@conference.localhost' xmlns='jabber:client' "
				+ "type='normal'><x xmlns='http://jabber.org/protocol/muc#user'>" + "<invite from='test1@localhost/emite-1291918896669'><reason />"
				+ "</invite></x><x jid='room@conference.localhost' " + "xmlns='jabber:x:conference' />"
				+ "<body>test1@localhost/emite-1291918896669 invites you to the room room@conference.localhost</body></message>");
		final ChatProperties properties = strategy.extractProperties(new Message(stanza));
		assertFalse(properties.shouldCreateNewChat());
	}

	@Test
	public void shouldNotInitiateCreationWhenNotBody() {
		final ChatProperties properties = strategy.extractProperties(new Message((String) null));
		assertFalse(properties.shouldCreateNewChat());
	}

}
