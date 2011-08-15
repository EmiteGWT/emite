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

package com.calclab.emite.xep.muc.client;

import com.calclab.emite.core.client.xmpp.stanzas.HasJID;
import com.calclab.emite.core.client.xmpp.stanzas.Presence.Show;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;

/**
 * A room occupant: each occupant in a room is identified as
 * <room@service/nick>, where "nick" is the room nickname of the occupant as
 * specified on entering the room or subsequently changed during the occupant's
 * visit.
 * 
 * @see http://xmpp.org/extensions/xep-0045.html
 */
public class Occupant implements HasJID {

	public static enum Affiliation {
		admin, member, none, owner
	}

	public static enum Role {
		moderator, participant, unknown, visitor
	}

	private Affiliation affiliation;
	private Role role;
	private final XmppURI occupantUri;
	private Show show;
	private String statusMessage;
	private final XmppURI userUri;

	public Occupant(final XmppURI userUri, final XmppURI occupantUri, final String affiliation, final String role, final Show show, final String statusMessage) {
		assert occupantUri != null : "Occupant uri can't be null in occupant";
		this.userUri = userUri;
		this.occupantUri = occupantUri;
		setAffiliation(affiliation);
		setRole(role);
		setShow(show);
		setStatusMessage(statusMessage);
	}

	/**
	 * Gets the affiliation of this occupant
	 * 
	 * @return
	 */
	public Affiliation getAffiliation() {
		return affiliation;
	}

	/**
	 * Get the nick of this occupant
	 * 
	 * @return
	 */
	public String getNick() {
		return occupantUri.getResource();
	}

	/**
	 * Get the occupant uri (the room jid and the nick as resource)
	 * 
	 * @return
	 */
	public XmppURI getOccupantUri() {
		return occupantUri;
	}

	public Role getRole() {
		return role;
	}

	public Show getShow() {
		return show;
	}

	/**
	 * Get the occupant status message
	 * 
	 * @return
	 */
	public String getStatusMessage() {
		return statusMessage;
	}

	/**
	 * Gets the user uri associated to this occupant
	 * 
	 * @return
	 */
	public XmppURI getUserUri() {
		return userUri;
	}

	public void setAffiliation(final String affiliation) {
		try {
			this.affiliation = Affiliation.valueOf(affiliation);
		} catch (final IllegalArgumentException e) {
			this.affiliation = Affiliation.none;
		} catch (final NullPointerException e) {
			this.affiliation = Affiliation.none;
		}
	}

	public void setRole(final String role) {
		try {
			this.role = Role.valueOf(role);
		} catch (final IllegalArgumentException e) {
			this.role = Role.unknown;
		} catch (final NullPointerException e) {
			this.role = Role.unknown;
		}
	}

	public void setShow(final Show show) {
		this.show = show;
	}

	public void setShow(final String show) {
		try {
			this.show = Show.valueOf(show);
		} catch (final IllegalArgumentException e) {
			this.show = Show.unknown;
		} catch (final NullPointerException e) {
			this.show = Show.unknown;
		}
	}

	public void setStatusMessage(final String statusMessage) {
		this.statusMessage = statusMessage;
	}

	@Override
	public String toString() {
		return occupantUri.toString() + "(" + affiliation + "," + role + "," + show + "," + statusMessage + ")";
	}

	/**
	 * {@inheritDoc}. In this case this method will return the user's jid.
	 */
	@Override
	public XmppURI getJID() {
		return getUserUri().getJID();
	}
}
