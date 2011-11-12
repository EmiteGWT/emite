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

import static com.google.common.base.Preconditions.checkNotNull;

import com.calclab.emite.im.events.SubscriptionRequestReceivedEvent;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * A little utility to handle subscriptions automatically. This component is not
 * created by default, so you have to explicitly create:
 * 
 * <pre>
 * SubscriptionHandler handler = Suco.get(SubscriptionHandler.class);
 * handler.setBehaviour(Behaviour.acceptAll);
 * </pre>
 * 
 * The default behavior is none: do nothing
 */
@Singleton
public class SubscriptionHandler implements SubscriptionRequestReceivedEvent.Handler {

	public static enum Behavior {
		/** do nothing **/
		none,
		/** accept all subscription request **/
		acceptAll,
		/** refuses all subscription request **/
		refuseAll
	}

	private final SubscriptionManager manager;
	private Behavior behavior;

	@Inject
	protected SubscriptionHandler(final SubscriptionManager manager) {
		this.manager = checkNotNull(manager);
		this.behavior = Behavior.none;

		manager.addSubscriptionRequestReceivedHandler(this);
	}

	@Override
	public void onSubscriptionRequestReceived(final SubscriptionRequestReceivedEvent event) {
		if (behavior == Behavior.acceptAll) {
			manager.approveSubscriptionRequest(event.getFrom(), event.getNick());
		} else if (behavior == Behavior.refuseAll) {
			manager.refuseSubscriptionRequest(event.getFrom());
		}
	}

	/**
	 * Change the behavior
	 * 
	 * @param behaviour
	 *            the desired new behavior
	 */
	public void setBehavior(final Behavior behavior) {
		this.behavior = checkNotNull(behavior);
	}

}
