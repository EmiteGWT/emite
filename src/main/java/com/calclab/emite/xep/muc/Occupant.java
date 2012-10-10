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

package com.calclab.emite.xep.muc;

import javax.annotation.Nullable;

import com.calclab.emite.core.XmppURI;
import com.calclab.emite.core.stanzas.Presence;

/**
 * A room occupant: each occupant in a room is identified as
 * <room@service/nick>, where "nick" is the room nickname of the occupant as
 * specified on entering the room or subsequently changed during the occupant's
 * visit.
 * 
 * @see http://xmpp.org/extensions/xep-0045.html
 */
public final class Occupant {

	public static enum Affiliation {
		admin, member, none, owner
	}

	public static enum Role {
		moderator, participant, unknown, visitor
	}

	private final XmppURI occupantUri;
	private final XmppURI userUri;
	
	private Affiliation affiliation;
	private Role role;
	private Presence.Show show;
	@Nullable private String statusMessage;

	public Occupant(final XmppURI occupantUri, final XmppURI userUri, final String affiliation, final String role, final Presence.Show show, final String statusMessage) {
		this.userUri = userUri;
		this.occupantUri = occupantUri;
		
		setAffiliation(affiliation);
		setRole(role);
		setShow(show);
		this.statusMessage = statusMessage;
	}
	
	/**
	 * Get the occupant uri (the room jid and the nick as resource)
	 * 
	 * @return
	 */
	public final XmppURI getOccupantUri() {
		return occupantUri;
	}
	
	/**
	 * Gets the user uri associated to this occupant
	 * 
	 * @return
	 */
	public final XmppURI getUserUri() {
		return userUri;
	}
	
	/**
	 * Get the nick of this occupant
	 * 
	 * @return
	 */
	public final String getNick() {
		return occupantUri.getResource();
	}

	/**
	 * Gets the affiliation of this occupant
	 * 
	 * @return
	 */
	public final Affiliation getAffiliation() {
		return affiliation;
	}

	public final void setAffiliation(final String affiliation) {
		try {
			this.affiliation = Affiliation.valueOf(affiliation);
		} catch (final IllegalArgumentException e) {
			this.affiliation = Affiliation.none;
		} catch (final NullPointerException e) {
			this.affiliation = Affiliation.none;
		}
	}

	public final Role getRole() {
		return role;
	}
	
	public final void setRole(final String role) {
		try {
			this.role = Role.valueOf(role);
		} catch (final IllegalArgumentException e) {
			this.role = Role.unknown;
		} catch (final NullPointerException e) {
			this.role = Role.unknown;
		}
	}

	public final Presence.Show getShow() {
		return show;
	}
	
	public final void setShow(final Presence.Show show) {
		this.show = show;
	}

	/**
	 * Get the occupant status message
	 * 
	 * @return
	 */
	@Nullable
	public final String getStatusMessage() {
		return statusMessage;
	}

	public final void setStatusMessage(@Nullable final String statusMessage) {
		this.statusMessage = statusMessage;
	}

	@Override
	public final String toString() {
		return occupantUri.toString() + "(" + affiliation + "," + role + "," + show + "," + statusMessage + ")";
	}

}
