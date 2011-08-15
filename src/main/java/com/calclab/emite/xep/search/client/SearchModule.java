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

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;

/**
 * Implements XEP-0055: Jabber Search
 * 
 * This specification provides canonical documentation of the jabber:iq:search
 * namespace currently in use within the Jabber community.
 * 
 * @see http://www.xmpp.org/extensions/xep-0055.html
 * 
 * 
 */
public class SearchModule extends AbstractGinModule {

	@Override
	protected void configure() {
		bind(SearchManager.class).to(SearchManagerImpl.class).in(Singleton.class);
	}

}
