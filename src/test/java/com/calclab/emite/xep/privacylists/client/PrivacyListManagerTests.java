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

package com.calclab.emite.xep.privacylists.client;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xtesting.XmppSessionTester;

public class PrivacyListManagerTests {

	private XmppSessionTester session;
	private PrivacyListsManager manager;

	@Before
	public void beforeTests() {
		session = new XmppSessionTester();
		manager = new PrivacyListsManager(session);
	}

	@Test
	public void shouldBlockUserBasedOnJID() {
		manager.blockUserBasedOnJID("myList", XmppURI.uri("name@domain/resource"), 7);
		session.verifyIQSent("<iq type='set'><query xmlns='jabber:iq:privacy'><list name='myList'>"
				+ "<item type='jid' value='name@domain' action='deny' order='7'></item></list></query></iq>");
	}
}
