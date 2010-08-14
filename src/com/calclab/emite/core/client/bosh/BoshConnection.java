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
package com.calclab.emite.core.client.bosh;

import com.calclab.emite.core.client.conn.AbstractConnection;
import com.calclab.emite.core.client.conn.Connection;
import com.calclab.emite.core.client.conn.ConnectionHandler;
import com.calclab.emite.core.client.conn.ConnectionSettings;
import com.calclab.emite.core.client.conn.StanzaReceivedHandler;
import com.calclab.emite.core.client.conn.StanzaSentHandler;
import com.calclab.emite.core.client.conn.XmppConnection;
import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.packet.IPacket;
import com.google.gwt.event.shared.HandlerRegistration;

public class BoshConnection extends AbstractConnection implements Connection {

    public BoshConnection(final XmppConnection delegate) {
	super(delegate);
    }

    @Override
    public HandlerRegistration addConnectionHandler(final ConnectionHandler handler) {
	return delegate.addConnectionHandler(handler);
    }

    @Override
    public HandlerRegistration addStanzaReceivedHandler(final StanzaReceivedHandler handler) {
	return delegate.addStanzaReceivedHandler(handler);
    }

    @Override
    public HandlerRegistration addStanzaSentHandler(final StanzaSentHandler handler) {
	return delegate.addStanzaSentHandler(handler);
    }

    @Override
    public void connect() {
	delegate.connect();
    }

    @Override
    public void disconnect() {
	delegate.disconnect();
    }

    @Override
    public EmiteEventBus getEventBus() {
	return delegate.getEventBus();
    }

    @Override
    public boolean hasErrors() {
	return delegate.hasErrors();
    }

    @Override
    public boolean isConnected() {
	return delegate.isConnected();
    }

    @Override
    public StreamSettings pause() {
	return delegate.pause();
    }

    @Override
    public void restartStream() {
	delegate.restartStream();
    }

    @Override
    public boolean resume(final StreamSettings settings) {
	return delegate.resume(settings);
    }

    @Override
    public void send(final IPacket packet) {
	delegate.send(packet);
    }

    @Override
    public void setSettings(final ConnectionSettings settings) {
	delegate.setSettings(settings);
    }
}
