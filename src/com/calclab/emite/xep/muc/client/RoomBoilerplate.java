package com.calclab.emite.xep.muc.client;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.calclab.emite.core.client.events.PresenceHandler;
import com.calclab.emite.core.client.events.PresenceReceivedEvent;
import com.calclab.emite.core.client.events.ChangedEvent.ChangeTypes;
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
import com.calclab.emite.xep.muc.client.subject.RoomSubjectChangedEvent;
import com.calclab.emite.xep.muc.client.subject.RoomSubjectChangedHandler;
import com.calclab.suco.client.events.Listener;
import com.calclab.suco.client.events.Listener2;
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

    /**
     * Add a handler to know when a room invitation has been sent
     * 
     * @param handler
     * @return
     */
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
     * Add a listener to know when an invitation was sent Use
     * addRoomInvitationSentHandler
     */
    @Override
    @Deprecated
    public void onInvitationSent(final Listener2<XmppURI, String> listener) {
	addRoomInvitationSentHandler(new RoomInvitationSentHandler() {
	    @Override
	    public void onRoomInvitationSent(RoomInvitationSentEvent event) {
		listener.onEvent(event.getUserJid(), event.getReasonText());
	    }
	});
    }

    /**
     * Use addOccupantChangedHandler
     */
    @Deprecated
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

    /**
     * Use addOccupantChangedHandler
     */
    @Deprecated
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

    /**
     * Use addOccupantChangedHandler
     */
    @Deprecated
    @Override
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

    /**
     * Use RoomSubject.addRoomSubjectChangedHandler
     */
    @Deprecated
    public void onSubjectChanged(final Listener2<Occupant, String> listener) {
	RoomSubject.addRoomSubjectChangedHandler(this, new RoomSubjectChangedHandler() {
	    @Override
	    public void onSubjectChanged(RoomSubjectChangedEvent event) {
		Occupant occupant = getOccupantByOccupantUri(event.getOccupantUri());
		listener.onEvent(occupant, event.getSubject());
	    }
	});
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
	occupantsByUserUri.put(occupant.getUserUri().getJID(), occupant);
	chatEventBus.fireEvent(new OccupantChangedEvent(ChangeTypes.added, occupant));
    }

    protected void removeOccupant(final XmppURI occupantUri) {
	final Occupant occupant = occupantsByOccupantUri.remove(occupantUri);
	if (occupant != null) {
	    occupantsByUserUri.remove(occupant.getUserUri().getJID());
	    chatEventBus.fireEvent(new OccupantChangedEvent(ChangeTypes.removed, occupant));
	}
    }
}
