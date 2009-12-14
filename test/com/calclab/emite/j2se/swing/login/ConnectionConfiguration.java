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
package com.calclab.emite.j2se.swing.login;

public class ConnectionConfiguration {
    public final String name;
    public final String httpBase;
    public final String domain;
    public final String userName;
    public final String password;

    public ConnectionConfiguration(final String name, final String httpBase, final String domain,
	    final String userName, final String password) {
	this.name = name;
	this.httpBase = httpBase;
	this.domain = domain;
	this.userName = userName;
	this.password = password;
    }

    @Override
    public String toString() {
	return name;
    }
}
