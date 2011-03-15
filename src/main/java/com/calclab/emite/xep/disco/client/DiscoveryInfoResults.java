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
    public static final PacketMatcher INFO_RESULT_MATCHER = MatcherFactory.byNameAndXMLNS("query",
	    "http://jabber.org/protocol/disco#info");

    private HashMap<String, Feature> features;
    private List<Identity> identities;

    private final IPacket result;

    /**
     * Create a DiscoveryInfoResult from an IQ response. The response MUST be of
     * type 'result'
     * 
     * @param iq
     */
    public DiscoveryInfoResults(IQ iq) {
	assert IQ.Type.result == iq.getType();
	this.result = iq.getFirstChild(INFO_RESULT_MATCHER);
    }

    public boolean areFeaturedSupported(String... featuresName) {
	for (String feature : featuresName) {
	    if (!features.containsKey(feature)) {
		return false;
	    }
	}
	return true;
    }

    public Collection<Feature> getFeatures() {
	if (this.features == null) {
	    this.features = processFeatures(result.getChildren(FEATURES_MATCHER));
	}
	return this.features.values();
    }

    public Collection<Identity> getIdentities() {
	if (this.identities == null) {
	    this.identities = processIdentity(result.getChildren(IDENTITIES_MATCHER));
	}
	return this.identities;
    }

    private HashMap<String, Feature> processFeatures(final List<? extends IPacket> children) {
	HashMap<String, Feature> features = new HashMap<String, Feature>();
	for (final IPacket child : children) {
	    Feature feature = Feature.fromPacket(child);
	    features.put(feature.var, feature);
	}
	return features;
    }

    private List<Identity> processIdentity(final List<? extends IPacket> children) {
	List<Identity> identities = new ArrayList<Identity>();
	for (final IPacket child : children) {
	    identities.add(Identity.fromPacket(child));
	}
	return identities;
    }

}
