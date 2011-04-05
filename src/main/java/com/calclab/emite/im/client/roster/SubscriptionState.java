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

/**
 * Detailed information about possible subscription states, which are described
 * here from the user's (not contact's) perspective
 * 
 *@see http://www.xmpp.org/rfcs/rfc3921.html#substates
 */
public enum SubscriptionState {
    /**
     * "both" -- both the user and the contact have subscriptions to each
     * other's presence information
     */
    both,
    /**
     * "from" -- the contact has a subscription to the user's presence
     * information, but the user does not have a subscription to the contact's
     * presence information
     */
    from,
    /**
     * "none" -- the user does not have a subscription to the contact's presence
     * information, and the contact does not have a subscription to the user's
     * presence information
     */
    none,
    /**
     * "None + Pending Out" -- contact and user are not subscribed to each
     * other, and user has sent contact a subscription request but contact has
     * not replied yet
     */
    nonePendingOut,
    /**
     * "None + Pending In" = contact and user are not subscribed to each other,
     * and contact has sent user a subscription request but user has not replied
     * yet
     */
    nonePendingIn,
    /**
     * "None + Pending Out/In" = contact and user are not subscribed to each
     * other, contact has sent user a subscription request but user has not
     * replied yet, and user has sent contact a subscription request but contact
     * has not replied yet
     */
    nonePendingInOut,
    /**
     * "to" -- the user has a subscription to the contact's presence
     * information, but the contact does not have a subscription to the user's
     * presence information
     */
    to,
    /**
     * "From + Pending Out" = contact is subscribed to user, and user has sent
     * contact a subscription request but contact has not replied yet
     */
    fromPendingOut,
    /**
     * remove -- the contact and it's presence subscription is going to be
     * removed
     */
    remove
}