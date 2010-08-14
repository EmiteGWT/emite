package com.calclab.emite.im.client.chat;

import java.util.HashMap;

/**
 * A simple store of data associated to a given chat
 *
 */
public class ChatMetadata {
    private HashMap<Class<?>, Object> data;

    public ChatMetadata() {
	this.data = new HashMap<Class<?>, Object>();
    }
    
    /**
     * Get the associated metadata object of class 'type'
     * 
     * @param <T>
     *            the class (key) of the associated object
     * @param type
     *            the class object itself
     * @return the associated object if any, null otherwise
     * @see setData
     */
    @SuppressWarnings("unchecked")
    public <T> T getData(final Class<T> type) {
	return (T) data.get(type);
    }
    
    /**
     * Set the metadata object associated to a class type
     * 
     * @param <T>
     *            the class of the object
     * @param type
     *            the class object itself
     * @param data
     *            the object you want to associate
     * @return the previously metadata of the same class type if any
     * @see getData
     */
    @SuppressWarnings("unchecked")
    public <T> T setData(final Class<T> type, final T value) {
	return (T) data.put(type, value);
    }
    
    
}
