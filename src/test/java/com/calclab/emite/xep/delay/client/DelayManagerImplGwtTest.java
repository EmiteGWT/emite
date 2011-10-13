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

import static com.calclab.emite.core.client.stanzas.XmppURI.uri;

import java.util.Date;

import org.junit.Test;

import com.calclab.emite.core.client.stanzas.Stanza;
import com.calclab.emite.core.client.stanzas.XmppURI;
import com.calclab.emite.core.client.xml.XMLPacket;
import com.google.gwt.junit.client.GWTTestCase;

public class DelayManagerImplGwtTest extends GWTTestCase {

	@Override
	public String getModuleName() {
		return "com.calclab.emite.xep.delay.EmiteDelay";
	}

	@Test
	public void testShouldGiveDelay() {
		final Stanza stanza = new Stanza("name", "xmlns");

		final XmppURI uri = uri("name@domain/resource");
		stanza.setTo(uri);
		assertEquals("name@domain/resource", stanza.getTo().toString());
		stanza.setTo((XmppURI) null);
		final XMLPacket delayNode = stanza.getXML().addChild("delay");
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
		final Stanza stanza = new Stanza("name", "xmlns");

		final XmppURI uri = uri("name@domain/resource");
		stanza.setTo(uri);
		assertEquals("name@domain/resource", stanza.getTo().toString());
		stanza.setTo(null);
		final XMLPacket delayNode = stanza.getXML().addChild("x");
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
		final Stanza stanza = new Stanza("name", "xmlns");
		stanza.getXML().setChildText("child", "value");
		assertEquals("value", stanza.getXML().getChildText("child"));
		stanza.getXML().setChildText("child", null);
		assertNull(stanza.getXML().getFirstChild("child"));
	}

	@Test
	public void testShouldSetTo() {
		final Stanza stanza = new Stanza("name", "xmlns");

		stanza.setTo(uri("name@domain/resource"));
		assertEquals("name@domain/resource", stanza.getTo().toString());
		stanza.setTo(null);
		assertNull(stanza.getTo());
	}
}
