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

package com.calclab.emite.core.session;

import javax.annotation.Nullable;

import com.calclab.emite.core.IQCallback;
import com.calclab.emite.core.XmppURI;
import com.calclab.emite.core.conn.StreamSettings;
import com.calclab.emite.core.events.IQRequestReceivedEvent;
import com.calclab.emite.core.events.IQResponseReceivedEvent;
import com.calclab.emite.core.events.MessageReceivedEvent;
import com.calclab.emite.core.events.PresenceReceivedEvent;
import com.calclab.emite.core.events.SessionStatusChangedEvent;
import com.calclab.emite.core.sasl.Credentials;
import com.calclab.emite.core.stanzas.IQ;
import com.calclab.emite.core.stanzas.Stanza;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * The most important object in Xmpp emite module. You can login, send and
 * receive stanzas. It also allows you to pause and resume the session.
 */
public interface XmppSession {

	/**
	 * Add a handler to know when an IQ request has been received.
	 * 
	 * @param handler
	 *            the handler
	 * @return the handler registration, can be stored in order to remove the
	 *         handler later
	 */
	HandlerRegistration addIQRequestReceivedHandler(IQRequestReceivedEvent.Handler handler);

	/**
	 * Add a handler to know when an IQ response has been received.
	 * 
	 * @param handler
	 *            the handler
	 * @return the handler registration, can be stored in order to remove the
	 *         handler later
	 */
	HandlerRegistration addIQResponseReceivedHandler(IQResponseReceivedEvent.Handler handler);

	/**
	 * Add a handler to know when a Message has been received.
	 * 
	 * @param handler
	 *            the handler
	 * @return the handler registration, can be stored in order to remove the
	 *         handler later
	 */
	HandlerRegistration addMessageReceivedHandler(MessageReceivedEvent.Handler handler);

	/**
	 * Add a handler to know when a Presence has been received.
	 * 
	 * @param handler
	 *            the handler
	 * @return the handler registration, can be stored in order to remove the
	 *         handler later
	 */
	HandlerRegistration addPresenceReceivedHandler(PresenceReceivedEvent.Handler handler);

	/**
	 * Add a handler to track session status changes.
	 * 
	 * If sendCurrent is true, the handler will receive the current session
	 * status just after been added.
	 * 
	 * @param handler
	 *            the handler
	 * @param sendCurrent
	 *            if {@code true}, the current status will be sent to the
	 *            handler
	 * @return the handler registration, can be stored in order to remove the
	 *         handler later
	 */
	HandlerRegistration addSessionStatusChangedHandler(SessionStatusChangedEvent.Handler handler, boolean sendCurrent);

	/**
	 * Returns the current user URI.
	 * 
	 * @return the current user URI, or {@code null} if not available
	 */
	@Nullable
	XmppURI getCurrentUserURI();

	/**
	 * Returns the current session status.
	 * 
	 * @return The current session status.
	 */
	// FIXME: isn't isReady() enough?
	SessionStatus getStatus();

	/**
	 * Set the session status.
	 * 
	 * @param status
	 *            the new session status
	 */
	// FIXME: This should not be public
	void setStatus(SessionStatus status);

	/**
	 * Check if the session is ready to send stanzas.
	 * 
	 * @return {@code true} if the session is ready, {@code false} otherwise
	 */
	boolean isReady();

	/**
	 * Start a login process with the given credentials.
	 * 
	 * You can use {@link Credentials#ANONYMOUS} to perform an anonymous login.
	 * 
	 * @param credentials the credentials to use at login
	 */
	void login(Credentials credentials);

	/**
	 * Start a logout process in the current session.
	 */
	void logout();

	/**
	 * Pause the current session.
	 * 
	 * You can use the returned object information to resume the session later.
	 * 
	 * @see #resume(XmppURI, StreamSettings)
	 * @return the StreamSettings if the session was ready, {@code null} otherwise
	 */
	@Nullable
	StreamSettings pause();

	/**
	 * Resume a previously paused session.
	 * 
	 * @see #pause()
	 * @param userURI
	 *            the user URI for the session
	 * @param settings
	 *            the stream settings given by the pause method
	 */
	void resume(XmppURI userURI, StreamSettings settings);

	/**
	 * Send a stanza to the server.
	 * 
	 * This method overrides the "from" uri attribute.
	 * 
	 * <b>All the stanzas sent using this method BEFORE the LoggedIn status are
	 * queued and sent AFTER Ready status.</b>
	 * 
	 * @param stanza
	 *            the stanza to be sent
	 */
	void send(Stanza stanza);

	/**
	 * Send a IQ stanza and attach a listener to the response.
	 * 
	 * This method overrides (if present) the given IQ id using the provided
	 * category and an internal sequential number. This method also overrides
	 * (if present) the given 'from' attribute.
	 * 
	 * If the listener is null, the IQ is sent but no callback called.
	 * 
	 * <b>All the stanzas sent using this method BEFORE the LoggedIn status are
	 * queued and sent AFTER Ready status.</b>
	 * 
	 * @param category
	 *            a uniqe-per-component string that allows the session to
	 *            generate a sequential and uniqe id for the IQ
	 * @param iq
	 *            the IQ stanza to be sent
	 * @param iqHandler
	 *            the handler called when a IQ of type "result" arrives to the
	 *            server. After the invocation, the handler is discarded. It CAN
	 *            be null
	 * 
	 */
	void sendIQ(String category, IQ iq, @Nullable IQCallback iqHandler);

}
