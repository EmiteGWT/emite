package com.calclab.emite.xep.disco;

import com.calclab.emite.base.xml.XMLPacket;
import com.google.common.collect.ImmutableSet;

public interface DiscoveryItemsCallback {
	
	void onDiscoveryItemsResult(ImmutableSet<Item> items);
	
	void onDiscoveryItemsError(XMLPacket error);
	
}