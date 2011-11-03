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

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.calclab.emite.core.client.stanzas.IQ;
import com.calclab.emite.core.client.xml.XMLPacket;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * A discovery info result object.
 * 
 * Contains one or more Identity elements and one or more Feature elements.
 * (Note: Every entity MUST have at least one identity, and every entity MUST
 * support at least the 'http://jabber.org/protocol/disco#info' feature)
 * 
 * @see http://xmpp.org/extensions/xep-0030.html#info
 */
public class DiscoveryInfoResults {

	private Map<String, Feature> features;
	private List<Identity> identities;

	private final XMLPacket result;

	/**
	 * Create a DiscoveryInfoResult from an IQ response. The response MUST be of
	 * type 'result'
	 * 
	 * @param iq
	 */
	public DiscoveryInfoResults(final IQ iq) {
		assert IQ.Type.result.equals(iq.getType());
		result = iq.getChild("query", "http://jabber.org/protocol/disco#info");
	}

	public boolean areFeaturedSupported(final String... featuresName) {
		for (final String feature : featuresName) {
			if (!features.containsKey(feature))
				return false;
		}
		return true;
	}

	public Collection<Feature> getFeatures() {
		if (features == null) {
			features = processFeatures(result.getChildren("feature"));
		}
		return features.values();
	}

	public Collection<Identity> getIdentities() {
		if (identities == null) {
			identities = processIdentity(result.getChildren("identity"));
		}
		return identities;
	}

	private static Map<String, Feature> processFeatures(final List<XMLPacket> children) {
		final Map<String, Feature> features = Maps.newHashMap();
		for (final XMLPacket child : children) {
			final Feature feature = Feature.fromPacket(child);
			features.put(feature.var, feature);
		}
		return features;
	}

	private static List<Identity> processIdentity(final List<XMLPacket> children) {
		final List<Identity> identities = Lists.newArrayList();
		for (final XMLPacket child : children) {
			identities.add(Identity.fromPacket(child));
		}
		return identities;
	}

}
