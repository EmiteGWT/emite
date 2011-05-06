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

package com.calclab.emite.xep.muc.client;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.calclab.emite.core.client.events.ChangedEvent.ChangeTypes;
import com.calclab.emite.core.client.events.PresenceHandler;
import com.calclab.emite.core.client.events.PresenceReceivedEvent;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.AbstractChat;
import com.calclab.emite.im.client.chat.ChatProperties;
import com.calclab.emite.xep.muc.client.events.BeforeRoomInvitationSendEvent;
import com.calclab.emite.xep.muc.client.events.BeforeRoomInvitationSendHandler;
import com.calclab.emite.xep.muc.client.events.OccupantChangedEvent;
import com.calclab.emite.xep.muc.client.events.OccupantChangedHandler;
import com.calclab.emite.xep.muc.client.events.RoomInvitationSentEvent;
import com.calclab.emite.xep.muc.client.events.RoomInvitationSentHandler;
import com.calclab.emite.xep.muc.client.subject.RoomSubject;
import com.google.gwt.event.shared.HandlerRegistration;

abstract class RoomBoilerplate extends AbstractChat implements Room {
	private final HashMap<XmppURI, Occupant> occupantsByOccupantUri;
	private final LinkedHashMap<XmppURI, Occupant> occupantsByUserUri;

	public RoomBoilerplate(XmppSession session, ChatProperties properties) {
		super(session, properties);
		occupantsByOccupantUri = new LinkedHashMap<XmppURI, Occupant>();
		occupantsByUserUri = new LinkedHashMap<XmppURI, Occupant>();
	}

	@Override
	public HandlerRegistration addBeforeRoomInvitationSendHandler(BeforeRoomInvitationSendHandler handler) {
		return BeforeRoomInvitationSendEvent.bind(chatEventBus, handler);
	}

	/**
	 * Add a handler to know when a occupant has changed
	 * 
	 * @param handler
	 * @return
	 */
	@Override
	public HandlerRegistration addOccupantChangedHandler(OccupantChangedHandler handler) {
		return OccupantChangedEvent.bind(chatEventBus, handler);
	}

	@Override
	public HandlerRegistration addPresenceReceivedHandler(PresenceHandler handler) {
		return PresenceReceivedEvent.bind(chatEventBus, handler);
	}

	@Override
	public HandlerRegistration addRoomInvitationSentHandler(RoomInvitationSentHandler handler) {
		return RoomInvitationSentEvent.bind(chatEventBus, handler);
	}

	@Override
	public Occupant getOccupantByOccupantUri(XmppURI occupantUri) {
		return occupantsByOccupantUri.get(occupantUri);
	}

	@Override
	@Deprecated
	public Occupant getOccupantByURI(final XmppURI uri) {
		return getOccupantByOccupantUri(uri);
	}

	@Override
	public Occupant getOccupantByUserUri(XmppURI userUri) {
		return occupantsByUserUri.get(userUri.getJID());
	}

	@Override
	public Collection<Occupant> getOccupants() {
		return occupantsByOccupantUri.values();
	}

	@Override
	public int getOccupantsCount() {
		return occupantsByOccupantUri.size();
	}

	/**
	 * Use RoomSubject.requestSubjectChange
	 */
	@Override
	@Deprecated
	public void setSubject(String newSubject) {
		RoomSubject.requestSubjectChange(this, newSubject);
	}

	protected void addOccupant(Occupant occupant) {
		occupantsByOccupantUri.put(occupant.getOccupantUri(), occupant);
		XmppURI userUri = occupant.getUserUri();
		if (userUri != null) {
			occupantsByUserUri.put(userUri.getJID(), occupant);
		}
		chatEventBus.fireEvent(new OccupantChangedEvent(ChangeTypes.added, occupant));
	}

	protected void removeOccupant(final XmppURI occupantUri) {
		final Occupant occupant = occupantsByOccupantUri.remove(occupantUri);
		if (occupant != null) {
			XmppURI userUri = occupant.getUserUri();
			if (userUri != null) {
				occupantsByUserUri.remove(userUri.getJID());
			}
			chatEventBus.fireEvent(new OccupantChangedEvent(ChangeTypes.removed, occupant));
		}
	}
}
