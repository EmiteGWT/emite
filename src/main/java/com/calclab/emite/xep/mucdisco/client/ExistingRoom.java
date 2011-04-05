package com.calclab.emite.xep.mucdisco.client;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;

/**
 * A existing room
 */
public class ExistingRoom implements Comparable<ExistingRoom> {
    private final String name;
    private final XmppURI uri;

    public ExistingRoom(XmppURI uri, String name) {
	assert uri != null : "URI can't be null in existing rooms";
	this.uri = uri;
	if (name == null) {
	    this.name = uri.toString();
	} else {
	    this.name = name;
	}
    }

    @Override
    public int compareTo(ExistingRoom o) {
	return name.compareTo(o.name);
    }

    public String getName() {
	return name;
    }

    public XmppURI getUri() {
	return uri;
    }

}