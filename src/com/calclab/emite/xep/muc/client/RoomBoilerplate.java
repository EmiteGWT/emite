package com.calclab.emite.xep.muc.client;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.calclab.emite.core.client.events.ChangedEvent.ChangeTypes;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.AbstractChat;
import com.calclab.emite.im.client.chat.ChatProperties;
import com.calclab.emite.xep.muc.client.events.OccupantChangedEvent;
import com.calclab.emite.xep.muc.client.events.OccupantChangedHandler;
import com.calclab.emite.xep.muc.client.events.RoomInvitationSentEvent;
import com.calclab.emite.xep.muc.client.events.RoomInvitationSentHandler;
import com.calclab.emite.xep.muc.client.events.RoomSubjectChangedEvent;
import com.calclab.emite.xep.muc.client.events.RoomSubjectChangedHandler;
import com.calclab.suco.client.events.Listener;
import com.calclab.suco.client.events.Listener2;
import com.google.gwt.event.shared.HandlerRegistration;

abstract class RoomBoilerplate extends AbstractChat implements Room {
    protected final HashMap<XmppURI, Occupant> occupantsByURI;

    public RoomBoilerplate(XmppSession session, ChatProperties properties) {
	super(session, properties);
	occupantsByURI = new LinkedHashMap<XmppURI, Occupant>();
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

    /**
     * Add a handler to know when a room invitation has been sent
     * 
     * @param handler
     * @return
     */
    public HandlerRegistration addRoomInvitationSentHandler(RoomInvitationSentHandler handler) {
	return RoomInvitationSentEvent.bind(chatEventBus, handler);
    }

    /**
     * Add a handler to know when the subject of the room changes
     * 
     * @param handler
     * @return
     */
    public HandlerRegistration addRoomSubjectChangedHandler(RoomSubjectChangedHandler handler) {
	return RoomSubjectChangedEvent.bind(chatEventBus, handler);
    }

    @Override
    public Occupant getOccupantByURI(final XmppURI uri) {
	return occupantsByURI.get(uri);
    }

    @Override
    public Collection<Occupant> getOccupants() {
	return occupantsByURI.values();
    }

    @Override
    public int getOccupantsCount() {
	return occupantsByURI.size();
    }

    /**
     * Add a listener to know when an invitation was sent
     */
    @Override
    public void onInvitationSent(final Listener2<XmppURI, String> listener) {
	addRoomInvitationSentHandler(new RoomInvitationSentHandler() {
	    @Override
	    public void onRoomInvitationSent(RoomInvitationSentEvent event) {
		listener.onEvent(event.getUserJid(), event.getReasonText());
	    }
	});
    }

    @Override
    public void onOccupantAdded(final Listener<Occupant> listener) {
	addOccupantChangedHandler(new OccupantChangedHandler() {
	    @Override
	    public void onOccupantChanged(OccupantChangedEvent event) {
		if (event.is(ChangeTypes.added)) {
		    listener.onEvent(event.getOccupant());
		}
	    }
	});
    }

    @Override
    public void onOccupantModified(final Listener<Occupant> listener) {
	addOccupantChangedHandler(new OccupantChangedHandler() {
	    @Override
	    public void onOccupantChanged(OccupantChangedEvent event) {
		if (event.is(ChangeTypes.modified)) {
		    listener.onEvent(event.getOccupant());
		}
	    }
	});
    }

    public void onOccupantRemoved(final Listener<Occupant> listener) {
	addOccupantChangedHandler(new OccupantChangedHandler() {
	    @Override
	    public void onOccupantChanged(OccupantChangedEvent event) {
		if (event.is(ChangeTypes.removed)) {
		    listener.onEvent(event.getOccupant());
		}
	    }
	});
    }

    public void onSubjectChanged(final Listener2<Occupant, String> listener) {
	addRoomSubjectChangedHandler(new RoomSubjectChangedHandler() {
	    @Override
	    public void onSubjectChanged(RoomSubjectChangedEvent event) {
		listener.onEvent(event.getOccupant(), event.getSubject());
	    }
	});
    }
}
