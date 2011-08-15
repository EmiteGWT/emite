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

package com.calclab.emite.xep.disco.client;

import java.util.List;

public class DiscoveryManagerResponse {
	private final List<Identity> identities;

	private final List<Feature> features;

	private final List<Item> items;

	public DiscoveryManagerResponse(final List<Identity> identities, final List<Feature> features) {
		this(identities, features, null);
	}

	public DiscoveryManagerResponse(final List<Identity> identities, final List<Feature> features, final List<Item> items) {
		this.identities = identities;
		this.features = features;
		this.items = items;
	}

	public DiscoveryManagerResponse(final List<Item> items) {
		this(null, null, items);
	}

	public List<Feature> getFeatures() {
		return features;
	}

	public List<Identity> getIdentities() {
		return identities;
	}

	public List<Item> getItems() {
		return items;
	}
}