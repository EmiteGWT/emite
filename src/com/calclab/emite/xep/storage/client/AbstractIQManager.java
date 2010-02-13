package com.calclab.emite.xep.storage.client;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.events.Listener;

/**
 * Class AbstractIQManager (trying to generalize this kind of IQManagers) Move
 * to a generic place?
 * 
 */
public class AbstractIQManager {

    protected final Session session;

    protected final String xmlns;

    private final String idPrefix;

    /**
     * Instantiates a new abstract iq manager.
     * 
     * @param xmlns
     *            the xmlns
     * @param idPrefix
     *            the id prefix of the IQ packets
     * @param session
     *            the session
     */
    public AbstractIQManager(final String xmlns, final String idPrefix, final Session session) {
	this.xmlns = xmlns;
	this.idPrefix = idPrefix;
	this.session = session;
    }

    /**
     * Get method (without to/from)
     * 
     * @param listener
     *            the listener
     * @param childs
     *            the childs
     */
    protected void get(final Listener<IQResponse> listener, final IPacket... childs) {
	get(null, null, listener, childs);
    }

    /**
     * Get method
     * 
     * @param from
     *            the from uri
     * @param to
     *            the to uri
     * @param listener
     *            the response listener
     * @param childs
     *            the childs to add to the get IQ packet
     */
    protected void get(final XmppURI from, final XmppURI to, final Listener<IQResponse> listener,
	    final IPacket... childs) {
	final IQ iq = new IQ(IQ.Type.get);
	setFromTo(from, to, iq);
	for (final IPacket child : childs) {
	    iq.addChild(child);
	}
	session.sendIQ(idPrefix, iq, new Listener<IPacket>() {
	    @Override
	    public void onEvent(final IPacket parameter) {
		handleResponse(parameter, listener);
	    }
	});
    }

    /**
     * Handle response.
     * 
     * @param result
     *            the result
     * @param listener
     *            the listener
     */
    protected void handleResponse(final IPacket result, final Listener<IQResponse> listener) {
	final IQResponse response = new IQResponse(result);
	if (listener != null) {
	    listener.onEvent(response);
	}
    }

    /**
     * Set method without from/to
     * 
     * @param listener
     *            the response listener
     * @param childs
     *            the childs to add to the IQ set packet
     */
    protected void set(final Listener<IQResponse> listener, final IPacket... childs) {
	set(null, null, listener, childs);
    }

    /**
     * Sets method
     * 
     * @param from
     *            the from uri
     * @param to
     *            the to uri
     * @param listener
     *            the response listener
     * @param childs
     *            the childs to add to the IQ set packet
     */
    protected void set(final XmppURI from, final XmppURI to, final Listener<IQResponse> listener,
	    final IPacket... childs) {
	final IQ iq = new IQ(IQ.Type.set);
	setFromTo(from, to, iq);
	for (final IPacket child : childs) {
	    iq.addChild(child);
	}
	session.sendIQ(idPrefix, iq, new Listener<IPacket>() {
	    @Override
	    public void onEvent(final IPacket parameter) {
		handleResponse(parameter, listener);
	    }
	});
    }

    private void setFromTo(final XmppURI from, final XmppURI to, final IQ iq) {
	if (from != null) {
	    iq.setFrom(from);
	}
	if (to != null) {
	    iq.setTo(to);
	}
    }
}
