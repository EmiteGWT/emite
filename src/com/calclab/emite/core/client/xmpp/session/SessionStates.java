package com.calclab.emite.core.client.xmpp.session;

/**
 * Different (and common) session states. The different states paths are:
 * <ul>
 * <li>Successfull login: (disconnected) - connecting - authorized - loggedIn -
 * ready</li>
 * <li>Unsuccessfull login: (disconnected) - connecting - notAuthorized -
 * disconected</li>
 * <li>Loging out: (ready) - loggingOut - disconnected</li>
 * </ul>
 * 
 * Session can have other states different from this ones.
 */
public class SessionStates {
    /**
     * The authorization was successfull. You can NOT send stanzas using the
     * session (stanzas will be queued). If you need to send stanzas, use the
     * connection object directly
     */
    public static final String authorized = "authorized";

    /**
     * You are logged in. This is the first state when you can send stanzas.
     */
    public static final String loggedIn = "loggedIn";

    /**
     * Start login process. You can NOT send stanzas using session (you should
     * use the connection directly)
     */
    public static final String connecting = "connecting";

    /**
     * We are disconnected. You can NOT send stanzas.
     */
    public static final String disconnected = "disconnected";
    public static final String error = "error";
    public static final String notAuthorized = "notAuthorized";

    /**
     * The session is ready to use. All the queued stanzas are sent just before
     * this state.
     */
    public static final String ready = "ready";
    /**
     * We are logging out. Last oportunity to send stanzas (i.e: last presence).
     * session.getCurrentUser() returns the current user;
     */
    public static final String loggingOut = "loggingOut";

    /**
     * We are resuming a session. When resuming a session you only receive
     * "resuming" and "ready" (not loggedIn)
     */
    public static final String resume = "resume";

    /**
     * The session is binded
     */
    protected static final String binded = "binded";

    /**
     * The roster is ready
     */
    public static final String rosterReady = "rosterReady";

    /**
     * Helper function to determine if the given state is disconnected
     */
    public static final boolean isDisconnected(String state) {
        return disconnected.equals(state);
    }

    /**
     * Helper function to determine if the given state is ready
     */
    public static final boolean isReady(String state) {
        return ready.equals(state);
    }
}