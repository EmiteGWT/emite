/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008-2009 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emite.core.client.xmpp.stanzas;

import com.calclab.emite.core.client.packet.IPacket;

public class Message extends BasicStanza {
    public static enum Type {
	chat, error, groupchat, headlines, normal
    }

    /**
     * Create a message by delegation. Not for normal use
     * 
     * @param iPacket
     */
    public Message(final IPacket iPacket) {
	super(iPacket);
    }

    /**
     * Create a message with a body
     * 
     * @param body
     *            the body of the message
     */
    public Message(final String body) {
	this();
	setBody(body);
    }

    /**
     * Easy to use constructor to create a chat message with body and recipient
     * 
     * @param body
     *            the message body
     * @param to
     *            the message recipient
     */
    public Message(final String body, final XmppURI toURI) {
	this(null, toURI, body, Type.chat);
    }

    /**
     * Create a chat (type) message
     * 
     * @param fromUri
     * @param toURI
     * @param body
     */
    public Message(final XmppURI fromUri, final XmppURI toURI, final String body) {
	this(fromUri, toURI, body, Type.chat);
    }

    /**
     * Create a message
     * 
     * @param fromUri
     * @param toURI
     * @param message
     */
    public Message(final XmppURI fromUri, final XmppURI toURI, final String message, final Type type) {
	super("message", null);
	setType(type);
	setFrom(fromUri);
	setTo(toURI);
	setBody(message);
    }

    Message() {
	this(null, null, null);
    }

    public Message Body(final String body) {
	setBody(body);
	return this;
    }

    public String getBody() {
	return getFirstChild("body").getText();
    }

    public String getSubject() {
	return getFirstChild("subject").getText();
    }

    public String getThread() {
	return getFirstChild("thread").getText();
    }

    /**
     * An IM application SHOULD support all of the foregoing message types; if
     * an application receives a message with no 'type' attribute or the
     * application does not understand the value of the 'type' attribute
     * provided, it MUST consider the message to be of type "normal" (i.e.,
     * "normal" is the default). The "error" type MUST be generated only in
     * response to an error related to a message received from another entity.
     * 
     * @see http://www.xmpp.org/rfcs/rfc3921.html#stanzas-message-type
     * @return
     */
    public Type getType() {
	final String type = getAttribute(TYPE);
	try {
	    return type != null ? Type.valueOf(type) : Type.normal;
	} catch (final IllegalArgumentException e) {
	    return Type.normal;
	}
    }

    /**
     * Return true if Message has a body
     * 
     * @return
     */
    public boolean hasBody() {
	return hasChild("body");
    }

    public void setBody(final String msg) {
	if (msg != null) {
	    final IPacket body = addChild("body", null);
	    body.setText(msg);
	}
    }

    public void setThread(final String thread) {
	super.setTextToChild("thread", thread);
    }

    public void setType(final Type type) {
	setType(type.toString());
    }

    public Message Subject(final String subject) {
	setTextToChild("subject", subject);
	return this;
    }

    public Message Thread(final String thread) {
	setThread(thread);
	return this;
    }

    public Message Type(final Type type) {
	setType(type);
	return this;
    }
}
