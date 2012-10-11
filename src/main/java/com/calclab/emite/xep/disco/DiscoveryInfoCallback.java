package com.calclab.emite.xep.disco;

import com.calclab.emite.base.xml.XMLPacket;
import com.google.common.collect.ImmutableSet;

public interface DiscoveryInfoCallback {
	
	void onDiscoveryInfoResult(ImmutableSet<Feature> features, ImmutableSet<Identity> identities);
	
	void onDiscoveryInfoError(XMLPacket error);

}