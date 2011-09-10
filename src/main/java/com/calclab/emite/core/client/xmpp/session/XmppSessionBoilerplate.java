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

package com.calclab.emite.core.client.xmpp.session;

import com.calclab.emite.core.client.events.BeforeStanzaSentEvent;
import com.calclab.emite.core.client.events.IQReceivedEvent;
import com.calclab.emite.core.client.events.MessageReceivedEvent;
import com.calclab.emite.core.client.events.PresenceReceivedEvent;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

public abstract class XmppSessionBoilerplate implements XmppSession {
	
	protected final EventBus eventBus;
	private SessionState state;

	public XmppSessionBoilerplate(final EventBus eventBus) {
		this.eventBus = eventBus;
		state = SessionState.disconnected;
	}

	@Override
	public HandlerRegistration addBeforeStanzaSentHandler(final BeforeStanzaSentEvent.Handler handler) {
		return eventBus.addHandlerToSource(BeforeStanzaSentEvent.TYPE, this, handler);
	}

	@Override
	public HandlerRegistration addIQReceivedHandler(final IQReceivedEvent.Handler handler) {
		return eventBus.addHandlerToSource(IQReceivedEvent.TYPE, this, handler);
	}

	@Override
	public HandlerRegistration addMessageReceivedHandler(final MessageReceivedEvent.Handler handler) {
		return eventBus.addHandlerToSource(MessageReceivedEvent.TYPE, this, handler);
	}

	@Override
	public HandlerRegistration addPresenceReceivedHandler(final PresenceReceivedEvent.Handler handler) {
		return eventBus.addHandlerToSource(PresenceReceivedEvent.TYPE, this, handler);
	}

	@Override
	public HandlerRegistration addSessionStateChangedHandler(final boolean sendCurrent, final SessionStateChangedEvent.Handler handler) {
		if (sendCurrent) {
			handler.onSessionStateChanged(new SessionStateChangedEvent(getSessionState()));
		}
		
		return eventBus.addHandlerToSource(SessionStateChangedEvent.TYPE, this, handler);
	}

	@Override
	public SessionState getSessionState() {
		return state;
	}

	@Override
	public boolean isState(final SessionState expectedState) {
		return state.equals(expectedState);
	}

	@Override
	public void setSessionState(final SessionState newState) {
		if (!newState.equals(state)) {
			state = newState;
			eventBus.fireEventFromSource(new SessionStateChangedEvent(newState), this);
		}
	}
	
	@Override
	public void login(final XmppURI uri, final String password) {
		login(new Credentials(uri, password));
	}
}
