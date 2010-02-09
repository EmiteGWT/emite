package com.calclab.emite.xep.vcard.client;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.events.Event;
import com.calclab.suco.client.events.Listener;

public class VCardManager {

	private final Session session;
	private Event<VCardResponse> onVCardResponse;

	public VCardManager(Session session) {
		this.session = session;
		this.onVCardResponse = new Event<VCardResponse>("vcard.response");
	}
	
	public void getOwnVCard(final Listener<VCardResponse> listener) {
		IQ iq  = null;
		session.sendIQ("vcard", iq, new Listener<IPacket>() {
			@Override
			public void onEvent(IPacket parameter) {
				handleVCard(parameter, listener);
			}
		});
	}
	
	protected void handleVCard(IPacket result, Listener<VCardResponse> listener) {
		VCardResponse response = new VCardResponse(result);
		if (listener != null) 
			listener.onEvent(response);
		onVCardResponse.fire(response);
	}

	public void getUserVCard(XmppURI userJid, final Listener<VCardResponse> listener) {
		IQ iq = null;
		session.sendIQ("vcard", iq, new Listener<IPacket>() {
			@Override
			public void onEvent(IPacket parameter) {
				handleVCard(parameter, listener);
			}
		});
	}
	
	public void updateOwnVCard(final VCard vcard, final Listener<VCardResponse> listener) {
		IQ iq = null;
		session.sendIQ("vcard", iq, new Listener<IPacket>() {
			@Override
			public void onEvent(IPacket parameter) {
				handleUpdateVCard(vcard, parameter, listener);
			}
		});
		
	}
	
	protected void handleUpdateVCard(VCard vcard, IPacket parameter,
			Listener<VCardResponse> listener) {
		
	}

	public void addOnVCardReceived(Listener<VCardResponse> listener) {
		onVCardResponse.add(listener);
	}
}
