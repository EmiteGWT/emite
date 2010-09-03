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
	this.behaviour = Behaviour.none;

	manager.addSubscriptionRequestReceivedHandler(new SubscriptionRequestReceivedHandler() {
	    @Override
	    public void onSubscriptionRequestReceived(SubscriptionRequestReceivedEvent event) {
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
    public void setBehaviour(Behaviour behaviour) {
	this.behaviour = behaviour;
    }

}
