package com.calclab.emite.xep.storage.client;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.suco.client.events.Listener;

/**
 * Implements http://xmpp.org/extensions/xep-0049.html
 */
public class PrivateStorageManager {
	private final Session session;

	public PrivateStorageManager(Session session) {
		this.session = session;
	}
	
	public void retrieve(String namespace, Listener<SimpleStorageData> listener) {
		
	}

	public void store(SimpleStorageData data, Listener<IQ.Type> listener) {
		
	}
}
