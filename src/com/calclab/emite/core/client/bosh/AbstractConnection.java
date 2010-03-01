package com.calclab.emite.core.client.bosh;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.Packet;
import com.calclab.suco.client.events.Event;
import com.calclab.suco.client.events.Event0;
import com.calclab.suco.client.events.Event2;
import com.calclab.suco.client.events.Listener;
import com.calclab.suco.client.events.Listener0;
import com.calclab.suco.client.events.Listener2;

/**
 * An abstract connection. It has all the boilerplate
 * 
 */
public abstract class AbstractConnection implements Connection {
    private final Event<String> onError;
    private final Event2<Integer, Integer> onRetry;
    private final Event<String> onDisconnected;
    private final Event0 onConnected;
    private final Event<IPacket> onStanzaReceived;
    private final Event<IPacket> onStanzaSent;
    private final Event<String> onResponse;
    private boolean active;
    private StreamSettings stream;
    private Packet currentBody;
    private int errors;
    private BoshSettings userSettings;

    public AbstractConnection() {
	onError = new Event<String>("bosh:onError");
	onRetry = new Event2<Integer, Integer>("bosh:onRetry");
	onDisconnected = new Event<String>("bosh:onDisconnected");
	onConnected = new Event0("bosh:onConnected");
	onStanzaReceived = new Event<IPacket>("bosh:onReceived");
	onResponse = new Event<String>("bosh:onResponse");
	onStanzaSent = new Event<IPacket>("bosh:onSent");
    }

    public void clearErrors() {
	errors = 0;
    }

    public int incrementErrors() {
	errors++;
	return errors;
    }
    
    public boolean noError(){
	return errors == 0;
    }

    public void onError(final Listener<String> listener) {
	onError.add(listener);
    }

    public void onRetry(final Listener2<Integer, Integer> listener) {
	onRetry.add(listener);
    }

    public void onResponse(final Listener<String> listener) {
	onResponse.add(listener);
    }

    public void onConnected(final Listener0 listener) {
	onConnected.add(listener);
    }

    public void onDisconnected(final Listener<String> listener) {
	onDisconnected.add(listener);
    }

    public void onStanzaReceived(final Listener<IPacket> listener) {
	onStanzaReceived.add(listener);
    }

    public void onStanzaSent(final Listener<IPacket> listener) {
	onStanzaSent.add(listener);
    }

    public void removeOnStanzaReceived(final Listener<IPacket> listener) {
	onStanzaReceived.remove(listener);
    }

    public void setSettings(final BoshSettings settings) {
	userSettings = settings;
    }

    protected void fireConnected() {
	onConnected.fire();
    }

    protected void fireRetry(Integer attempt, Integer scedTime) {
	onRetry.fire(attempt, scedTime);
    }
    
    protected void fireDisconnected(final String message) {
	onDisconnected.fire(message);
    }

    protected void fireError(final String error) {
	onError.fire(error);
    }

    protected void fireResponse(final String response) {
	onResponse.fire(response);
    }

    protected void fireStanzaReceived(final IPacket stanza) {
	onStanzaReceived.fire(stanza);
    }

    protected void fireStanzaSent(final IPacket packet) {
	onStanzaSent.fire(packet);
    }

    /**
     * @return the currentBody
     */
    protected Packet getCurrentBody() {
	return currentBody;
    }

    /**
     * @return the stream
     */
    protected StreamSettings getStream() {
	return stream;
    }

    protected BoshSettings getUserSettings() {
	return userSettings;
    }

    /**
     * @return if the connection is active
     */
    protected boolean isActive() {
	return active;
    }

    /**
     * Set the conntection active
     * 
     * @param active
     *            true if active
     * 
     */
    protected void setActive(final boolean active) {
	this.active = active;
    }

    /**
     * @param currentBody
     *            the currentBody to set
     */
    protected void setCurrentBody(final Packet currentBody) {
	this.currentBody = currentBody;
    }

    /**
     * @param stream
     *            the stream to set
     */
    protected void setStream(final StreamSettings stream) {
	this.stream = stream;
    }
}
