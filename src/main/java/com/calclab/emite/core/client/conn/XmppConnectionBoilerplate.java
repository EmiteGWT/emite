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

import com.calclab.emite.core.client.conn.bosh.StreamSettings;
import com.calclab.emite.core.client.events.ConnectionResponseEvent;
import com.calclab.emite.core.client.events.ConnectionStatusChangedEvent;
import com.calclab.emite.core.client.events.PacketReceivedEvent;
import com.calclab.emite.core.client.events.StanzaSentEvent;
import com.calclab.emite.core.client.xml.XMLPacket;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * A base XmppConnection implementation with all the boiler plate code.
 * 
 * @see XmppConnection
 */
public abstract class XmppConnectionBoilerplate implements XmppConnection {

	private static final Logger logger = Logger.getLogger(XmppConnectionBoilerplate.class.getName());
	
	protected final EventBus eventBus;
	private int errors;
	private boolean active;
	private StreamSettings stream;
	private XMLPacket currentBody;
	private ConnectionSettings connectionSettings;

	public XmppConnectionBoilerplate(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	@Override
	public HandlerRegistration addConnectionResponseHandler(final ConnectionResponseEvent.Handler handler) {
		return eventBus.addHandlerToSource(ConnectionResponseEvent.TYPE, this, handler);
	}

	@Override
	public HandlerRegistration addConnectionStatusChangedHandler(final ConnectionStatusChangedEvent.Handler handler) {
		return eventBus.addHandlerToSource(ConnectionStatusChangedEvent.TYPE, this, handler);
	}

	@Override
	public HandlerRegistration addStanzaReceivedHandler(final PacketReceivedEvent.Handler handler) {
		return eventBus.addHandlerToSource(PacketReceivedEvent.TYPE, this, handler);
	}

	@Override
	public HandlerRegistration addStanzaSentHandler(final StanzaSentEvent.Handler handler) {
		return eventBus.addHandlerToSource(StanzaSentEvent.TYPE, this, handler);
	}

	/**
	 * @return the stream settings
	 */
	@Override
	public StreamSettings getStreamSettings() {
		return stream;
	}
	
	public void clearErrors() {
		errors = 0;
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

	protected ConnectionSettings getConnectionSettings() {
		return connectionSettings;
	}

	/**
	 * @return the currentBody
	 */
	protected XMLPacket getCurrentBody() {
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
	protected void setCurrentBody(final XMLPacket currentBody) {
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
