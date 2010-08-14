package com.calclab.emite.xtesting;

import java.util.ArrayList;

import com.calclab.emite.core.client.bosh.StreamSettings;
import com.calclab.emite.core.client.bosh.XmppBoshConnection;
import com.calclab.emite.core.client.conn.AbstractConnection;
import com.calclab.emite.core.client.conn.Connection;
import com.calclab.emite.core.client.conn.ConnectionEvent;
import com.calclab.emite.core.client.conn.ConnectionHandler;
import com.calclab.emite.core.client.conn.ConnectionSettings;
import com.calclab.emite.core.client.conn.StanzaReceivedEvent;
import com.calclab.emite.core.client.conn.StanzaReceivedHandler;
import com.calclab.emite.core.client.conn.StanzaSentEvent;
import com.calclab.emite.core.client.conn.StanzaSentHandler;
import com.calclab.emite.core.client.events.DefaultEmiteEventBus;
import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.xtesting.matchers.IsPacketLike;
import com.calclab.emite.xtesting.services.TigaseXMLService;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Object of this class are used to test against connection (for example, to
 * test the Session)
 * 
 */
public class ConnectionTester extends AbstractConnection implements Connection {

    private boolean isConnected;
    private boolean paused;
    private final TigaseXMLService xmler;
    private final ArrayList<IPacket> sent;
    private boolean streamRestarted;
    private final ArrayList<IPacket> received;
    private ConnectionSettings settings;
    private final EmiteEventBus eventBus;

    public ConnectionTester() {
	super(new XmppBoshConnection(new DefaultEmiteEventBus(), new ServicesTester()));
	eventBus = delegate.getEventBus();
	xmler = new TigaseXMLService();
	sent = new ArrayList<IPacket>();
	received = new ArrayList<IPacket>();
    }

    @Override
    public HandlerRegistration addConnectionHandler(final ConnectionHandler handler) {
	return eventBus.addHandler(ConnectionEvent.getType(), handler);
    }

    @Override
    public HandlerRegistration addStanzaReceivedHandler(final StanzaReceivedHandler handler) {
	return eventBus.addHandler(StanzaReceivedEvent.getType(), handler);
    }

    @Override
    public HandlerRegistration addStanzaSentHandler(final StanzaSentHandler handler) {
	return eventBus.addHandler(StanzaSentEvent.getType(), handler);
    }

    @Override
    public void connect() {
	isConnected = true;
    }

    @Override
    public void disconnect() {
	isConnected = false;
    }

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
