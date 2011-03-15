package com.calclab.emite.core.client.conn;

import com.calclab.emite.core.client.bosh.StreamSettings;
import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.packet.IPacket;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * A connection to a xmpp server
 */
public interface XmppConnection {

    /**
     * Add a handler to know when a response (text) arrived from server
     * 
     * @param handler
     * @return
     */
    public HandlerRegistration addConnectionResponseHandler(ConnectionResponseHandler handler);

    /**
     * Add a handler to know when the state of the connection has changed
     * 
     * @param handler
     * @return
     */
    public HandlerRegistration addConnectionStateChangedHandler(ConnectionStateChangedHandler handler);

    /**
     * Add a handler to know when a stanza has arrived from the server
     * 
     * @param handler
     * @return
     */
    public HandlerRegistration addStanzaReceivedHandler(StanzaHandler handler);

    /**
     * Add a handler to know when a stanza has been sent to the server
     * 
     * @param handler
     * @return
     */
    public HandlerRegistration addStanzaSentHandler(StanzaHandler handler);

    /**
     * Connect to the server
     * 
     * @see setSettings
     */
    public abstract void connect();

    /**
     * Disconnect to the server
     */
    public abstract void disconnect();

    /**
     * Get the default Emite event bus
     * 
     * @return
     */
    public EmiteEventBus getEventBus();

    /**
     * Obtain the stream settings. This is the actual object used in the
     * connection and should not be changed
     * 
     * @return the current stream settings
     */
    public StreamSettings getStreamSettings();

    /**
     * A way to know if the connection has errors
     * 
     * @return true if the connection has errors
     */
    public boolean hasErrors();

    /**
     * A way know if the connection is connected
     * 
     * @return true if is connected
     */
    public abstract boolean isConnected();

    /**
     * Pause the connection and return a stream settings object that can be
     * serialized to restore the session
     * 
     * @return StreamSettings object if the connection if a stream is present
     *         (the connection is active), null otherwise
     */
    public abstract StreamSettings pause();

    /**
     * A way to restart the stream. Usually you don't need this method
     */
    public abstract void restartStream();

    /**
     * The opposite to the pause method. Usually used to pause the session
     * between web page changes
     * 
     * @param settings
     *            the paused stream settings
     * @return true if the connection is resumed
     */
    public abstract boolean resume(StreamSettings settings);

    /**
     * Send a packet into the connection channel
     * 
     * @param packet
     */
    public abstract void send(final IPacket packet);

    /**
     * Set the connection settings. This method MUST be called before connect.
     * You can use the BrowserModule to configure the connection via html meta
     * tags (recommended)
     * 
     * @param settings
     *            The connection settings
     */
    public abstract void setSettings(ConnectionSettings settings);
}
