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

import javax.annotation.Nullable;

import com.calclab.emite.base.xml.XMLPacket;

/**
 * A Message stanza.
 * 
 * @see <a href="http://xmpp.org/rfcs/rfc6121.html#message-syntax">RFC 6121 - Section 5.2</a>
 */
public class Message extends Stanza {

	/**
	 * Possible <i>type</i> values for messages.
	 * 
	 * @see <a href="http://xmpp.org/rfcs/rfc6121.html#message-syntax-type">RFC 6121 - Section 5.2.2</a>
	 */
	public static enum Type {
		chat, error, groupchat, headline, normal;
	}

	/**
	 * Creates a new message from a XML packet.
	 * 
	 * No checks are done to the packet, so it's only meant for internal use.
	 * 
	 * @param xml the XML packet for this message
	 */
	public Message(final XMLPacket xml) {
		super(xml);
	}

	/**
	 * Create a new message.
	 */
	public Message() {
		super("message");
	}

	/**
	 * Create a new message with the given body.
	 * 
	 * @param body the body for the new message
	 */
	public Message(final String body) {
		this();
		setBody(body);
	}
	
	/**
	 * Returns the <i>type</i> attribute for this message.
	 * 
	 * If type is not set or is not valid, {@link Type#normal} will be returned.
	 * 
	 * @return the type for this message
	 */
	public final Type getType() {
		try {
			return Type.valueOf(xml.getAttribute("type"));
		} catch (final Exception e) {
			return Type.normal;
		}
	}

	/**
	 * Sets a new <i>type</i> attribute for this message.
	 * 
	 * @param type the new type for this message
	 */
	public final void setType(@Nullable final Type type) {
		xml.setAttribute("type", type != null ? type.toString() : null);
	}

	/**
	 * Returns the <i>body</i> attribute for this stanza.
	 * 
	 * @return the body for this stanza, or {@code null} if none
	 */
	@Nullable
	public final String getBody() {
		return xml.getChildText("body");
	}

	/**
	 * Sets a new <i>body</i> attribute for this stanza.
	 * 
	 * @param body the new body for this stanza
	 */
	public final void setBody(@Nullable final String body) {
		xml.setChildText("body", body);
	}

	/**
	 * Returns the <i>subject</i> attribute for this stanza.
	 * 
	 * @return the subject for this stanza, or {@code null} if none
	 */
	@Nullable
	public final String getSubject() {
		return xml.getChildText("subject");
	}

	/**
	 * Sets a new <i>subject</i> attribute for this stanza.
	 * 
	 * @param subject the new subject for this stanza
	 */
	public final void setSubject(@Nullable final String subject) {
		xml.setChildText("subject", subject);
	}

	/**
	 * Returns the <i>thread</i> attribute for this stanza.
	 * 
	 * @return the thread for this stanza, or {@code null} if none
	 */
	@Nullable
	public final String getThread() {
		return xml.getChildText("thread");
	}

	/**
	 * Sets a new <i>thread</i> attribute for this stanza.
	 * 
	 * @param thread the new thread for this stanza
	 */
	public final void setThread(@Nullable final String thread) {
		xml.setChildText("thread", thread);
	}

}
