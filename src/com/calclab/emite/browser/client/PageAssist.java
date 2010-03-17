package com.calclab.emite.browser.client;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;

import com.calclab.emite.core.client.bosh.BoshSettings;
import com.calclab.emite.core.client.bosh.Connection;
import com.calclab.emite.core.client.bosh.StreamSettings;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.DOM;

/**
 * An utility class to perform actions based on meta tags
 */
public class PageAssist {

    /**
     * Meta key to store the host param in bosh configuration
     */
    static final String PARAM_HOST = "emite.host";

    /**
     * Meta key to store the httpBase parameter in bosh configuration
     */
    static final String PARAM_HTTPBASE = "emite.httpBase";

    /**
     * Meta key to store the user password in session login
     */
    static final String PARAM_PASSWORD = "emite.password";

    /**
     * Meta key to store tue user JID in session login
     */
    static final String PARAM_JID = "emite.user";

    /**
     * Meta key to store the route host param in bosh configuration
     */
    static final String PARAM_ROUTE_HOST = "emite.routeHost";

    /**
     * Meta key to store the route host port number param in bosh configuration
     */
    static final String PARAM_ROUTE_PORT = "emite.routePort";

    /**
     * Meta key to store the route host port number param in bosh configuration
     */
    static final String PARAM_SECURE = "emite.secure";

    private static final String PAUSE_COOKIE = "emite.cookies.pause";

    public static void closeSession(final Session session) {
	Cookies.removeCookie(PAUSE_COOKIE);
	session.logout();
    }

    /**
     * Will configure the given connection if PARAM_HTTPBASE <b>and</b>
     * PARAM_HOST is present as html meta tags in the current html page
     * 
     * @param connection
     *            The connection to be configured
     * @return true if the configuration is perfomed (PARAM_HTTPBASE and
     *         PARAM_HOST are present), false otherwise
     */
    public static final boolean configureFromMeta(final Connection connection) {
	GWT.log("Configuring connection...", null);
	final String httpBase = getMeta(PARAM_HTTPBASE);
	final String host = getMeta(PARAM_HOST);
	final String routeHost = getMeta(PARAM_ROUTE_HOST);
	final String routePortString = getMeta(PARAM_ROUTE_PORT);
	final boolean secure = isMetaTrue(PARAM_SECURE);

	Integer routePort = null;
	if (routePortString != null) {
	    try {
		routePort = Integer.decode(routePortString);
	    } catch (final NumberFormatException e) {

	    }
	}

	if (host != null && httpBase != null) {
	    GWT.log(("CONNECTION PARAMS: " + httpBase + ", " + host), null);
	    connection.setSettings(new BoshSettings(httpBase, host, routeHost, routePort, secure));
	    return true;
	} else {
	    return false;
	}
    }

    /**
     * Get the value of meta information writen in the html page. The meta
     * information is a html tag with name of meta usually placed inside the the
     * head section with two attributes: id and content. For example:
     * 
     * <code>&lt;meta id="name" value="userName" /&gt;</code>
     * 
     * @param id
     *            the 'id' value of the desired meta tag
     * @return the value of the attribute 'value' or null if not found
     */
    public static final String getMeta(final String id) {
	String value = null;
	final Element element = DOM.getElementById(id);
	if (element != null) {
	    value = element.getPropertyString("content");
	}
	return value;
    }

    public static boolean isMetaFalse(final String id) {
	return "false".equals(getMeta(id));
    }

    /**
     * Return true if the given meta is not "false". That means always true
     * except when "false"
     * 
     * @param id
     *            the 'id' value of the dessired meta tag
     * @return true if meta is not "false"
     * @see getMeta
     */
    public static final boolean isMetaTrue(final String id) {
	return !"false".equals(getMeta(id));
    }

    /**
     * Will try to login session if PARAM_JID and PARAM_PASSWORD are present. <br/>
     * PARAM_PASSWORD is optional if PARAM_JID value is set to 'anonymous'
     * 
     * @param session
     *            the session to be logged in
     * @return true if meta parameters value are presents, false otherwise
     */
    public static final boolean loginFromMeta(final Session session) {
	final String userJID = getMeta(PARAM_JID);
	final String password = getMeta(PARAM_PASSWORD);
	if (password != null && userJID != null) {
	    final XmppURI jid = uri(userJID);
	    session.login(jid, password);
	    return true;
	} else if (userJID != null && "anonymous".equals(userJID.toLowerCase())) {
	    session.login(Session.ANONYMOUS, null);
	    return true;
	} else {
	    return false;
	}
    }

    /**
     * Pause a session and serializes the stream settings in a cookie
     * 
     * @param session
     *            the session to be paused
     * @return true if the session is paused (if the session was ready), false
     *         otherwise
     */
    public static final boolean pauseSession(final Session session) {
	GWT.log("Pausing connection...", null);
	final StreamSettings stream = session.pause();
	if (stream != null) {
	    final String user = session.getCurrentUser().toString();
	    final SerializableMap map = new SerializableMap();
	    map.put("rid", "" + stream.rid);
	    map.put("sid", stream.sid);
	    map.put("wait", stream.wait);
	    map.put("inactivity", stream.inactivity);
	    map.put("maxPause", stream.maxPause);
	    map.put("user", user);
	    final String serialized = map.serialize();
	    Cookies.setCookie(PAUSE_COOKIE, serialized);
	    GWT.log(("Pausing session: " + serialized), null);
	    return true;
	} else {
	    return false;
	}
    }

    /**
     * Try to resume the given session.
     * 
     * @param session
     *            the session to be resumed
     * @return true if the cookie is present (and therefore the session is
     *         resumed), false otherwise. True doesn't mean the sessions is
     *         <b>succesfully</b> resumed.
     */
    public static final boolean resumeSession(final Session session) {
	final String pause = Cookies.getCookie(PAUSE_COOKIE);
	if (pause != null) {
	    GWT.log(("Resume session: " + pause), null);
	    Cookies.removeCookie(PAUSE_COOKIE);
	    final SerializableMap map = SerializableMap.restore(pause);
	    final StreamSettings stream = new StreamSettings();
	    stream.rid = Integer.parseInt(map.get("rid"));
	    stream.sid = map.get("sid");
	    stream.wait = map.get("wait");
	    stream.inactivity = map.get("inactivity");
	    stream.maxPause = map.get("maxPause");
	    final XmppURI user = uri(map.get("user"));
	    session.resume(user, stream);
	    return true;
	} else {
	    return false;
	}
    }

}
