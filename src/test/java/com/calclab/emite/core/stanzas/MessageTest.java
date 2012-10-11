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

package com.calclab.emite.core.stanzas;

import static com.calclab.emite.core.XmppURI.uri;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.calclab.emite.base.xml.XMLBuilder;
import com.calclab.emite.core.stanzas.Message;
import com.calclab.emite.core.stanzas.Message.Type;

public class MessageTest {
	@Test
	public void shouldAddSubject() {
		final Message message = new Message("message", uri("user2@domain/r"), uri("user1@domain/r"));
		message.setSubject("the subject");
		assertEquals("the subject", message.getSubject());
	}

	@Test
	public void shouldNotAddBodyIfNotSpecified() {
		final Message message = new Message();
		assertNull(message.getBody());
		assertEquals(0, message.getXML().getChildren("body").size());
		final Message message2 = new Message(null, uri("other@domain"), uri("me@domain"));
		assertEquals(0, message2.getXML().getChildren("body").size());
	}

	@Test
	public void shouldRetrieveSubject() {
		final Message message = new Message(XMLBuilder.create("message").childText("subject", "the subject").getXML());
		assertEquals("the subject", message.getSubject());
	}

	@Test
	public void shouldReturnNullThread() {
		final Message message = new Message("message", uri("user2@domain/r"), uri("user1@domain/r"));
		assertEquals(null, message.getThread());
	}

	@Test
	public void shouldReturnUnkownType() {
		final Message message = new Message(XMLBuilder.create("message").attribute("type", "invalid-here").getXML());
		assertSame(Type.normal, message.getType());
	}

	@Test
	public void shouldTypeNotSpecifiedType() {
		final Message message = new Message(XMLBuilder.create("message").getXML());
		final Type type = message.getType();
		assertSame(Type.normal, type);
	}
}
