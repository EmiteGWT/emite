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

package com.calclab.emite.core.client.stanzas;

import com.calclab.emite.core.client.xml.XMLPacket;

public class Message extends Stanza {

	public static enum Type {
		chat, error, groupchat, headlines, normal
	}

	/**
	 * Create a message by delegation. Not for normal use
	 * 
	 * @param iPacket
	 */
	public Message(final XMLPacket xml) {
		super(xml);
	}

	public Message() {
		super("message");
	}

	public Message(final String body) {
		this();
		setBody(body);
	}

	public String getBody() {
		return xml.getChildText("body");
	}

	public void setBody(final String msg) {
		xml.setChildText("body", msg);
	}

	public String getSubject() {
		return xml.getChildText("subject");
	}

	public void setSubject(final String subject) {
		xml.setChildText("subject", subject);
	}

	public String getThread() {
		return xml.getChildText("thread");
	}

	public void setThread(final String thread) {
		xml.setChildText("thread", thread);
	}

	/**
	 * An IM application SHOULD support all of the foregoing message types; if
	 * an application receives a message with no 'type' attribute or the
	 * application does not understand the value of the 'type' attribute
	 * provided, it MUST consider the message to be of type "normal" (i.e.,
	 * "normal" is the default). The "error" type MUST be generated only in
	 * response to an error related to a message received from another entity.
	 * 
	 * @see http://www.xmpp.org/rfcs/rfc3921.html#stanzas-message-type
	 * @return
	 */
	public Type getType() {
		final String type = xml.getAttribute("type");
		try {
			return type != null ? Type.valueOf(type) : Type.normal;
		} catch (final IllegalArgumentException e) {
			return Type.normal;
		}
	}

	public void setType(final Type type) {
		xml.setAttribute("type", type != null ? type.toString() : null);
	}

}
