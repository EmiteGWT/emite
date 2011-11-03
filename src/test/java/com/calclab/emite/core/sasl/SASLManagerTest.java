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

package com.calclab.emite.core.sasl;

import static com.calclab.emite.core.XmppURI.uri;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.base.xml.XMLBuilder;
import com.calclab.emite.base.xml.XMLPacket;
import com.calclab.emite.core.XmppURI;
import com.calclab.emite.core.events.AuthorizationResultEvent;
import com.calclab.emite.core.session.Credentials;
import com.calclab.emite.core.session.sasl.SASLManager;
import com.calclab.emite.core.session.sasl.SASLManagerImpl;
import com.calclab.emite.xtesting.XmppConnectionTester;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class SASLManagerTest {
	private SASLManager manager;
	private XmppConnectionTester connection;
	protected AuthorizationResultEvent authEvent;

	@Before
	public void beforeTests() {
		connection = new XmppConnectionTester();
		manager = new SASLManagerImpl(new SimpleEventBus(), connection);
		authEvent = null;
		
		manager.addAuthorizationResultHandler(new AuthorizationResultEvent.Handler() {
			@Override
			public void onAuthorizationResult(final AuthorizationResultEvent event) {
				authEvent = event;
			}
		});
	}

	@Test
	public void shouldHandleSuccessWhenAuthorizationSent() {
		manager.sendAuthorizationRequest(credentials(uri("me@domain"), "password"));
		connection.receives("<success xmlns=\"urn:ietf:params:xml:ns:xmpp-sasl\"/>");
		assertNotNull(authEvent);
		assertTrue(authEvent.isSuccess());
	}

	@Test
	public void shouldHanonStanzadleFailure() {
		manager.sendAuthorizationRequest(credentials(uri("node@domain"), "password"));
		connection.receives("<failure xmlns=\"urn:ietf:params:xml:ns:xmpp-sasl\"><not-authorized/></failure>");
		assertNotNull(authEvent);
		assertFalse(authEvent.isSuccess());
	}

	@Test
	public void shouldSendAnonymousIfAnonymousProvided() {
		manager.sendAuthorizationRequest(Credentials.createAnonymous());
		final XMLPacket packet = XMLBuilder.create("auth", "urn:ietf:params:xml:ns:xmpp-sasl").attribute("mechanism", "ANONYMOUS").getXML();
		assertTrue(connection.hasSent(packet));
	}

	@Test
	public void shouldSendPlainAuthorizationUnlessAnonymous() {
		manager.sendAuthorizationRequest(credentials(uri("node@domain/resource"), "password"));
		final XMLPacket packet = XMLBuilder.create("auth", "urn:ietf:params:xml:ns:xmpp-sasl").attribute("mechanism", "PLAIN").getXML();
		assertTrue(connection.hasSent(packet));
	}

	@Test
	public void shouldSendPlainAuthorizationWithoutNode() {
		manager.sendAuthorizationRequest(credentials(uri("domain/resource"), ""));
		final XMLPacket packet = XMLBuilder.create("auth", "urn:ietf:params:xml:ns:xmpp-sasl").attribute("mechanism", "PLAIN").getXML();
		assertTrue(connection.hasSent(packet));
	}

	private Credentials credentials(final XmppURI uri, final String password) {
		final Credentials credentials = new Credentials(uri, password);
		return credentials;
	}
}
