package com.calclab.emite.xtesting;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.roster.RosterItem;

public class RosterItemHelper {

    public static RosterItem createItem(final String jid, final String name, final boolean isAvailable,
	    final String... groups) {
	final RosterItem item = new RosterItem(XmppURI.uri(jid), null, name, null);
	for (final String group : groups) {
	    item.addToGroup(group);
	}
	String resource = item.getJID().getResource();
	item.setAvailable(isAvailable, resource);
	return item;
    }

    public static RosterItem createItem(final String jid, final String name, final String... groups) {
	return createItem(jid, name, false, groups);
    }

}
