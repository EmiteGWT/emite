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

import static com.google.common.base.Preconditions.checkArgument;

import javax.annotation.Nullable;

import com.calclab.emite.base.xml.XMLPacket;
import com.calclab.emite.core.XmppURI;

/**
 * A Presence stanza.
 * 
 * @see <a href="http://xmpp.org/rfcs/rfc6121.html#presence-syntax">RFC 6121 - Section 4.7</a>
 */
public class Presence extends Stanza {

	/**
	 * Possible <i>type</i> values for messages.
	 * 
	 * @see <a href="http://xmpp.org/rfcs/rfc6121.html#presence-syntax-type">RFC 6121 - Section 4.7.1</a>
	 */
	public static enum Type {
		error, probe, subscribe, subscribed, unavailable, unsubscribe, unsubscribed;
	}
	
	/**
	 * Possible <i>show</i> values for messages.
	 * 
	 * @see <a href="http://xmpp.org/rfcs/rfc6121.html#presence-syntax-children-show">RFC 6121 - Section 4.7.2.1</a>
	 */
	public static enum Show {
		away, chat, dnd, xa;
	}

	/**
	 * Creates a new presence from a XML packet.
	 * 
	 * No checks are done to the packet, so it's only meant for internal use.
	 * 
	 * @param xml the XML packet for this presence
	 */
	public Presence(final XMLPacket xml) {
		super(xml);
	}

	/**
	 * Create a new presence.
	 */
	public Presence() {
		super("presence");
	}

	/**
	 * Create a new presence with the given type.
	 * 
	 * @param type the type for the new presence
	 */
	public Presence(@Nullable final Type type) {
		this();
		setType(type);
	}

	/**
	 * Create a new presence with the given type and recipient.
	 * 
	 * @param type the type for the new presence
	 * @param to the recipient for the new presence
	 */
	public Presence(@Nullable final Type type, final XmppURI to) {
		this(type);
		setTo(to);
	}
	
	/**
	 * Returns the <i>type</i> attribute for this presence.
	 * 
	 * @return the type for this presence, or {@code null} if available
	 */
	@Nullable
	public final Type getType() {
		try {
			return Type.valueOf(xml.getAttribute("type"));
		} catch (final Exception e) {
			return null;
		}
	}
	
	/**
	 * Sets a new <i>type</i> attribute for this presence.
	 * 
	 * @param type the new type for this presence
	 */
	public final void setType(@Nullable final Type type) {
		xml.setAttribute("type", type != null ? type.toString() : null);
	}

	/**
	 * Returns the <i>show</i> value for this presence.
	 * 
	 * @return the show for this presence, or {@code null} if none
	 */
	@Nullable
	public final Show getShow() {
		try {
			return Show.valueOf(xml.getChildText("show"));
		} catch (final Exception e) {
			return null;
		}
	}

	/**
	 * Sets a new <i>show</i> value for this presence.
	 * 
	 * @param show the new show for this presence
	 */
	public final void setShow(@Nullable final Show show) {
		xml.setChildText("show", show != null ? show.toString() : null);
	}
	
	/**
	 * Returns the <i>status</i> value for this presence.
	 * 
	 * @return the status for this presence, or {@code null} if none
	 */
	@Nullable
	public final String getStatus() {
		return xml.getChildText("status");
	}

	/**
	 * Sets a new <i>status</i> value for this presence.
	 * 
	 * @param status the new status for this presence
	 */
	public final void setStatus(@Nullable final String status) {
		xml.setChildText("status", status);
	}
	
	/**
	 * Returns the <i>priority</i> value for this presence.
	 * 
	 * @return the priority for this presence
	 */
	public final int getPriority() {
		try {
			return Integer.parseInt(xml.getChildText("priority"));
		} catch (final Exception e) {
			return 0;
		}
	}

	/**
	 * Sets a new <i>priority</i> value for this presence.
	 * 
	 * The priority must be ranged between -128 and 127.
	 * 
	 * @param priority the new priority for this presence
	 */
	public final void setPriority(final int priority) {
		checkArgument(priority >= -128 && priority <= 127, "Priority is out of range");
		xml.setChildText("priority", String.valueOf(priority >= 0 ? priority : 0));
	}

}
