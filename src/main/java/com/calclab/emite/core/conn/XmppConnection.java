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

package com.calclab.emite.core.conn;

import javax.annotation.Nullable;

import com.calclab.emite.base.xml.HasXML;
import com.calclab.emite.core.events.ConnectionStatusChangedEvent;
import com.calclab.emite.core.events.PacketReceivedEvent;
import com.calclab.emite.core.events.PacketSentEvent;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * A connection to a XMPP server.
 */
public interface XmppConnection {

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
	HandlerRegistration addPacketReceivedHandler(PacketReceivedEvent.Handler handler);

	/**
	 * Add a handler to know when a stanza has been sent to the server.
	 * 
	 * @param handler
	 *            the handler
	 * @return the handler registration, can be stored in order to remove the
	 *         handler later
	 */
	HandlerRegistration addPacketSentHandler(PacketSentEvent.Handler handler);

	/**
	 * Connect to the server.
	 * 
	 * Connection settings must be set before calling this method.
	 * 
	 * @see #setSettings(ConnectionSettings)
	 */
	void connect();

	/**
	 * Disconnect from the server
	 */
	void disconnect();

	/**
	 * Obtain the stream settings. This is the actual object used in the
	 * connection and should not be changed.
	 * 
	 * @return the current stream settings
	 */
	@Nullable
	StreamSettings getStreamSettings();

	/**
	 * Check if the connection has errors.
	 * 
	 * @return true if the connection has errors
	 */
	boolean hasErrors();

	/**
	 * Check if the connection is connected.
	 * 
	 * @return true if is connected
	 */
	boolean isConnected();

	/**
	 * Pause the connection and return a stream settings object that can be
	 * serialized to restore the session.
	 * 
	 * @return StreamSettings object if a stream is present, null otherwise
	 */
	@Nullable
	StreamSettings pause();

	/**
	 * Restart the stream.
	 * 
	 * Usually you don't need this method.
	 */
	void restartStream();

	/**
	 * The opposite to the pause method.
	 * 
	 * Used to pause the session between web page changes.
	 * 
	 * @param settings
	 *            the paused stream settings
	 * @return true if the connection is resumed
	 */
	boolean resume(StreamSettings settings);

	/**
	 * Send a packet into the connection channel.
	 * 
	 * @param packet
	 *            the packet to be sent
	 */
	void send(HasXML packet);

	/**
	 * Set the connection settings.
	 * 
	 * This method MUST be called before calling {@link #connect()}.
	 * 
	 * You can use the BrowserModule to configure the connection via HTML meta
	 * tags (recommended)
	 * 
	 * @param settings
	 *            the connection settings
	 */
	void setSettings(ConnectionSettings settings);
	
}
