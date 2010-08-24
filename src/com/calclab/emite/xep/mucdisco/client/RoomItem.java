package com.calclab.emite.xep.mucdisco.client;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;

/**
 * A room item class
 */
public class RoomItem {
    public final XmppURI uri;
    public final String roomName;

    public RoomItem(XmppURI uri, String roomName) {
	this.uri = uri;
	this.roomName = roomName;
    }
}
