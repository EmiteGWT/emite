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

import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.events.Event;
import com.calclab.suco.client.events.Listener;

/**
 * Session event plumbing.
 */
public abstract class AbstractSession implements Session {

    private final Event<Session> onStateChanged;
    private final Event<Presence> onPresence;
    private final Event<Message> onMessage;
    private final Event<IQ> onIQ;
    private State state;

    public AbstractSession() {
	onStateChanged = new Event<Session>("session:onStateChanged");
	onPresence = new Event<Presence>("session:onPresence");
	onMessage = new Event<Message>("session:onMessage");
	onIQ = new Event<IQ>("session:onIQ");

	state = State.disconnected;
    }

    public Session.State getState() {
	return state;
    }

    public void login(final XmppURI uri, final String password) {
	login(new Credentials(uri, password, Credentials.ENCODING_NONE));
    }

    public void onIQ(final Listener<IQ> listener) {
	onIQ.add(listener);
    }

    public void onMessage(final Listener<Message> listener) {
	onMessage.add(listener);
    }

    public void onPresence(final Listener<Presence> listener) {
	onPresence.add(listener);
    }

    public void onStateChanged(final Listener<Session> listener) {
	onStateChanged.add(listener);
    }

    protected void fireIQ(final IQ iq) {
	onIQ.fire(iq);
    }

    protected void fireMessage(final Message message) {
	onMessage.fire(message);
    }

    protected void firePresence(final Presence presence) {
	onPresence.fire(presence);
    }

    protected void setState(final State state) {
	this.state = state;
	onStateChanged.fire(this);

    }

}
