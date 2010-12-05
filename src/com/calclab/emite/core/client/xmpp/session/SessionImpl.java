/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008-2009 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emite.core.client.xmpp.session;

import com.calclab.emite.core.client.bosh.StreamSettings;
import com.calclab.emite.core.client.events.IQEvent;
import com.calclab.emite.core.client.events.IQHandler;
import com.calclab.emite.core.client.events.MessageEvent;
import com.calclab.emite.core.client.events.MessageHandler;
import com.calclab.emite.core.client.events.PresenceEvent;
import com.calclab.emite.core.client.events.PresenceHandler;
import com.calclab.emite.core.client.events.StateChangedEvent;
import com.calclab.emite.core.client.events.StateChangedHandler;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.events.Listener;
import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Default Session implementation. Use Session interface instead.
 */
@Singleton
public class SessionImpl implements Session {
    private final XmppSession delegate;

    // private final HashMap<State, String> stateToString;

    @Inject
    public SessionImpl(final XmppSession delegate) {
	GWT.log("Create SessionImpl");
	// this.stateToString = new HashMap<State, String>();
	// stateToString.put(State.authorized, SessionStates.authorized);
	// stateToString.put(State.connecting, SessionStates.connecting);
	// stateToString.put(State.disconnected, SessionStates.disconnected);
	// stateToString.put(State.error, SessionStates.error);
	// stateToString.put(State.loggedIn, SessionStates.loggedIn);
	// stateToString.put(State.loggingOut, SessionStates.loggingOut);
	// stateToString.put(State.notAuthorized, SessionStates.notAuthorized);
	// stateToString.put(State.ready, SessionStates.ready);
	// stateToString.put(State.resume, SessionStates.resume);
	this.delegate = delegate;
    }

    @Override
    public XmppURI getCurrentUser() {
	return delegate.getCurrentUserURI();
    }

    @Override
    public State getState() {
	return convertState(delegate.getSessionState());
    }

    @Override
    public boolean isLoggedIn() {
	return delegate.isReady();
    }

    @Override
    public void login(final Credentials credentials) {
	delegate.login(credentials);
    }

    @Override
    public void login(final XmppURI uri, final String password) {
	delegate.login(uri, password);
    }

    @Override
    public void logout() {
	delegate.logout();
    }

    @Override
    public void onIQ(final Listener<IQ> listener) {
	delegate.addIQReceivedHandler(new IQHandler() {
	    @Override
	    public void onPacket(final IQEvent event) {
		listener.onEvent(event.getIQ());
	    }
	});
    }

    @Override
    public void onMessage(final Listener<Message> listener) {
	delegate.addMessageReceivedHandler(new MessageHandler() {
	    @Override
	    public void onMessage(final MessageEvent event) {
		listener.onEvent(event.getMessage());
	    }
	});
    }

    @Override
    public void onPresence(final Listener<Presence> listener) {
	delegate.addPresenceReceivedHandler(new PresenceHandler() {
	    @Override
	    public void onPresence(final PresenceEvent event) {
		listener.onEvent(event.getPresence());
	    }
	});
    }

    @Override
    public void onStateChanged(final Listener<Session> listener) {
	delegate.addSessionStateChangedHandler(false, new StateChangedHandler() {
	    @Override
	    public void onStateChanged(final StateChangedEvent event) {
		listener.onEvent(SessionImpl.this);
	    }
	});
    }

    @Override
    public StreamSettings pause() {
	return delegate.pause();
    }

    @Override
    public void resume(final XmppURI userURI, final StreamSettings settings) {
	delegate.resume(userURI, settings);
    }

    @Override
    public void send(final IPacket stanza) {
	delegate.send(stanza);
    }

    @Override
    public void sendIQ(final String category, final IQ iq, final Listener<IPacket> listener) {
	delegate.sendIQ(category, iq, new IQResponseHandler() {
	    @Override
	    public void onIQ(final IQ iq) {
		listener.onEvent(iq);
	    }
	});
    }

    @Override
    public void setReady() {
	delegate.setSessionState(SessionStates.ready);
    }

    private State convertState(final String sessionState) {
	try {
	    return State.valueOf(sessionState);
	} catch (final Exception e) {
	    GWT.log("STATE CONVERT ERROR!!!" + sessionState);
	    return State.unknown;
	}
    }

}
