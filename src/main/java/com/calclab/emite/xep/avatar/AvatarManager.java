/*
 * ((e)) emite: A pure Google Web Toolkit XMPP library
 * Copyright (c) 2008-2011 The Emite development team
 * 
 * This file is part of Emite.
 *
 * Emite is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * Emite is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with Emite.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.calclab.emite.xep.avatar;

import com.calclab.emite.base.xml.XMLPacket;
import com.calclab.emite.core.IQCallback;
import com.calclab.emite.core.XmppURI;
import com.calclab.emite.core.events.PresenceReceivedEvent;
import com.calclab.emite.core.session.XmppSession;
import com.calclab.emite.core.stanzas.IQ;
import com.calclab.emite.core.stanzas.Presence;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

@Singleton
public class AvatarManager implements PresenceReceivedEvent.Handler {
	private static final String VCARD = "vCard";
	private static final String XMLNS = "vcard-temp";
	private static final String PHOTO = "PHOTO";
	private static final String TYPE = "TYPE";
	private static final String BINVAL = "BINVAL";

	private final EventBus eventBus;
	private final XmppSession session;

	@Inject
	protected AvatarManager(@Named("emite") final EventBus eventBus, final XmppSession session) {
		this.eventBus = eventBus;
		this.session = session;

		session.addPresenceReceivedHandler(this);
	}

	@Override
	public void onPresenceReceived(final PresenceReceivedEvent event) {
		final Presence presence = event.getPresence();
		if (presence.getXML().hasChild("x", "vcard-temp:x:update")) {
			eventBus.fireEventFromSource(new HashPresenceReceivedEvent(presence), this);
		}
	}

	public HandlerRegistration addAvatarVCardReceivedHandler(final AvatarVCardReceivedEvent.Handler handler) {
		return eventBus.addHandlerToSource(AvatarVCardReceivedEvent.TYPE, this, handler);
	}

	public HandlerRegistration addHashPresenceReceviedHandler(final HashPresenceReceivedEvent.Handler handler) {
		return eventBus.addHandlerToSource(HashPresenceReceivedEvent.TYPE, this, handler);
	}

	/**
	 * When the recipient's client receives the hash of the avatar image, it
	 * SHOULD check the hash to determine if it already has a cached copy of
	 * that avatar image. If not, it retrieves the sender's full vCard in
	 * accordance with the protocol flow described in XEP-0054 (note that this
	 * request is sent to the user's bare JID, not full JID):
	 * 
	 * @param otherJID
	 */
	public void requestVCard(final XmppURI otherJID) {
		final IQ iq = new IQ(IQ.Type.get);
		iq.setTo(otherJID);
		iq.addChild(VCARD, XMLNS);

		session.sendIQ("avatar", iq, new IQCallback() {
			@Override
			public void onIQSuccess(final IQ received) {
				if (received.getXML().hasChild(VCARD, "vcard-temp") && session.getCurrentUserURI().equals(received.getTo())) {
					final XMLPacket photo = received.getExtension(VCARD, "vcard-temp").getFirstChild(PHOTO);
					final String photoType = photo.getChildText(TYPE);
					final String photoBinval = photo.getChildText(BINVAL);
					final AvatarVCard avatar = new AvatarVCard(received.getFrom(), null, photoType, photoBinval);

					eventBus.fireEventFromSource(new AvatarVCardReceivedEvent(avatar), this);
				}
			}

			@Override
			public void onIQFailure(final IQ iq) {
			}
		});

	}

	public void setVCardAvatar(final String photoBinary) {
		final IQ iq = new IQ(IQ.Type.set);
		final XMLPacket vcard = iq.addChild(VCARD, XMLNS);
		vcard.setAttribute("xdbns", XMLNS);
		vcard.setAttribute("prodid", "-//HandGen//NONSGML vGen v1.0//EN");
		vcard.setAttribute("version", "2.0");
		vcard.addChild(PHOTO, null).addChild(BINVAL, null).setText(photoBinary);
		session.sendIQ("avatar", iq, new IQCallback() {
			@Override
			public void onIQSuccess(final IQ iq) {
				// TODO: add behaviour
			}

			@Override
			public void onIQFailure(final IQ iq) {
				// TODO: add behaviour (fire ErrorEvent)
			}
		});
	}

}
