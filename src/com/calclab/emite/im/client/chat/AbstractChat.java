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
package com.calclab.emite.im.client.chat;

import java.util.HashMap;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.events.Event;
import com.calclab.suco.client.events.Listener;

public abstract class AbstractChat implements Chat {

    protected final XmppURI uri;
    protected State state;
    private final Event<State> onStateChanged;
    private final Event<Message> onBeforeReceive;
    private final Session session;
    private final HashMap<Class<?>, Object> data;
    private final Event<Message> onMessageSent;
    private final Event<Message> onMessageReceived;
    private final Event<Message> onBeforeSend;
    private final XmppURI starter;

    public AbstractChat(final Session session, final XmppURI uri, final XmppURI starter) {
	this.session = session;
	this.uri = uri;
	this.starter = starter;
	this.data = new HashMap<Class<?>, Object>();
	this.state = Chat.State.locked;
	this.onStateChanged = new Event<State>("chat:onStateChanged");
	this.onMessageSent = new Event<Message>("chat:onMessageSent");
	this.onMessageReceived = new Event<Message>("chat:onMessageReceived");
	this.onBeforeSend = new Event<Message>("chat:onBeforeSend");
	this.onBeforeReceive = new Event<Message>("chat:onBeforeReceive");
    }

    @SuppressWarnings("unchecked")
    public <T> T getData(final Class<T> type) {
	return (T) data.get(type);
    }

    public State getState() {
	return state;
    }

    public XmppURI getURI() {
	return uri;
    }

    public boolean isInitiatedByMe() {
	return starter.equals(session.getCurrentUser());
    }

    public void onBeforeReceive(final Listener<Message> listener) {
	onBeforeReceive.add(listener);
    }

    public void onBeforeSend(final Listener<Message> listener) {
	onBeforeSend.add(listener);
    }

    public void onMessageReceived(final Listener<Message> listener) {
	onMessageReceived.add(listener);
    }

    public void onMessageSent(final Listener<Message> listener) {
	onMessageSent.add(listener);
    }

    public void onStateChanged(final Listener<State> listener) {
	onStateChanged.add(listener);
    }

    public void send(final Message message) {
	message.setFrom(session.getCurrentUser());
	onBeforeSend.fire(message);
	session.send(message);
	onMessageSent.fire(message);
    }

    @SuppressWarnings("unchecked")
    public <T> T setData(final Class<T> type, final T value) {
	return (T) data.put(type, value);
    }

    protected void fireBeforeReceive(Message message) {
	onBeforeReceive.fire(message);
    }

    protected void receive(final Message message) {
	onBeforeReceive.fire(message);
	onMessageReceived.fire(message);
    }

    protected void setState(final State state) {
	if (this.state != state) {
	    this.state = state;
	    onStateChanged.fire(state);
	}
    }
}
