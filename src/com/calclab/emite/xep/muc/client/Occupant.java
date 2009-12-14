/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008-2009 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emite.xep.muc.client;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;

public class Occupant {

    public static enum Affiliation {
	admin, member, none, owner
    }

    public static enum Role {
	moderator, participant, unknown, visitor
    }

    private Affiliation affiliation;
    private Role role;
    private final XmppURI uri;

    public Occupant(final XmppURI uri, final String affiliation, final String role) {
	this.uri = uri;
	setAffiliation(affiliation);
	setRole(role);
    }

    public Affiliation getAffiliation() {
	return affiliation;
    }

    public String getNick() {
	return uri.getResource();
    }

    public Role getRole() {
	return role;
    }

    public XmppURI getURI() {
	return uri;
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

    @Override
    public String toString() {
	return uri.toString() + "(" + affiliation + "," + role + ")";
    }
}
