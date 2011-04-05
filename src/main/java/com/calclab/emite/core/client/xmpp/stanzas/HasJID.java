package com.calclab.emite.core.client.xmpp.stanzas;

import com.calclab.emite.im.client.roster.RosterItem;

/**
 * Interface which provides a standard method for accessing the JID from an
 * object which encapsulates one (such as {@link XmppURI} and {@link RosterItem}
 * ).
 */
public interface HasJID {
	
	/**
	 * Gets the Jabber ID (JID) from this object.
	 * 
	 * @return the JID.
	 */
	public XmppURI getJID();
}
