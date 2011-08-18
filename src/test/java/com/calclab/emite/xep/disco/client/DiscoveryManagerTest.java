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

package com.calclab.emite.xep.disco.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xtesting.XmppSessionTester;
import com.calclab.emite.xtesting.handlers.DiscoveryInfoResultTestHandler;
import com.calclab.emite.xtesting.handlers.DiscoveryItemsResultTestHandler;

public class DiscoveryManagerTest {

	private DiscoveryManager manager;
	private XmppSessionTester session;

	private static final String DISCO_RESULT = "<iq type='result' from='plays.shakespeare.lit' to='romeo@montague.net/orchard'"
			+ " id='info1'><query xmlns='http://jabber.org/protocol/disco#info'><identity category='conference'"
			+ "	type='text' name='Play-Specific Chatrooms'/><identity category='directory'"
			+ "	type='chatroom' name='Play-Specific Chatrooms'/><feature var='http://jabber.org/protocol/disco#info'/>"
			+ "<feature var='http://jabber.org/protocol/disco#items'/><feature var='http://jabber.org/protocol/muc'/>"
			+ "<feature var='jabber:iq:register'/><feature var='jabber:iq:search'/><feature var='jabber:iq:time'/>"
			+ "<feature var='jabber:iq:version'/></query></iq>";

	private static final String DISCO_ITEMS_RESULT = "<iq type='result'" + "    from='shakespeare.lit'" + "    to='romeo@montague.net/orchard'"
			+ "    id='items1'>" + "  <query xmlns='http://jabber.org/protocol/disco#items'>" + "    <item jid='people.shakespeare.lit'"
			+ "          name='Directory of Characters'/>" + "    <item jid='plays.shakespeare.lit'" + "          name='Play-Specific Chatrooms'/>"
			+ "    <item jid='mim.shakespeare.lit'" + "          name='Gateway to Marlowe IM'/>" + "    <item jid='words.shakespeare.lit'"
			+ "          name='Shakespearean Lexicon'/>" + "    <item jid='globe.shakespeare.lit'" + "          name='Calendar of Performances'/>"
			+ "    <item jid='headlines.shakespeare.lit'" + "          name='Latest Shakespearean News'/>" + "    <item jid='catalog.shakespeare.lit'"
			+ "          name='Buy Shakespeare Stuff!'/>" + "    <item jid='en2fr.shakespeare.lit'" + "          name='French Translation Service'/>"
			+ "  </query>" + "</iq>";

	@Before
	public void beforeTests() {
		session = new XmppSessionTester();
		manager = new DiscoveryManagerImpl(session);
	}

	@Test
	public void shouldSendInfoQuery() {
		final XmppURI uri = XmppURI.uri("node", "localhost.localdomain", "resource");
		final DiscoveryInfoResultTestHandler handler = new DiscoveryInfoResultTestHandler();
		manager.sendInfoQuery(uri, handler);
		session.answer(DISCO_RESULT);
		assertTrue(handler.isCalledOnce());
		assertTrue(handler.getLastEvent().hasResult());
		final DiscoveryInfoResults result = handler.getLastEvent().getResults();
		assertEquals(2, result.getIdentities().size());
		assertEquals(7, result.getFeatures().size());

	}

	@Test
	public void shouldSendItemsQuery() {
		final XmppURI uri = XmppURI.uri("node", "localhost.localdomain", "resource");
		final DiscoveryItemsResultTestHandler handler = new DiscoveryItemsResultTestHandler();
		manager.sendItemsQuery(uri, handler);
		session.answer(DISCO_ITEMS_RESULT);
		assertTrue(handler.isCalledOnce());
		assertTrue(handler.getLastEvent().hasResult());
		final DiscoveryItemsResults result = handler.getLastEvent().getResults();
		assertEquals(8, result.getItems().size());
	}
}
