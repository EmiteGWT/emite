package com.calclab.emite.xtesting;

import java.util.ArrayList;

import com.calclab.emite.core.client.bosh.AbstractConnection;
import com.calclab.emite.core.client.bosh.Connection;
import com.calclab.emite.core.client.bosh.StreamSettings;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.xtesting.matchers.IsPacketLike;
import com.calclab.emite.xtesting.services.TigaseXMLService;

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

    public ConnectionTester() {
	xmler = new TigaseXMLService();
	sent = new ArrayList<IPacket>();
	received = new ArrayList<IPacket>();
    }

    @Override
    public void connect() {
	this.isConnected = true;
    }

    @Override
    public void disconnect() {
	this.isConnected = false;
    }

    public int getSentSize() {
	return sent.size();
    }

    public boolean hasSent(IPacket packet) {
	IsPacketLike matcher = new IsPacketLike(packet);
	for (IPacket stanza : sent) {
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
	this.paused = true;
	return null;
    }

    public void receives(IPacket stanza) {
	received.add(stanza);
	fireStanzaReceived(stanza);
    }

    public void receives(String stanza) {
	fireStanzaReceived(xmler.toXML(stanza));
    }

    @Override
    public void restartStream() {
	streamRestarted = true;
    }

    @Override
    public boolean resume(StreamSettings settings) {
	if (paused) {
	    paused = false;
	    return true;
	}
	return false;
    }

    @Override
    public void send(IPacket packet) {
	sent.add(packet);
	fireStanzaSent(packet);
    }

    public void send(String stanza) {
	send(xmler.toXML(stanza));
    }

}
