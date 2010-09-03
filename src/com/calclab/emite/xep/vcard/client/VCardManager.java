package com.calclab.emite.xep.vcard.client;

import com.calclab.emite.core.client.xmpp.session.IQResponseHandler;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xep.vcard.client.events.VCardResponseEvent;
import com.calclab.emite.xep.vcard.client.events.VCardResponseHandler;
import com.calclab.suco.client.events.Listener;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.inject.Inject;

public class VCardManager {

    private static final String ID_PREFIX = "vcard";
    private final XmppSession session;

    @Inject
    public VCardManager(final XmppSession session) {
	this.session = session;
    }

    public void addOnVCardReceived(final Listener<VCardResponse> listener) {
	addVCardResponseHandler(new VCardResponseHandler() {
	    @Override
	    public void onVCardResponse(VCardResponseEvent event) {
		listener.onEvent(event.getvCardResponse());
	    }
	});
    }

    public HandlerRegistration addVCardResponseHandler(VCardResponseHandler handler) {
	return VCardResponseEvent.bind(session.getEventBus(), handler);
    }

    public void getUserVCard(final XmppURI userJid, final Listener<VCardResponse> listener) {
	final IQ iq = new IQ(IQ.Type.get);
	iq.addChild(VCard.VCARD, VCard.DATA_XMLS);
	iq.setFrom(session.getCurrentUser());
	iq.setTo(userJid);

	session.sendIQ(ID_PREFIX, iq, new IQResponseHandler() {
	    @Override
	    public void onIQ(IQ iq) {
		handleVCard(iq, listener);
	    }
	});

    }

    public void requestOwnVCard(final Listener<VCardResponse> listener) {
	final IQ iq = new IQ(IQ.Type.get);
	iq.addChild(VCard.VCARD, VCard.DATA_XMLS);
	iq.setFrom(session.getCurrentUser());
	session.sendIQ(ID_PREFIX, iq, new IQResponseHandler() {
	    @Override
	    public void onIQ(IQ iq) {
		handleVCard(iq, listener);
	    }
	});
    }

    public void updateOwnVCard(final VCard vcard, final Listener<VCardResponse> listener) {
	final IQ iq = new IQ(IQ.Type.set);
	iq.addChild(vcard);
	session.sendIQ(ID_PREFIX, iq, new IQResponseHandler() {
	    @Override
	    public void onIQ(IQ iq) {
		handleVCard(iq, listener);
	    }
	});

    }

    protected void handleVCard(final IQ result, final Listener<VCardResponse> listener) {
	final VCardResponse response = new VCardResponse(result);
	if (listener != null) {
	    listener.onEvent(response);
	}
	session.getEventBus().fireEvent(new VCardResponseEvent(response));
    }
}
