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

package com.calclab.emite.im.roster;

import com.calclab.emite.core.XmppURI;
import com.calclab.emite.im.events.SubscriptionRequestReceivedEvent;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * Manager presence subscriptions between users. Also, it take cares of
 * integration of roster items and presence subscriptions
 * 
 * @see http://www.xmpp.org/rfcs/rfc3921.html#sub
 * @see http://www.xmpp.org/rfcs/rfc3921.html#int
 */
public interface SubscriptionManager {

	/**
	 * Add a handler to know when a subscription request has arrived
	 * 
	 * @param handler
	 * @return
	 */
	HandlerRegistration addSubscriptionRequestReceivedHandler(SubscriptionRequestReceivedEvent.Handler handler);

	/**
	 * Approves previously subscription request stanza
	 * 
	 * @param jid
	 *            the other entity's JID
	 * @param nick
	 *            the desired roster nick
	 */
	void approveSubscriptionRequest(XmppURI jid, String nick);

	/**
	 * Cancels a previously-granted subscription
	 * 
	 * @param jid
	 *            the entity's jid (resource ignored)
	 */
	void cancelSubscription(XmppURI jid);

	/**
	 * Refuse a previously subscription request stanza
	 * 
	 * @param jid
	 *            the other entity's JID
	 */
	void refuseSubscriptionRequest(XmppURI jid);

	/**
	 * Send a request to subscribe to another entity's presence
	 * 
	 * @param jid
	 *            the another entity's jid (resource ignored)
	 */
	void requestSubscribe(XmppURI jid);

	/**
	 * Unsubscribes from the presence of another entity
	 * 
	 * @param jid
	 *            the another entity's jid (resource ignored)
	 */
	void unsubscribe(XmppURI jid);
}
