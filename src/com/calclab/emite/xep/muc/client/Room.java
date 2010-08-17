package com.calclab.emite.xep.muc.client;

import java.util.Collection;

import com.calclab.emite.core.client.events.PresenceHandler;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.core.client.xmpp.stanzas.Presence.Show;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.xep.muc.client.events.OccupantChangedHandler;
import com.calclab.suco.client.events.Listener;
import com.calclab.suco.client.events.Listener2;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * A Room is a MUC chat. It extends the chat with MUC related methods
 */
public interface Room extends Chat {

    /**
     * Get the occupant by uri.
     * 
     * @param uri
     *            the uri (jid) of the occupant
     * @return the occupant if exist, null otherwise
     */
    public abstract Occupant getOccupantByURI(XmppURI uri);

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

    // TODO: deprecate
    public abstract void onOccupantAdded(Listener<Occupant> listener);

    // TODO: deprecate
    public abstract void onOccupantModified(Listener<Occupant> listener);

    // TODO: deprecate
    public abstract void onOccupantRemoved(Listener<Occupant> occupantRemoved);

    // TODO: deprecate
    public abstract void onSubjectChanged(Listener2<Occupant, String> subjectListener);

    public abstract void reEnter(final HistoryOptions historyOptions);

    public abstract void removeOccupant(final XmppURI uri);

    /**
     * 
     * http://www.xmpp.org/extensions/xep-0045.html#invite
     * 
     * @param userJid
     *            user to invite
     * @param reasonText
     *            reason for the invitation
     */
    public abstract void sendInvitationTo(final XmppURI userJid, final String reasonText);

    public abstract Occupant setOccupantPresence(final XmppURI uri, final String affiliation, final String role,
	    final Show show, final String statusMessage);

    /**
     * Update my status to other occupants.
     * 
     * @param statusMessage
     * @param show
     */
    public abstract void setStatus(final String statusMessage, final Show show);

    /**
     * http://www.xmpp.org/extensions/xep-0045.html#subject-mod
     * 
     * @param subjectText
     */
    public abstract void setSubject(final String subjectText);

    /**
     * Adds a handler to know when a OccupantChangedEvent ocurs. This is used
     * know when a user enter or exites the room, for example.
     * 
     * @param handler
     * @return
     */
    HandlerRegistration addOccupantChangedHandler(OccupantChangedHandler handler);

    /**
     * Adds a handler to know when a presence arrives to this room
     * 
     * @param handler
     * @return
     */
    HandlerRegistration addPresenceReceivedHandler(PresenceHandler handler);

    /**
     * Return the occupants of this room. THIS IS THE ACTUAL BACKEND
     * IMPLEMENTATION. DO NOT MODIFY
     * 
     * @return
     */
    Collection<Occupant> getOccupants();

    // TODO: deprecate
    void onInvitationSent(Listener2<XmppURI, String> listener);

}