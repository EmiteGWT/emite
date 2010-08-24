package com.calclab.emite.xep.disco.client;

import java.util.ArrayList;
import java.util.List;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.MatcherFactory;
import com.calclab.emite.core.client.packet.PacketMatcher;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;

/**
 * Discovery items result object.
 * 
 * It contains zero or more Item objects
 * 
 * @see http://xmpp.org/extensions/xep-0030.html#items
 */
public class DiscoveryItemsResults {
    public static final PacketMatcher ITEMS_RESULT_MATCHER = MatcherFactory.byNameAndXMLNS("query",
	    "http://jabber.org/protocol/disco#items");
    public static final PacketMatcher ITEMS_MATCHER = MatcherFactory.byName("item");
    private List<Item> items;
    private final IPacket result;

    public DiscoveryItemsResults(IQ iq) {
	assert IQ.Type.result == iq.getType();
	this.result = iq.getFirstChild(ITEMS_RESULT_MATCHER);

    }

    public List<Item> getItems() {
	if (this.items == null) {
	    this.items = processItem(result.getChildren(ITEMS_MATCHER));
	}
	return this.items;
    }

    private List<Item> processItem(final List<? extends IPacket> children) {
	List<Item> items = new ArrayList<Item>();
	for (final IPacket child : children) {
	    items.add(Item.fromPacket(child));
	}
	return items;
    }
}
