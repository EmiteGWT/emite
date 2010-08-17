package com.calclab.emite.xtesting;

import java.util.ArrayList;

import com.calclab.emite.core.client.bosh.StreamSettings;
import com.calclab.emite.core.client.conn.XmppConnectionBoilerPlate;
import com.calclab.emite.core.client.conn.ConnectionSettings;
import com.calclab.emite.core.client.conn.StanzaReceivedEvent;
import com.calclab.emite.core.client.conn.StanzaSentEvent;
import com.calclab.emite.core.client.conn.XmppConnection;
import com.calclab.emite.core.client.events.GwtEmiteEventBus;
import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.xtesting.matchers.IsPacketLike;
import com.calclab.emite.xtesting.services.TigaseXMLService;

public class XmppConnectionTester extends XmppConnectionBoilerPlate implements XmppConnection {

    private final TigaseXMLService xmler;
    private final ArrayList<IPacket> sent;
    private final ArrayList<IPacket> received;
    private boolean isConnected;
    private boolean paused;
    private boolean streamRestarted;
    private ConnectionSettings settings;

    public XmppConnectionTester() {
	super(new GwtEmiteEventBus());
	xmler = new TigaseXMLService();
	sent = new ArrayList<IPacket>();
	received = new ArrayList<IPacket>();
    }

    @Override
    public void connect() {
	isConnected = true;
    }

    @Override
    public void disconnect() {
	isConnected = false;
    }

    @Override
    public EmiteEventBus getEventBus() {
	return eventBus;
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

    public boolean hasSent(final IPacket packet) {
	final IsPacketLike matcher = new IsPacketLike(packet);
	for (final IPacket stanza : sent) {
	    if (matcher.matches(stanza, System.out)) {
		return true;
	    }
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

    public void receives(final IPacket stanza) {
	received.add(stanza);
	eventBus.fireEvent(new StanzaReceivedEvent(stanza));
    }

    public void receives(final String stanza) {
	receives(xmler.toXML(stanza));
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
    public void send(final IPacket packet) {
	sent.add(packet);
	eventBus.fireEvent(new StanzaSentEvent(packet));
    }

    public void send(final String stanza) {
	send(xmler.toXML(stanza));
    }

    @Override
    public void setSettings(final ConnectionSettings settings) {
	this.settings = settings;
    }

}
