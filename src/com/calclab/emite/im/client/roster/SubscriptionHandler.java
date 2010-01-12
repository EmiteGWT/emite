package com.calclab.emite.im.client.roster;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.events.Listener2;
import com.google.gwt.core.client.GWT;

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

    public SubscriptionHandler(final SubscriptionManager manager) {
	this.behaviour = Behaviour.none;

	manager.onSubscriptionRequested(new Listener2<XmppURI, String>() {
	    @Override
	    public void onEvent(XmppURI uri, String nick) {
		GWT.log("Subscription requested: " + nick, null);
		if (behaviour == Behaviour.acceptAll) {
		    manager.approveSubscriptionRequest(uri, nick);
		} else if (behaviour == Behaviour.refuseAll) {
		    manager.refuseSubscriptionRequest(uri);
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
