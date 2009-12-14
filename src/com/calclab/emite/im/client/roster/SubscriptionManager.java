/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008-2009 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emite.im.client.roster;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.events.Listener2;

/**
 * Manager presence subscriptions between users. Also, it take cares of
 * integration of roster items and presence subscriptions
 * 
 * @see http://www.xmpp.org/rfcs/rfc3921.html#sub
 * @see http://www.xmpp.org/rfcs/rfc3921.html#int
 */
public interface SubscriptionManager {

    /**
     * Approves previously subscription request stanza
     * 
     * @param jid
     *            the other entity's JID
     * @param nick
     *            the desired roster nick
     */
    public void approveSubscriptionRequest(XmppURI jid, String nick);

    /**
     * Cancels a previously-granted subscription
     * 
     * @param jid
     *            the entity's jid (resource ignored)
     */
    public void cancelSubscription(XmppURI jid);

    /**
     * This listener is called when a INCOMING subscription request arrived (a
     * request to subscribe to the current logged in user's presence)
     * 
     * @param listener
     *            the listener to be called (with the jid of the entity and the
     *            nick name)
     */
    public void onSubscriptionRequested(Listener2<XmppURI, String> listener);

    /**
     * Refuse a previously subscription request stanza
     * 
     * @param jid
     *            the other entity's JID
     */
    public void refuseSubscriptionRequest(XmppURI jid);

    /**
     * Send a request to subscribe to another entity's presence
     * 
     * @param jid
     *            the another entity's jid (resource ignored)
     */
    public void requestSubscribe(XmppURI jid);

    /**
     * Unsubscribes from the presence of another entity
     * 
     * @param jid
     *            the another entity's jid (resource ignored)
     */
    public void unsubscribe(XmppURI jid);
}
