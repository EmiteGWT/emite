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

package com.calclab.emite.xep.search;

import com.calclab.emite.base.xml.XMLPacket;
import com.calclab.emite.core.XmppURI;

/**
 * Item search result
 * 
 * See: http://xmpp.org/extensions/xep-0055.html#schema
 */
public class SearchResultItem {

	public static SearchResultItem parse(final XMLPacket xml) {
		final XmppURI jid = XmppURI.jid(xml.getAttribute("jid"));
		assert jid != null;
		return new SearchResultItem(jid, xml.getChildText("nick"), xml.getChildText("first"), xml.getChildText("last"), xml.getChildText("email"));
	}

	private final XmppURI jid;
	private String first;
	private String last;
	private String nick;
	private String email;

	/**
	 * Created a search result items. All parameters can be null except jid
	 * 
	 * @param jid
	 *            jid of the result (is a required field)
	 * @param email
	 * @param nick
	 * @param last
	 * @param first
	 * 
	 */
	public SearchResultItem(final XmppURI jid, final String nick, final String first, final String last, final String email) {
		assert jid != null : "SearchResultItem requires a JID";
		this.jid = jid;
		this.first = first;
		this.last = last;
		this.nick = nick;
		this.email = email;
	}

	public XmppURI getJid() {
		return jid;
	}

	public String getEmail() {
		return email;
	}

	public String getFirst() {
		return first;
	}

	public String getLast() {
		return last;
	}

	public String getNick() {
		return nick;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public void setFirst(final String first) {
		this.first = first;
	}

	public void setLast(final String last) {
		this.last = last;
	}

	public void setNick(final String nick) {
		this.nick = nick;
	}
}
