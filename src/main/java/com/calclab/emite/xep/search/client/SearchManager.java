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

package com.calclab.emite.xep.search.client;

import java.util.HashMap;
import java.util.List;

import com.calclab.emite.core.client.xmpp.session.ResultListener;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xep.dataforms.client.Form;

/**
 * Search information repositories on the Jabber network.
 * 
 * Implements XEP-0055: Jabber Search
 * 
 * @see http://xmpp.org/extensions/xep-0055.html
 */
public interface SearchManager {

	/**
	 * Set the host service where send the requests. Usually you send all the
	 * request to same service, so its common to setup once (probably from a
	 * meta tag)
	 * 
	 * @param host
	 *            the jid of the search service
	 */
	public void setHost(XmppURI host);

	/**
	 * Request available search fields using simple search
	 * 
	 * @param onResult
	 *            A SearchResult with a list of string with the available name
	 *            fields
	 * 
	 * @see SearchResult
	 */
	void requestSearchFields(ResultListener<SearchFields> listener);

	/**
	 * Request available search fields using extended search
	 * 
	 * @param onResult
	 *            A SearchResult with a search form
	 * 
	 * @see SearchResult
	 */

	void requestSearchForm(ResultListener<Form> listener);

	/**
	 * Perform a extended search
	 * 
	 * @param searchForm
	 *            the search form
	 * @param listener
	 *            the listener with a form with the results
	 */
	void search(Form searchForm, ResultListener<Form> listener);

	/**
	 * Perform a simple search
	 * 
	 * @param query
	 *            A HashMap with names and values
	 * @param onResult
	 *            A SearchResult with a list of returned items
	 * 
	 * @see SearchResult
	 */
	void search(HashMap<String, String> query, ResultListener<List<SearchResultItem>> listener);

}