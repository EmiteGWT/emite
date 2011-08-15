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

package com.calclab.emite.core.client.conn;

import java.util.logging.Logger;

import com.calclab.emite.core.client.bosh.StreamSettings;
import com.calclab.emite.core.client.conn.ConnectionStateChangedEvent.ConnectionState;
import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.Packet;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * A base XmppConnection implementation with all the boiler plate code.
 * 
 * @see XmppConnection
 */
public abstract class XmppConnectionBoilerPlate implements XmppConnection {

	private static final Logger logger = Logger.getLogger(XmppConnectionBoilerPlate.class.getName());
	
	protected final EmiteEventBus eventBus;
	private int errors;
	private boolean active;
	private StreamSettings stream;
	private Packet currentBody;
	private ConnectionSettings connectionSettings;

	public XmppConnectionBoilerPlate(final EmiteEventBus eventBus) {
		this.eventBus = eventBus;
	}

	@Override
	public HandlerRegistration addConnectionResponseHandler(final ConnectionResponseHandler handler) {
		return ConnectionResponseEvent.bind(eventBus, handler);
	}

	@Override
	public HandlerRegistration addConnectionStateChangedHandler(final ConnectionStateChangedHandler handler) {
		return ConnectionStateChangedEvent.bind(eventBus, handler);
	}

	@Override
	public HandlerRegistration addStanzaReceivedHandler(final StanzaHandler handler) {
		return StanzaReceivedEvent.bind(eventBus, handler);
	}

	@Override
	public HandlerRegistration addStanzaSentHandler(final StanzaHandler handler) {
		return StanzaSentEvent.bind(eventBus, handler);
	}

	public void clearErrors() {
		errors = 0;
	}

	@Override
	public EmiteEventBus getEventBus() {
		return eventBus;
	}

	/**
	 * @return the stream settings
	 */
	@Override
	public StreamSettings getStreamSettings() {
		return stream;
	}

	@Override
	public boolean hasErrors() {
		return errors != 0;
	}

	public int incrementErrors() {
		errors++;
		return errors;
	}

	@Override
	public void setSettings(final ConnectionSettings settings) {
		logger.finer("Setting connection settings.");
		connectionSettings = settings;
	}

	protected void fireConnected() {
		eventBus.fireEvent(new ConnectionStateChangedEvent(ConnectionState.connected));
	}

	protected void fireDisconnected(final String message) {
		eventBus.fireEvent(new ConnectionStateChangedEvent(ConnectionState.disconnected, message));
	}

	protected void fireError(final String error) {
		eventBus.fireEvent(new ConnectionStateChangedEvent(ConnectionState.error, error));
	}

	protected void fireResponse(final String response) {
		eventBus.fireEvent(new ConnectionResponseEvent(response));
	}

	protected void fireRetry(final int attempt, final int scedTime) {
		eventBus.fireEvent(new ConnectionStateChangedEvent(ConnectionState.waitingForRetry, "The connection will try to re-connect in " + scedTime
				+ " milliseconds.", scedTime));
	}

	protected void fireStanzaReceived(final IPacket stanza) {
		eventBus.fireEvent(new StanzaReceivedEvent(stanza));
	}

	protected ConnectionSettings getConnectionSettings() {
		return connectionSettings;
	}

	/**
	 * @return the currentBody
	 */
	protected Packet getCurrentBody() {
		return currentBody;
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
