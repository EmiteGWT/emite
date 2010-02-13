package com.calclab.emite.xep.storage.client;

import com.calclab.emite.core.client.packet.Packet;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.suco.client.events.Listener;

/**
 * Implements http://xmpp.org/extensions/xep-0049.html
 */
public class PrivateStorageManager extends AbstractIQManager {
    private static final String XMLNS = "jabber:iq:private";
    private static final String ID = "priv";

    public PrivateStorageManager(final Session session) {
	super(XMLNS, ID, session);
    }

    public void retrieve(final SimpleStorageData data, final Listener<IQResponse> listener) {
	final Packet query = new Packet("query", XMLNS);
	query.addChild(data);
	get(listener, query);
    }

    public void store(final SimpleStorageData data, final Listener<IQResponse> listener) {
	final Packet query = new Packet("query", XMLNS);
	query.addChild(data);
	set(listener, query);
    }
}
