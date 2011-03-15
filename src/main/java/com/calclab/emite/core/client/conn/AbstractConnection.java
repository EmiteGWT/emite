package com.calclab.emite.core.client.conn;

import com.calclab.emite.core.client.conn.ConnectionStateChangedEvent.ConnectionState;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.suco.client.events.Listener;
import com.calclab.suco.client.events.Listener0;
import com.calclab.suco.client.events.Listener2;

/**
 * An abstract connection. It has all the boilerplate
 * 
 */
public abstract class AbstractConnection {

    protected final XmppConnection delegate;

    public AbstractConnection(final XmppConnection delegate) {
	this.delegate = delegate;
    }

    public void onConnected(final Listener0 listener) {
	delegate.addConnectionStateChangedHandler(new ConnectionStateChangedHandler() {
	    @Override
	    public void onStateChanged(final ConnectionStateChangedEvent event) {
		if (event.is(ConnectionState.connected)) {
		    listener.onEvent();
		}
	    }
	});
    }

    public void onDisconnected(final Listener<String> listener) {
	delegate.addConnectionStateChangedHandler(new ConnectionStateChangedHandler() {
	    @Override
	    public void onStateChanged(final ConnectionStateChangedEvent event) {
		if (event.is(ConnectionState.disconnected)) {
		    listener.onEvent(event.getDescription());
		}
	    }
	});
    }

    public void onError(final Listener<String> listener) {
	delegate.addConnectionStateChangedHandler(new ConnectionStateChangedHandler() {
	    @Override
	    public void onStateChanged(final ConnectionStateChangedEvent event) {
		if (event.is(ConnectionState.error)) {
		    listener.onEvent(event.getDescription());
		}
	    }
	});
    }

    /**
     * Use addConnectionResponseHandler
     * 
     * @param listener
     */
    @Deprecated
    public void onResponse(final Listener<String> listener) {
	delegate.addConnectionResponseHandler(new ConnectionResponseHandler() {
	    @Override
	    public void onResponse(final ConnectionResponseEvent event) {
		listener.onEvent(event.getResponse());
	    }
	});
    }

    public void onRetry(final Listener2<Integer, Integer> listener) {
	delegate.addConnectionStateChangedHandler(new ConnectionStateChangedHandler() {
	    @Override
	    public void onStateChanged(final ConnectionStateChangedEvent event) {
		if (event.is(ConnectionState.waitingForRetry)) {
		    listener.onEvent(event.getValue(), 0);
		}
	    }
	});
    }

    public void onStanzaReceived(final Listener<IPacket> listener) {
	delegate.addStanzaReceivedHandler(new StanzaHandler() {
	    @Override
	    public void onStanza(final StanzaEvent event) {
		listener.onEvent(event.getStanza());
	    }
	});
    }

    public void onStanzaSent(final Listener<IPacket> listener) {
	delegate.addStanzaSentHandler(new StanzaHandler() {
	    @Override
	    public void onStanza(final StanzaEvent event) {
		listener.onEvent(event.getStanza());
	    }
	});
    }
}
