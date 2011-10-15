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

import static com.calclab.emite.core.client.uri.XmppURI.uri;

import java.util.Date;

import org.junit.Test;

import com.calclab.emite.core.client.uri.XmppURI;
import com.calclab.emite.core.client.xml.XMLBuilder;
import com.calclab.emite.core.client.xml.XMLPacket;
import com.google.gwt.junit.client.GWTTestCase;

public class DelayGwtTest extends GWTTestCase {

	@Override
	public String getModuleName() {
		return "com.calclab.emite.xep.delay.EmiteDelay";
	}

	@Test
	public void testShouldCalculateDelay() {

		final XmppURI uri = uri("name@domain/resource");
		final XMLPacket delayNode = XMLBuilder.create("delay", "urn:xmpp:delay").getXML();
		delayNode.setAttribute("from", "name@domain/resource");
		delayNode.setAttribute("stamp", "1980-04-15T17:15:02.159+01:00");
		final Delay delay = new Delay(delayNode);
		assertNotNull(delay);
		final Date date = new Date(324663302159L);
		assertEquals(uri, delay.getFrom());
		assertEquals(date, delay.getStamp());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testShouldCalculateDelayLegacyFormat() {

		final XmppURI uri = uri("name@domain/resource");
		final XMLPacket delayNode = XMLBuilder.create("x", "jabber:x:delay").getXML();
		delayNode.setAttribute("xmlns", "jabber:x:delay");
		delayNode.setAttribute("from", "name@domain/resource");
		delayNode.setAttribute("stamp", "19800415T17:15:02");
		final Delay delay = new Delay(delayNode);
		assertNotNull(delay);

		final Date date = new Date(80, 3, 15, 17, 15, 2);
		date.setTime(date.getTime() - (date.getTimezoneOffset() * 60000));
		
		assertEquals(uri, delay.getFrom());
		assertEquals(date, delay.getStamp());
	}

}
