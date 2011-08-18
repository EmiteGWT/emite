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

package com.calclab.emite.xep.delay.client;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;

import java.util.Date;

import org.junit.Test;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.NoPacket;
import com.calclab.emite.core.client.xmpp.stanzas.BasicStanza;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.google.gwt.junit.client.GWTTestCase;

public class DelayManagerImplGwtTest extends GWTTestCase {

	@Override
	public String getModuleName() {
		return "com.calclab.emite.xep.delay.EmiteDelay";
	}

	@Test
	public void testShouldGiveDelay() {
		final BasicStanza stanza = new BasicStanza("name", "xmlns");

		final XmppURI uri = uri("name@domain/resource");
		stanza.setTo(uri);
		assertEquals("name@domain/resource", stanza.getToAsString());
		stanza.setTo((XmppURI) null);
		final IPacket delayNode = stanza.addChild("delay");
		delayNode.setAttribute("xmlns", "urn:xmpp:delay");
		delayNode.setAttribute("from", "name@domain/resource");
		delayNode.setAttribute("stamp", "1980-04-15T17:15:02.159+01:00");
		final Delay delay = DelayHelper.getDelay(stanza);
		assertNotNull(delay);
		
		final Date date = new Date(324663302159L);
		assertEquals(uri, delay.getFrom());
		assertEquals(date, delay.getStamp());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testShouldGiveDelayLegacyFormat() {
		final BasicStanza stanza = new BasicStanza("name", "xmlns");

		final XmppURI uri = uri("name@domain/resource");
		stanza.setTo(uri);
		assertEquals("name@domain/resource", stanza.getToAsString());
		stanza.setTo((XmppURI) null);
		final IPacket delayNode = stanza.addChild("x");
		delayNode.setAttribute("xmlns", "jabber:x:delay");
		delayNode.setAttribute("from", "name@domain/resource");
		delayNode.setAttribute("stamp", "19800415T15:15:02");
		final Delay delay = DelayHelper.getDelay(stanza);
		assertNotNull(delay);

		final Date date = new Date(80, 3, 15, 15, 15, 2);
		date.setTime(date.getTime() - (date.getTimezoneOffset() * 60000));
		
		assertEquals(uri, delay.getFrom());
		assertEquals(date, delay.getStamp());
	}

	@Test
	public void testShouldSetTextToChild() {
		final BasicStanza stanza = new BasicStanza("name", "xmlns");
		stanza.setTextToChild("child", "value");
		assertEquals("value", stanza.getFirstChild("child").getText());
		stanza.setTextToChild("child", null);
		assertSame(NoPacket.INSTANCE, stanza.getFirstChild("child"));
	}

	@Test
	public void testShouldSetTo() {
		final BasicStanza stanza = new BasicStanza("name", "xmlns");

		stanza.setTo(uri("name@domain/resource"));
		assertEquals("name@domain/resource", stanza.getToAsString());
		stanza.setTo((XmppURI) null);
		assertNull(stanza.getToAsString());
	}
}
