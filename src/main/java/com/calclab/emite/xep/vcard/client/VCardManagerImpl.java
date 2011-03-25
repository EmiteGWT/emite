package com.calclab.emite.xep.vcard.client;

import com.calclab.emite.core.client.xmpp.session.IQResponseHandler;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xep.vcard.client.events.VCardResponseEvent;
import com.calclab.emite.xep.vcard.client.events.VCardResponseHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Implements VCardManager
 * 
 * @see VCardManager
 */
@Singleton
public class VCardManagerImpl implements VCardManager {
    private static final String ID_PREFIX = "vcard";
    private final XmppSession session;

    @Inject
    public VCardManagerImpl(final XmppSession session) {
	this.session = session;
    }

    @Override
    public HandlerRegistration addVCardResponseHandler(VCardResponseHandler handler) {
	return VCardResponseEvent.bind(session.getEventBus(), handler);
    }

    @Override
    public void getUserVCard(XmppURI userJid, final VCardResponseHandler handler) {
	final IQ iq = new IQ(IQ.Type.get);
	iq.addChild(VCard.VCARD, VCard.DATA_XMLS);
	iq.setFrom(session.getCurrentUserURI());
	iq.setTo(userJid);

	session.sendIQ(ID_PREFIX, iq, new IQResponseHandler() {
	    @Override
	    public void onIQ(IQ iq) {
		handleVCard(iq, handler);
	    }
	});

    }

    @Override
    public void requestOwnVCard(final VCardResponseHandler handler) {
	final IQ iq = new IQ(IQ.Type.get);
	iq.addChild(VCard.VCARD, VCard.DATA_XMLS);
	iq.setFrom(session.getCurrentUserURI());
	session.sendIQ(ID_PREFIX, iq, new IQResponseHandler() {
	    @Override
	    public void onIQ(IQ iq) {
		handleVCard(iq, handler);
	    }
	});

    }

    @Override
    public void updateOwnVCard(VCard vcard, final VCardResponseHandler handler) {
	final IQ iq = new IQ(IQ.Type.set);
	iq.addChild(vcard);
	session.sendIQ(ID_PREFIX, iq, new IQResponseHandler() {
	    @Override
	    public void onIQ(IQ iq) {
		handleVCard(iq, handler);
	    }
	});

    }

    protected void handleVCard(final IQ result, final VCardResponseHandler handler) {
	final VCardResponse response = new VCardResponse(result);
	VCardResponseEvent event = new VCardResponseEvent(response);
	if (handler != null) {
	    handler.onVCardResponse(event);
	}
	session.getEventBus().fireEvent(event);
    }
}
