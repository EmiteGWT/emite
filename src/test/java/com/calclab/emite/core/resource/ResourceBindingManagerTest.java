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

package com.calclab.emite.core.resource;

import static com.calclab.emite.core.XmppURI.uri;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.session.resource.ResourceBindResultEvent;
import com.calclab.emite.core.session.resource.ResourceBindingManager;
import com.calclab.emite.core.session.resource.ResourceBindingManagerImpl;
import com.calclab.emite.core.stanzas.IQ;
import com.calclab.emite.xtesting.XmppConnectionTester;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class ResourceBindingManagerTest {
	
	private ResourceBindingManager manager;
	private XmppConnectionTester connection;
	private ResourceBindResultEvent currentEvent;

	@Before
	public void beforeTests() {
		connection = new XmppConnectionTester();
		manager = new ResourceBindingManagerImpl(new SimpleEventBus(), connection);
	}

	@Test
	public void shouldEventIfBindedSucceed() {
		currentEvent = null;
		manager.addResourceBindResultHandler(new DiscoveryItemsCallback.Handler() {
			@Override
			public void onResourceBindResult(ResourceBindResultEvent event) {
				currentEvent = event;
			}
		});

		connection.receives("<iq type='result' id='bind-resource'>" + "<bind xmlns='urn:ietf:params:xml:ns:xmpp-bind'>"
				+ "<jid>somenode@example.com/someresource</jid></bind></iq>");

		assertNotNull(currentEvent);
		assertEquals(uri("somenode@example.com/someresource"), currentEvent.getUri());
	}

	@Test
	public void shouldPerformBinding() {
		manager.bindResource("resource");
		assertTrue(connection.hasSent(new IQ(IQ.Type.set).Includes("bind", "urn:ietf:params:xml:ns:xmpp-bind")));
	}
}
