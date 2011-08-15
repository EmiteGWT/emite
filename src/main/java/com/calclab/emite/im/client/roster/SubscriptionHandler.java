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

package com.calclab.emite.im.client.roster;

import com.calclab.emite.im.client.roster.events.SubscriptionRequestReceivedEvent;
import com.calclab.emite.im.client.roster.events.SubscriptionRequestReceivedHandler;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * A little utility to handle subscriptions automatically. This component is not
 * created by default, so you have to explicity create:
 * 
 * <pre>
 * SubscriptionHandler handler = Suco.get(SubscriptionHandler.class);
 * handler.setBehaviour(Behaviour.acceptAll);
 * </pre>
 * 
 * The default behaviour is none: do nothing
 */
@Singleton
public class SubscriptionHandler {

	public static enum Behaviour {
		/** do nothing **/
		none,
		/** accept all subscription request **/
		acceptAll,
		/** refuses all subscription request **/
		refuseAll
	}

	private Behaviour behaviour;

	@Inject
	public SubscriptionHandler(final SubscriptionManager manager) {
		behaviour = Behaviour.none;

		manager.addSubscriptionRequestReceivedHandler(new SubscriptionRequestReceivedHandler() {
			@Override
			public void onSubscriptionRequestReceived(final SubscriptionRequestReceivedEvent event) {
				if (behaviour == Behaviour.acceptAll) {
					manager.approveSubscriptionRequest(event.getFrom(), event.getNick());
				} else if (behaviour == Behaviour.refuseAll) {
					manager.refuseSubscriptionRequest(event.getFrom());
				}
			}
		});

	}

	/**
	 * Change the behaviour
	 * 
	 * @param behaviour
	 *            the desired new behaviour
	 */
	public void setBehaviour(final Behaviour behaviour) {
		this.behaviour = behaviour;
	}

}
