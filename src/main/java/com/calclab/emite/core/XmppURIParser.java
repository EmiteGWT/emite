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

package com.calclab.emite.core;

import com.google.common.base.Function;
import com.google.common.base.Strings;

/**
 * URI parser and validator.
 */
final class XmppURIParser implements Function<String, XmppURI> {
	
	protected static final XmppURIParser INSTANCE = new XmppURIParser();

	// TODO: Add Stringprep support.

	@Override
	public final XmppURI apply(final String uri) {
		if (Strings.isNullOrEmpty(uri))
			return null;

		String node = null;
		String domain = null;
		String resource = null;

		final int atIndex = uri.indexOf('@') + 1;
		if (atIndex > 0) {
			node = uri.substring(0, atIndex - 1);
			if (node.length() == 0)
				return null;
		}

		final int barIndex = uri.indexOf('/', atIndex);
		if (atIndex == barIndex)
			return null;
		if (barIndex > 0) {
			domain = uri.substring(atIndex, barIndex);
			resource = uri.substring(barIndex + 1);
		} else {
			domain = uri.substring(atIndex);
		}
		if (domain.length() == 0)
			return null;

		return new XmppURI(node, domain, resource);
	}
	
	private XmppURIParser() {
	}

}