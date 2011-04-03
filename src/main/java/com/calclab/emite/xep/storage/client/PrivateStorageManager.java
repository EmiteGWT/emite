package com.calclab.emite.xep.storage.client;

import com.calclab.emite.core.client.xmpp.session.IQResponseHandler;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.core.client.xmpp.stanzas.IQ.Type;
import com.calclab.emite.xep.storage.client.events.PrivateStorageResponseEvent;
import com.calclab.emite.xep.storage.client.events.PrivateStorageResponseHandler;
import com.google.inject.Inject;

/**
 * Implements http://xmpp.org/extensions/xep-0049.html
 */
public class PrivateStorageManager {
    private static final String XMLNS = "jabber:iq:private";
    private static final String ID = "priv";

    private final XmppSession session;

    @Inject
    public PrivateStorageManager(final XmppSession session) {
	this.session = session;
    }

    public void retrieve(final SimpleStorageData data, final PrivateStorageResponseHandler handler) {
	final IQ iq = new IQ(Type.get);
	iq.addQuery(XMLNS).addChild(data);

	session.sendIQ(ID, iq, new IQResponseHandler() {
	    @Override
	    public void onIQ(IQ iq) {
		if (!IQ.isSuccess(iq)) {
		    handler.onStorageResponse(new PrivateStorageResponseEvent(iq));
		}
	    }
	});
    }

    public void store(final SimpleStorageData data, final PrivateStorageResponseHandler handler) {
	final IQ iq = new IQ(Type.set);
	iq.addQuery(XMLNS).addChild(data);

	session.sendIQ(ID, iq, new IQResponseHandler() {
	    @Override
	    public void onIQ(IQ iq) {
		if (!IQ.isSuccess(iq)) {
		    handler.onStorageResponse(new PrivateStorageResponseEvent(iq));
		}
	    }
	});
    }
}
