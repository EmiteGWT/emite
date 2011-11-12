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

import com.calclab.emite.base.xml.XMLPacket;
import com.calclab.emite.core.XmppURI;

/**
 * A Presence stanza
 */
public class Presence extends Stanza {

	/**
	 * 
	 * 2.2.2.1. Show
	 * 
	 * <p>
	 * If 'show' element is notSpecified, the entity is assumed to be online and
	 * available.
	 * </p>
	 * 
	 * <p>
	 * If provided, the XML character data value MUST be one of the following
	 * (additional availability types could be defined through a
	 * properly-namespaced child element of the presence stanza):
	 * </p>
	 * 
	 * @see http://xmpp.org/rfcs/rfc3920.html
	 * @see http://www.xmpp.org/rfcs/rfc3921.html#stanzas
	 */
	public static enum Show {
		/** The entity or resource is temporarily away */
		away,
		/** The entity or resource is actively interested in chatting */
		chat,
		/** The entity or resource is busy (dnd = "Do Not Disturb") */
		dnd,
		/**
		 * The entity or resource is away for an extended period (xa =
		 * "eXtended Away")
		 */
		xa,
		/**
		 * If 'show' element is notSpecified, the entity is assumed to be online
		 * and available
		 */
		notSpecified,
		/**
		 * Unknown
		 */
		unknown
	}

	/**
	 * 2.2.1. Types of Presence
	 * 
	 * <p>
	 * The 'type' attribute of a presence stanza is OPTIONAL. A presence stanza
	 * that does not possess a 'type' attribute is used to signal to the server
	 * that the sender is online and available for communication. If included,
	 * the 'type' attribute specifies a lack of availability, a request to
	 * manage a subscription to another entity's presence, a request for another
	 * entity's current presence, or an error related to a previously-sent
	 * presence stanza. If included, the 'type' attribute MUST have one of the
	 * following values:
	 * </p>
	 */
	public static enum Type {
		/**
		 * error -- An error has occurred regarding processing or delivery of a
		 * previously-sent presence stanza.
		 */
		error,
		/**
		 * probe -- A request for an entity's current presence; SHOULD be
		 * generated only by a server on behalf of a user.
		 */
		probe,
		/**
		 * subscribe -- The sender wishes to subscribe to the recipient's
		 * presence.
		 */
		subscribe,
		/**
		 * subscribed -- The sender has allowed the recipient to receive their
		 * presence.
		 */
		subscribed,
		/**
		 * unavailable -- Events that the entity is no longer available for
		 * communication.
		 */
		unavailable,
		/**
		 * unsubscribe -- The sender is unsubscribing from another entity's
		 * presence.
		 */
		unsubscribe,
		/**
		 * unsubscribed -- The subscription request has been denied or a
		 * previously-granted subscription has been cancelled.
		 */
		unsubscribed
	}

	public Presence(final XMLPacket stanza) {
		super(stanza);
	}

	public Presence() {
		super("presence");
	}

	public Presence(final Type type) {
		this();
		setType(type);
	}

	public Presence(final Type type, final XmppURI to) {
		this(type);
		setTo(to);
	}

	/**
	 * Get the priority of the presence
	 * 
	 * @return The priority (1-10), 0 if not specified
	 */
	public int getPriority() {
		int value = 0;
		final String priority = xml.getChildText("priority");
		if (priority != null) {
			try {
				value = Integer.parseInt(priority);
			} catch (final NumberFormatException e) {
				value = 0;
			}
		}
		return value;
	}

	/**
	 * Return the show of the presence
	 * 
	 * @return The show, never null
	 */
	public Show getShow() {
		final String value = xml.getChildText("show");
		try {
			return value != null ? Show.valueOf(value) : Show.notSpecified;
		} catch (final IllegalArgumentException e) {
			return Show.unknown;
		}
	}

	/**
	 * Return the status of the presence.
	 * 
	 * @return The status, null if not specified
	 */
	public String getStatus() {
		return xml.getChildText("status");
	}

	/**
	 * Get the presence's type. If null returned, available (status) is supposed
	 * 
	 * @return The type, can return null (means available)
	 * @see http://www.xmpp.org/rfcs/rfc3921.html#presence
	 */
	public Type getType() {
		final String type = xml.getAttribute("type");
		try {
			return type != null ? Type.valueOf(type) : null;
		} catch (final IllegalArgumentException e) {
			return Type.error;
		}
	}

	public void setPriority(final int value) {
		xml.setChildText("priority", String.valueOf(value >= 0 ? value : 0));
	}

	public void setShow(final Show value) {
		xml.setChildText("show", value != null && value != Show.notSpecified && value != Show.unknown ? value.toString() : null);
	}

	public void setStatus(final String statusMessage) {
		xml.setChildText("status", statusMessage);
	}

	public void setType(final Type type) {
		xml.setAttribute("type", type != null ? type.toString() : null);
	}

}
