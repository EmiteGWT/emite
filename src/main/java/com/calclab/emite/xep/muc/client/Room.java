package com.calclab.emite.xep.muc.client;

import java.util.Collection;

import com.calclab.emite.core.client.events.PresenceHandler;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.core.client.xmpp.stanzas.Presence.Show;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.xep.muc.client.events.BeforeRoomInvitationSendHandler;
import com.calclab.emite.xep.muc.client.events.OccupantChangedHandler;
import com.calclab.emite.xep.muc.client.events.RoomInvitationSentHandler;
import com.calclab.emite.xep.muc.client.subject.RoomSubject;
import com.calclab.suco.client.events.Listener;
import com.calclab.suco.client.events.Listener2;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * A Room is a MUC chat. It extends the chat with MUC related methods
 */
public interface Room extends Chat {

    /**
     * Adds a handler to know when a invitation to this room is going to be
     * sent. This handler allows to decorate the Message object before is sent
     * 
     * @param handler
     * @return
     */
    public HandlerRegistration addBeforeRoomInvitationSendHandler(BeforeRoomInvitationSendHandler handler);

    /**
     * Adds a handler to know when a OccupantChangedEvent ocurs. This is used
     * know when a user enter or exites the room, for example.
     * 
     * @param handler
     * @return
     */
    public HandlerRegistration addOccupantChangedHandler(OccupantChangedHandler handler);

    /**
     * Adds a handler to know when a presence arrives to this room
     * 
     * @param handler
     * @return
     */
    public HandlerRegistration addPresenceReceivedHandler(PresenceHandler handler);

    /**
     * Add a handler to know when a room invitation has been sent
     * 
     * @param handler
     * @return
     */
    public HandlerRegistration addRoomInvitationSentHandler(RoomInvitationSentHandler handler);

    /**
     * Find an occupant with the given occupant uri
     * 
     * @param occupantUri
     *            occupant uri is the room jid with nick name as resource
     * @return the occupant if found, null if not
     */
    public abstract Occupant getOccupantByOccupantUri(XmppURI occupantUri);

    /**
     * Use getOccupantByOccupantUri
     */
    @Deprecated
    public abstract Occupant getOccupantByURI(XmppURI uri);

    /**
     * Find an occupant with the given user jid
     * 
     * @param userUri
     *            the user's uri (resource is ignored)
     * @return the occupant if found, null if not
     */
    public abstract Occupant getOccupantByUserUri(XmppURI userUri);

    /**
     * Return the occupants of this room. THIS IS THE ACTUAL BACKEND
     * IMPLEMENTATION. DO NOT MODIFY
     * 
     * @return
     */
    public Collection<Occupant> getOccupants();

    /**
     * Return the current number of occupants of this room
     * 
     * @return
     */
    public abstract int getOccupantsCount();

    /**
     * To check if is an echo message
     * 
     * @param message
     * @return true if this message is a room echo
     */
    public abstract boolean isComingFromMe(final Message message);

    public abstract boolean isUserMessage(Message message);

    /**
     * Use addBeforeInvitationSendHandler
     * 
     * @param listener
     */
    @Deprecated
    public void onInvitationSent(Listener2<XmppURI, String> listener);

    /**
     * Use addOccupantChangedHandler
     * 
     * @param listener
     */
    @Deprecated
    public abstract void onOccupantAdded(Listener<Occupant> listener);

    /**
     * Use addOccupantChangedHandler
     * 
     * @param listener
     */
    @Deprecated
    public abstract void onOccupantModified(Listener<Occupant> listener);

    /**
     * Use addOccupantChangedHandler
     * 
     * @param listener
     */
    @Deprecated
    public abstract void onOccupantRemoved(Listener<Occupant> occupantRemoved);

    /**
     * Use RoomSubject.addRoomSubjectChangedHandler
     * 
     * @param subjectListener
     */
    @Deprecated
    public abstract void onSubjectChanged(Listener2<Occupant, String> subjectListener);

    public void reEnter(final HistoryOptions historyOptions);

    /**
     * 
     * http://www.xmpp.org/extensions/xep-0045.html#invite
     * 
     * @param userJid
     *            user to invite
     * @param reasonText
     *            reason for the invitation
     */
    public void sendInvitationTo(final XmppURI userJid, final String reasonText);

    // public Occupant setOccupantPresence(final XmppURI uri, final String
    // affiliation, final String role,
    // final Show show, final String statusMessage);

    /**
     * Update my status to other occupants.
     * 
     * @param statusMessage
     * @param show
     */
    public void setStatus(final String statusMessage, final Show show);

    /**
     * Use RoomSubject.requestSubjectChange
     * 
     * @param newSubject
     * @see RoomSubject
     */
    @Deprecated
    public void setSubject(String newSubject);

}