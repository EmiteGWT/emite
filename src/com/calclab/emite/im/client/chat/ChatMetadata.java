package com.calclab.emite.im.client.chat;

import java.util.HashMap;

/**
 * A simple store of data associated to a given chat
 * 
 */
public class ChatMetadata {
    private final HashMap<String, Object> data;

    public ChatMetadata() {
	this.data = new HashMap<String, Object>();
    }

    /**
     * Get the metadata object associated to a given key
     * 
     * @param key
     *            the key
     * @return the associated object if any, null otherwise
     * @see setData
     */
    public Object getData(String key) {
	return data.get(key);
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
    public Object setData(String key, Object value) {
	return data.put(key, value);
    }

}
