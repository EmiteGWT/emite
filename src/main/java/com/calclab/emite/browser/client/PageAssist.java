/*
 * ((e)) emite: A pure Google Web Toolkit XMPP library
 * Copyright (c) 2008-2011 The Emite development team
 * 
 * This file is part of Emite.
 *
 * Emite is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * Emite is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with Emite.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.calclab.emite.browser.client;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;

import java.util.logging.Logger;

import com.calclab.emite.core.client.bosh.StreamSettings;
import com.calclab.emite.core.client.conn.ConnectionSettings;
import com.calclab.emite.core.client.conn.XmppConnection;
import com.calclab.emite.core.client.xmpp.session.Credentials;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.DOM;

/**
 * An utility class to perform actions based on meta tags
 */
public class PageAssist {

	private static final Logger logger = Logger.getLogger(PageAssist.class.getName());

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
	 * Meta key to store the password encoding method. Out of the box "none" and
	 * "plain" are provided
	 */
	private static final String PARAM_PASSWORD_ENCODING = "emite.password.encoding";

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

	/**
	 * Meta key to store the "wait" BOSH parameter
	 */
	static final String PARAM_BOSH_WAIT = "emite.bosh.wait";

	/**
	 * Meta key to store the "hold" BOSH parameter
	 */
	static final String PARAM_BOSH_HOLD = "emite.bosh.hold";

	private static final String PAUSE_COOKIE = "emite.cookies.pause";

	public static void closeSession(final XmppSession session) {
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
	public static final boolean configureFromMeta(final XmppConnection connection) {
		logger.info("Configuring connection...");
		final String httpBase = getMeta(PARAM_HTTPBASE);
		final String host = getMeta(PARAM_HOST);
		final String routeHost = getMeta(PARAM_ROUTE_HOST);
		final Integer routePort = getMetaInteger(PARAM_ROUTE_PORT);
		final boolean secure = isMetaTrue(PARAM_SECURE);
		final Integer wait = getMetaInteger(PARAM_BOSH_WAIT);
		final Integer hold = getMetaInteger(PARAM_BOSH_HOLD);

		if (host != null && httpBase != null) {
			logger.info("CONNECTION PARAMS: " + httpBase + ", " + host);
			connection.setSettings(new ConnectionSettings(httpBase, host, wait, hold, routeHost, routePort, secure));
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
	 * <code>&lt;meta name="name" value="userName" /&gt;</code>
	 * 
	 * @param id
	 *            the 'id' value of the desired meta tag
	 * @return the value of the attribute 'value' or null if not found
	 */
	public static final String getMeta(final String id) {
		String value = null;
		Element element = null;
		final NodeList<Element> elements = Document.get().getElementsByTagName("meta");
		if (elements != null) {
			for (int i = 0; i < elements.getLength() && element == null; i++) {
				final Element candidate = elements.getItem(i);
				if (id.equals(candidate.getAttribute("name"))) {
					element = candidate;
				}
			}
		}
		if (element == null) {
			element = DOM.getElementById(id);
		}
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
	 * Return an int value for the meta
	 * 
	 * @param id
	 *            the 'id' value of the desired meta tag
	 * @return the int value of the meta tag, null if it is invalid or doesn't
	 *         exist
	 * @see PageAssist#getMeta(String)
	 */
	public static Integer getMetaInteger(final String id) {
		final String metaValue = getMeta(id);

		if (metaValue != null) {
			try {
				return new Integer(metaValue);
			} catch (final NumberFormatException eNF) {
				logger.warning("Invalid meta value for " + id + " : " + metaValue);
			}
		}

		return null;
	}

	/**
	 * Returns the true/false value of a meta tag.
	 * 
	 * @param id
	 *            the 'id' value of the desired meta tag
	 * @param defaultValue
	 *            the value to return if the meta value is neither
	 *            <code>"true"</code> nor <code>"false"</code>
	 * @return true if the given meta is "true", false if the given meta is
	 *         "false" otherwise the defaultValue.
	 * @see PageAssist#getMeta(String)
	 */
	public static final boolean isMetaTrue(final String id, final boolean defaultValue) {
		final String metaValue = getMeta(id);
		if ("true".equals(metaValue))
			return true;
		else if ("false".equals(metaValue))
			return false;

		return defaultValue;
	}

	/**
	 * Will try to login session if PARAM_JID and PARAM_PASSWORD are present. <br/>
	 * PARAM_PASSWORD is optional if PARAM_JID value is set to 'anonymous'
	 * 
	 * @param session
	 *            the session to be logged in
	 * @return true if meta parameters value are presents, false otherwise
	 */
	public static final boolean loginFromMeta(final XmppSession session) {
		logger.info("Loging in from meta data...");
		final String userJID = getMeta(PARAM_JID);
		final String password = getMeta(PARAM_PASSWORD);
		String encodingMethod = getMeta(PARAM_PASSWORD_ENCODING);
		encodingMethod = encodingMethod != null ? encodingMethod : Credentials.ENCODING_NONE;
		if (password != null && userJID != null) {
			final XmppURI jid = uri(userJID);
			session.login(new Credentials(jid, password, encodingMethod));
			return true;
		} else if (userJID != null && "anonymous".equals(userJID.toLowerCase())) {
			session.login(Credentials.createAnonymous());
			return true;
		} else
			return false;
	}

	/**
	 * Pause a session and serializes the stream settings in a cookie
	 * 
	 * @param session
	 *            the session to be paused
	 * @return true if the session is paused (if the session was ready), false
	 *         otherwise
	 */
	public static final boolean pauseSession(final XmppSession session) {
		logger.info("Pausing connection...");
		final StreamSettings stream = session.pause();
		if (stream == null)
			return false;

		final String user = session.getCurrentUserURI().toString();
		final SerializableMap map = new SerializableMap();
		map.put("rid", "" + stream.rid);
		map.put("sid", stream.sid);
		map.put("wait", stream.wait);
		map.put("inactivity", stream.getInactivityString());
		map.put("maxPause", stream.getMaxPauseString());
		map.put("user", user);

		final String serialized = map.serialize();
		Cookies.setCookie(PAUSE_COOKIE, serialized);
		logger.finer("Pausing session: " + serialized);
		return true;
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
	public static final boolean resumeSession(final XmppSession session) {
		final String pause = Cookies.getCookie(PAUSE_COOKIE);
		if (pause == null)
			return false;

		logger.finer("Resume session: " + pause);
		Cookies.removeCookie(PAUSE_COOKIE);
		final SerializableMap map = SerializableMap.restore(pause);
		final StreamSettings stream = new StreamSettings();
		stream.rid = Integer.parseInt(map.get("rid"));
		stream.sid = map.get("sid");
		stream.wait = map.get("wait");
		stream.setInactivity(map.get("inactivity"));
		stream.setMaxPause(map.get("maxPause"));
		final XmppURI user = uri(map.get("user"));
		session.resume(user, stream);
		return true;
	}

}
