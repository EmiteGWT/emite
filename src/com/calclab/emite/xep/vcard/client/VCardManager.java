package com.calclab.emite.xep.vcard.client;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.events.Event;
import com.calclab.suco.client.events.Listener;

public class VCardManager {

    private static final String ID_PREFIX = "vcard";
    private final Session session;
    private final Event<VCardResponse> onVCardResponse;

    public VCardManager(final Session session) {
	this.session = session;
	this.onVCardResponse = new Event<VCardResponse>("vcard.response");
    }

    public void addOnVCardReceived(final Listener<VCardResponse> listener) {
	onVCardResponse.add(listener);
    }

    public void getUserVCard(final XmppURI userJid, final Listener<VCardResponse> listener) {
	final IQ iq = new IQ(IQ.Type.get);
	iq.addChild(VCard.VCARD, VCard.DATA_XMLS);
	iq.setFrom(session.getCurrentUser());
	iq.setTo(userJid);
	session.sendIQ(ID_PREFIX, iq, new Listener<IPacket>() {
	    @Override
	    public void onEvent(final IPacket parameter) {
		handleVCard(parameter, listener);
	    }
	});
    }

    public void requestOwnVCard(final Listener<VCardResponse> listener) {
	final IQ iq = new IQ(IQ.Type.get);
	iq.addChild(VCard.VCARD, VCard.DATA_XMLS);
	iq.setFrom(session.getCurrentUser());
	session.sendIQ(ID_PREFIX, iq, new Listener<IPacket>() {
	    @Override
	    public void onEvent(final IPacket parameter) {
		handleVCard(parameter, listener);
	    }
	});
    }

    public void updateOwnVCard(final VCard vcard, final Listener<VCardResponse> listener) {
	final IQ iq = new IQ(IQ.Type.set);
	iq.addChild(vcard);
	session.sendIQ(ID_PREFIX, iq, new Listener<IPacket>() {
	    @Override
	    public void onEvent(final IPacket parameter) {
		handleVCard(parameter, listener);
	    }
	});

    }

    protected void handleVCard(final IPacket result, final Listener<VCardResponse> listener) {
	final VCardResponse response = new VCardResponse(result);
	if (listener != null) {
	    listener.onEvent(response);
	}
	onVCardResponse.fire(response);
    }
}
