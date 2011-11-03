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

import com.calclab.emite.core.client.conn.bosh.StreamSettings;
import com.calclab.emite.core.client.events.ConnectionResponseEvent;
import com.calclab.emite.core.client.events.ConnectionStatusChangedEvent;
import com.calclab.emite.core.client.events.PacketReceivedEvent;
import com.calclab.emite.core.client.events.StanzaSentEvent;
import com.calclab.emite.core.client.xml.HasXML;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * A connection to a XMPP server
 */
public interface XmppConnection {

	/**
	 * Add a handler to know when a response (text) arrived from server.
	 * 
	 * @param handler
	 *            the handler
	 * @return the handler registration, can be stored in order to remove the
	 *         handler later
	 */
	HandlerRegistration addConnectionResponseHandler(ConnectionResponseEvent.Handler handler);

	/**
	 * Add a handler to know when the status of the connection has changed.
	 * 
	 * @param handler
	 *            the handler
	 * @return the handler registration, can be stored in order to remove the
	 *         handler later
	 */
	HandlerRegistration addConnectionStatusChangedHandler(ConnectionStatusChangedEvent.Handler handler);

	/**
	 * Add a handler to know when a stanza has arrived from the server.
	 * 
	 * @param handler
	 *            the handler
	 * @return the handler registration, can be stored in order to remove the
	 *         handler later
	 */
	HandlerRegistration addStanzaReceivedHandler(PacketReceivedEvent.Handler handler);

	/**
	 * Add a handler to know when a stanza has been sent to the server.
	 * 
	 * @param handler
	 *            the handler
	 * @return the handler registration, can be stored in order to remove the
	 *         handler later
	 */
	HandlerRegistration addStanzaSentHandler(StanzaSentEvent.Handler handler);

	/**
	 * Connect to the server.
	 * 
	 * Connection settings must be set before calling this method.
	 * 
	 * @see #setSettings
	 */
	void connect();

	/**
	 * Disconnect from the server
	 */
	void disconnect();

	/**
	 * Obtain the stream settings. This is the actual object used in the
	 * connection and should not be changed
	 * 
	 * @return the current stream settings
	 */
	StreamSettings getStreamSettings();

	/**
	 * A way to know if the connection has errors
	 * 
	 * @return true if the connection has errors
	 */
	boolean hasErrors();

	/**
	 * A way know if the connection is connected
	 * 
	 * @return true if is connected
	 */
	boolean isConnected();

	/**
	 * Pause the connection and return a stream settings object that can be
	 * serialized to restore the session
	 * 
	 * @return StreamSettings object if the connection if a stream is present
	 *         (the connection is active), null otherwise
	 */
	StreamSettings pause();

	/**
	 * A way to restart the stream. Usually you don't need this method
	 */
	void restartStream();

	/**
	 * The opposite to the pause method. Usually used to pause the session
	 * between web page changes
	 * 
	 * @param settings
	 *            the paused stream settings
	 * @return true if the connection is resumed
	 */
	boolean resume(StreamSettings settings);

	/**
	 * Send a packet into the connection channel
	 * 
	 * @param packet
	 *            the packet to be sent
	 */
	void send(HasXML packet);

	/**
	 * Set the connection settings. This method MUST be called before connect.
	 * You can use the BrowserModule to configure the connection via html meta
	 * tags (recommended)
	 * 
	 * @param settings
	 *            The connection settings
	 */
	void setSettings(ConnectionSettings settings);
}
