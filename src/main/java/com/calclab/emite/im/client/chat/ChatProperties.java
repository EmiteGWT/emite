package com.calclab.emite.im.client.chat;

import java.util.HashMap;
import java.util.Map.Entry;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;

/**
 * The properties of a chat. One property is required (the uri), some other are
 * fixed but optional (like the initiatorUri, or the state) and some are
 * optional and not fixed (you can add or retrieve any property by key)
 * 
 * The properties are the id used to find a chat in a pool of chats.
 */
public class ChatProperties {
    private final HashMap<String, Object> data;
    private final XmppURI uri;
    private XmppURI initiatorUri;
    private String state;
    private boolean shouldCreateNewChat;

    public ChatProperties(final XmppURI uri) {
	this(uri, null, null);
    }

    /**
     * Creates a new {@link ChatProperties} instance taking the data from the
     * given properties instance. Note that this will not copy the initiator uri
     * or state.
     * 
     * @param uri
     *            the new uri for the properties.
     * @param properties
     *            the properties object to replicate.
     */
    public ChatProperties(final XmppURI uri, final ChatProperties properties) {
	this(uri, null, null, properties);

	for (Entry<String, Object> entry : properties.data.entrySet()) {
	    this.setData(entry.getKey(), entry.getValue());
	}
    }

    public ChatProperties(final XmppURI uri, final XmppURI initiatorUri, final String state) {
	this.uri = uri;
	this.initiatorUri = initiatorUri;
	this.state = state;
	this.shouldCreateNewChat = true;
	data = new HashMap<String, Object>();
    }

    /**
     * Creates a new {@link ChatProperties} instance taking the data from the
     * given properties instance. Note that this will not copy the initiator uri
     * or state.
     * 
     * @param uri
     *            the new uri for the properties.
     * @param initiatorUri
     *            the uri of the chat initiator.
     * @param state
     *            the chat state.
     * @param properties
     *            the properties object to replicate.
     */
    public ChatProperties(final XmppURI uri, final XmppURI initiatorUri, final String state,
	    final ChatProperties properties) {
	this(uri, initiatorUri, state);

	for (Entry<String, Object> entry : properties.data.entrySet()) {
	    this.setData(entry.getKey(), entry.getValue());
	}
    }

    /**
     * Get the metadata object associated to a given key
     * 
     * @param key
     *            the key
     * @return the associated object if any, null otherwise
     * @see setData
     */
    public Object getData(final String key) {
	return data.get(key);
    }

    /**
     * This is the uri of the entity that inititated the chat
     * 
     * @return
     */
    public XmppURI getInitiatorUri() {
	return initiatorUri;
    }

    /**
     * The current state of the chat. See ChatStates to see some options.
     * 
     * @return
     * @see ChatStates
     */
    public String getState() {
	return state;
    }

    /**
     * Get the chats uri. Is the other side of the conversation in a PairChat,
     * or the room uri in a RoomChat
     * 
     * @return
     */
    public XmppURI getUri() {
	return uri;
    }

    /**
     * Set the metadata object associated to a given key
     * 
     * @param key
     *            the key
     * @param data
     *            the object you want to associate
     * @return the previously metadata object associated to that kay (if any) or
     *         null otherwise
     * @see getData
     */
    public Object setData(final String key, final Object value) {
	return data.put(key, value);
    }

    /**
     * Change the initiatorUri
     * 
     * @param initiatorUri
     */
    public void setInitiatorUri(final XmppURI initiatorUri) {
	this.initiatorUri = initiatorUri;
    }

    /**
     * If this attribute is true, when a chat is not found with this properties,
     * the ChatManager should create a new chat.
     * 
     * If this attribute is false, this chat properties never fire a new chat
     * creation
     * 
     * @param shouldCreateNewChat
     */
    public void setShouldCreateNewChat(boolean shouldCreateNewChat) {
	this.shouldCreateNewChat = shouldCreateNewChat;
    }

    /**
     * Change the state of the chat
     * 
     * @param state
     */
    public void setState(final String state) {
	this.state = state;
    }

    /**
     * ChatManager uses this value to know whenever a new chat should be created
     * when a message with this chat properties arrives
     * 
     * @return
     */
    public boolean shouldCreateNewChat() {
	return shouldCreateNewChat;
    }
}
