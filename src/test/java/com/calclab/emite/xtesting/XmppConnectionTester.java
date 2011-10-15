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

package com.calclab.emite.xtesting;

import java.util.ArrayList;
import java.util.List;

import com.calclab.emite.core.client.conn.ConnectionSettings;
import com.calclab.emite.core.client.conn.bosh.StreamSettings;
import com.calclab.emite.core.client.conn.bosh.XmppBoshConnection;
import com.calclab.emite.core.client.events.PacketReceivedEvent;
import com.calclab.emite.core.client.events.PacketSentEvent;
import com.calclab.emite.core.client.xml.HasXML;
import com.calclab.emite.core.client.xml.XMLBuilder;
import com.calclab.emite.core.client.xml.XMLPacket;
import com.calclab.emite.xtesting.matchers.IsPacketLike;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class XmppConnectionTester extends XmppBoshConnection {

	private final List<XMLPacket> sent;
	private final List<XMLPacket> received;
	private boolean isConnected;
	private boolean paused;
	private boolean streamRestarted;
	private ConnectionSettings settings;

	public XmppConnectionTester() {
		super(new SimpleEventBus());
		sent = new ArrayList<XMLPacket>();
		received = new ArrayList<XMLPacket>();
	}

	@Override
	public void connect() {
		isConnected = true;
	}

	@Override
	public void disconnect() {
		isConnected = false;
	}

	public int getSentSize() {
		return sent.size();
	}

	public ConnectionSettings getSettings() {
		return settings;
	}

	@Override
	public boolean hasErrors() {
		return false;
	}

	public boolean hasSent(final XMLPacket packet) {
		final IsPacketLike matcher = new IsPacketLike(packet);
		for (final XMLPacket stanza : sent) {
			if (matcher.matches(stanza, System.out))
				return true;
		}
		return false;
	}

	@Override
	public boolean isConnected() {
		return isConnected;
	}

	public boolean isStreamRestarted() {
		return streamRestarted;
	}

	@Override
	public StreamSettings pause() {
		paused = true;
		return null;
	}

	public void receives(final XMLPacket stanza) {
		received.add(stanza);
		eventBus.fireEvent(new PacketReceivedEvent(stanza));
	}

	public void receives(final String stanza) {
		receives(XMLBuilder.fromXML(stanza));
	}

	@Override
	public void restartStream() {
		streamRestarted = true;
	}

	@Override
	public boolean resume(final StreamSettings settings) {
		if (paused) {
			paused = false;
			return true;
		}
		return false;
	}

	@Override
	public void send(final HasXML packet) {
		sent.add(packet.getXML());
		eventBus.fireEventFromSource(new PacketSentEvent(packet.getXML()), this);
	}

	public void send(final String stanza) {
		send(XMLBuilder.fromXML(stanza));
	}

	@Override
	public void setSettings(final ConnectionSettings settings) {
		this.settings = settings;
	}

}
