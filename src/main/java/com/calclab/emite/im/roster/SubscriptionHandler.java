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
public final class SubscriptionHandler implements SubscriptionRequestReceivedEvent.Handler {

	public static enum Behavior {
		/** do nothing **/
		none,
		/** accept all subscription request **/
		acceptAll,
		/** refuses all subscription request **/
		refuseAll
	}
	
	private static Behavior behavior = Behavior.none;
	
	/**
	 * Change the behavior.
	 * 
	 * @param behavior
	 *            the desired new behavior
	 */
	public static final void setBehavior(final Behavior behavior) {
		SubscriptionHandler.behavior = checkNotNull(behavior);
	}
	
	private final SubscriptionManager manager;

	@Inject
	protected SubscriptionHandler(final SubscriptionManager manager) {
		this.manager = checkNotNull(manager);

		manager.addSubscriptionRequestReceivedHandler(this);
	}

	@Override
	public void onSubscriptionRequestReceived(final SubscriptionRequestReceivedEvent event) {
		if (Behavior.acceptAll.equals(behavior)) {
			manager.approveSubscriptionRequest(event.getFrom(), event.getNick());
		} else if (Behavior.refuseAll.equals(behavior)) {
			manager.refuseSubscriptionRequest(event.getFrom());
		}
	}

}
