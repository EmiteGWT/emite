package com.calclab.emite.im.client.chat;

/**
 * Common chat states. Chat states are not limited to this ones.
 */
public class ChatStates {
    /**
     * the chat is ready to be used
     */
    public static final String ready = "ready";
    /**
     * the chat is opened but can't be used (maybe waiting for a server
     * confirmation or because the session is closed or the connection is lost)
     */
    public static final String locked = "locked";

    public static final boolean isLocked(String state) {
        return locked.equals(state);
    }

    public static final boolean isReady(String state) {
        return ready.equals(state);
    }
}