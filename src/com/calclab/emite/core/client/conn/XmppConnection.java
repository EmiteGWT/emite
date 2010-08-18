package com.calclab.emite.core.client.conn;

import com.calclab.emite.core.client.bosh.StreamSettings;
import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.packet.IPacket;
import com.google.gwt.event.shared.HandlerRegistration;

public interface XmppConnection {

    public abstract void connect();

    public abstract void disconnect();

    public EmiteEventBus getEventBus();

    public boolean hasErrors();

    public abstract boolean isConnected();

    /**
     * Pause the connection and return a stream settings object that can be
     * serialized to restore the session
     * 
     * @return StreamSettings object if the connection if a stream is present
     *         (the connection is active), null otherwise
     */
    public abstract StreamSettings pause();

    public abstract void restartStream();

    public abstract boolean resume(StreamSettings settings);

    public abstract void send(final IPacket packet);

    public abstract void setSettings(ConnectionSettings settings);

    HandlerRegistration addConnectionHandler(ConnectionStateHandler handler);

    HandlerRegistration addConnectionResponseHandler(ConnectionResponseHandler handler);

    HandlerRegistration addStanzaReceivedHandler(StanzaHandler handler);

    HandlerRegistration addStanzaSentHandler(StanzaHandler handler);
}
