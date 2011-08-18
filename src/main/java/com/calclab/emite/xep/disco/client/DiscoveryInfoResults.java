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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.MatcherFactory;
import com.calclab.emite.core.client.packet.PacketMatcher;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;

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
	public static final PacketMatcher FEATURES_MATCHER = MatcherFactory.byName("feature");
	public static final PacketMatcher IDENTITIES_MATCHER = MatcherFactory.byName("identity");
	public static final PacketMatcher INFO_RESULT_MATCHER = MatcherFactory.byNameAndXMLNS("query", "http://jabber.org/protocol/disco#info");

	private HashMap<String, Feature> features;
	private List<Identity> identities;

	private final IPacket result;

	/**
	 * Create a DiscoveryInfoResult from an IQ response. The response MUST be of
	 * type 'result'
	 * 
	 * @param iq
	 */
	public DiscoveryInfoResults(final IQ iq) {
		assert IQ.Type.result.equals(iq.getType());
		result = iq.getFirstChild(INFO_RESULT_MATCHER);
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
			features = processFeatures(result.getChildren(FEATURES_MATCHER));
		}
		return features.values();
	}

	public Collection<Identity> getIdentities() {
		if (identities == null) {
			identities = processIdentity(result.getChildren(IDENTITIES_MATCHER));
		}
		return identities;
	}

	private HashMap<String, Feature> processFeatures(final List<? extends IPacket> children) {
		final HashMap<String, Feature> features = new HashMap<String, Feature>();
		for (final IPacket child : children) {
			final Feature feature = Feature.fromPacket(child);
			features.put(feature.var, feature);
		}
		return features;
	}

	private List<Identity> processIdentity(final List<? extends IPacket> children) {
		final List<Identity> identities = new ArrayList<Identity>();
		for (final IPacket child : children) {
			identities.add(Identity.fromPacket(child));
		}
		return identities;
	}

}
