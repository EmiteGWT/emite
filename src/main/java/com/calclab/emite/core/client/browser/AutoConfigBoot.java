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

package com.calclab.emite.core.client.browser;

import static com.calclab.emite.core.client.uri.XmppURI.uri;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashMap;
import java.util.logging.Logger;

import com.calclab.emite.core.client.conn.ConnectionSettings;
import com.calclab.emite.core.client.conn.XmppConnection;
import com.calclab.emite.core.client.conn.bosh.StreamSettings;
import com.calclab.emite.core.client.session.Credentials;
import com.calclab.emite.core.client.session.XmppSession;
import com.calclab.emite.core.client.uri.XmppURI;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * In order to remove the (bosh) configuration from the source code and to
 * reduce the boiler plate code, some of the parameters and behaviours of the
 * Emite library can be specified using <meta> tags inside <html> files.
 * 
 * <h3>1. Install</h3>
 * 
 * Simply add this line:
 * 
 * <pre>
 * &lt;inherits name=&quot;com.calclab.emite.browser.EmiteBrowser&quot; /&gt;
 * </pre>
 * 
 * To your module's .gwt.xml file
 * 
 * <h3>2. Usage<h3>
 * <h4>2.1 Bosh configuration</h4>
 * You can configure your bosh configuration settings adding this lines inside
 * the head tag of your html file:
 * 
 * <pre>
 * &lt;meta name=&quot;emite.httpBase&quot; content=&quot;proxy&quot; /&gt;
 * &lt;meta name=&quot;emite.host&quot; content=&quot;localhost&quot; /&gt;
 * </pre>
 * 
 * This is completly equivalent to write this configuration code:
 * 
 * <pre>
 * Suco.get(Connection.class).setSettings(new BoshSettings(&quot;proxy&quot;, &quot;localhost&quot;));
 * </pre>
 * 
 * So, if you install this module and add those meta tags inside the html file
 * you don't longer need to writa that configuration code anymore.
 * 
 * <h4>2.2 Login and logout</h4>
 * 
 * To make BrowserModule to login and logout when enter and exits the page
 * respectively this meta tag should be placed in the html file:
 * 
 * <pre>
 * &lt;meta name=&quot;emite.session&quot; content=&quot;login&quot; /&gt;
 * </pre>
 * 
 * In order this feature to work you have to specify the user's jid and password
 * (plain text at this moment, more to come):
 * 
 * <pre>
 * &lt;meta name=&quot;emite.user&quot; content=&quot;test2@localhost&quot; /&gt;
 * &lt;meta name=&quot;emite.password&quot; content=&quot;test2&quot; /&gt;
 * </pre>
 * 
 * Automatic logout only (when page closed) is possible using the the "logout"
 * content value in this meta tag. If this option is choosen, there's no need to
 * specify user or password.
 * 
 * <h4>2.3 Pause and resume</h4>
 * 
 * To make BrowserModule pause and resume the session when enter and exit the
 * page respectively, this meta tag should be use: *
 * 
 * <pre>
 * &lt;meta name=&quot;emite.session&quot; content=&quot;resume&quot; /&gt;
 * </pre>
 * 
 * BrowserModule will pause the session and serialize it on a browser's cookie.
 * It will try to resume the session if the cookie is present.
 * 
 * <h4>2.4 Resume or login</h4>
 * 
 * To make BrowserModule to pause the session when the user leaves the page and
 * to resume the session or login if resume fails, this meta tag should be
 * placed in your html file:
 * 
 * <pre>
 * &lt;meta name=&quot;emite.session&quot; content=&quot;resumeOrLogin&quot; /&gt;
 * </pre>
 * 
 * 
 * <b>WARNING: this is work in progress and currently COMPLETLY UNSECURE. We
 * will implement the ability to encode the passwords... but is NOT currently
 * implemented. But...</b>
 * 
 * You can force an ANONYMOUS login (much more secure, but usually not enough)
 * with the following line:
 * 
 * <pre>
 * &lt;meta name=&quot;emite.user&quot; content=&quot;anonymous&quot; /&gt;
 * </pre>
 * 
 * Remember that <b>emite</b> won't autologin if a session was previously
 * paused.
 */
@Singleton
public class AutoConfigBoot implements Scheduler.ScheduledCommand, Window.ClosingHandler {

	private static final Logger logger = Logger.getLogger(AutoConfigBoot.class.getName());

	private static final String PARAM_AUTOCONFIG = "emite.autoConfig";

	/**
	 * Meta key to store the session behaviour
	 */
	private static final String PARAM_SESSION = "emite.session";

	/**
	 * Meta key to store the httpBase parameter in bosh configuration
	 */
	private static final String PARAM_HTTPBASE = "emite.httpBase";

	/**
	 * Meta key to store the host param in bosh configuration
	 */
	private static final String PARAM_HOST = "emite.host";

	/**
	 * Meta key to store tue user JID in session login
	 */
	private static final String PARAM_JID = "emite.user";

	/**
	 * Meta key to store the user password in session login
	 */
	private static final String PARAM_PASSWORD = "emite.password";

	/**
	 * Meta key to store the route host param in bosh configuration
	 */
	private static final String PARAM_ROUTE_HOST = "emite.routeHost";

	/**
	 * Meta key to store the route host port number param in bosh configuration
	 */
	private static final String PARAM_ROUTE_PORT = "emite.routePort";

	/**
	 * Meta key to store the secure param in bosh configuration
	 */
	private static final String PARAM_SECURE = "emite.secure";

	/**
	 * Meta key to store the "wait" BOSH parameter
	 */
	private static final String PARAM_BOSH_WAIT = "emite.bosh.wait";

	/**
	 * Meta key to store the "hold" BOSH parameter
	 */
	private static final String PARAM_BOSH_HOLD = "emite.bosh.hold";

	private static final String PAUSE_COOKIE = "emite.cookies.pause";

	private static final String LOGIN = "login";
	private static final String RESUME = "resume";
	private static final String RESUME_OR_LOGIN = "resumeOrLogin";
	private static final String LOGOUT = "logout";

	private final XmppConnection connection;
	private final XmppSession session;
	private String sessionBehaviour;

	@Inject
	protected AutoConfigBoot(final XmppConnection connection, final XmppSession session) {
		this.connection = checkNotNull(connection);
		this.session = checkNotNull(session);

		Scheduler.get().scheduleDeferred(this);
		Window.addWindowClosingHandler(this);
	}

	@Override
	public final void execute() {
		logger.fine("Checking auto config");
		if (!PageAssist.getMeta(PARAM_AUTOCONFIG, true))
			return;

		sessionBehaviour = PageAssist.getMeta(PARAM_SESSION, RESUME_OR_LOGIN);
		configureFromMeta();

		if (LOGIN.equals(sessionBehaviour)) {
			loginFromMeta();
		} else if (RESUME.equals(sessionBehaviour)) {
			resumeSession();
		} else if (RESUME_OR_LOGIN.equals(sessionBehaviour)) {
			if (!resumeSession()) {
				loginFromMeta();
			}
		}
	}

	@Override
	public final void onWindowClosing(final Window.ClosingEvent event) {
		if (RESUME.equals(sessionBehaviour) || RESUME_OR_LOGIN.equals(sessionBehaviour)) {
			logger.info("PAUSING SESSION...");
			pauseSession();
		} else if (LOGIN.equals(sessionBehaviour)) {
			logger.info("LOGGIN OUT SESSION...");
			closeSession();
		} else if (LOGOUT.equals(sessionBehaviour)) {
			logger.info("LOGGIN OUT SESSION...");
			closeSession();
		}
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
	private final boolean configureFromMeta() {
		logger.info("Configuring connection...");
		final String httpBase = PageAssist.getMeta(PARAM_HTTPBASE, "/http-bind");
		final String hostName = PageAssist.getMeta(PARAM_HOST, null);
		final String routeHost = PageAssist.getMeta(PARAM_ROUTE_HOST, hostName);
		final int routePort = PageAssist.getMeta(PARAM_ROUTE_PORT, 5222);
		final boolean secure = PageAssist.getMeta(PARAM_SECURE, true);
		final int wait = PageAssist.getMeta(PARAM_BOSH_WAIT, ConnectionSettings.DEFAULT_WAIT);
		final int hold = PageAssist.getMeta(PARAM_BOSH_HOLD, ConnectionSettings.DEFAULT_HOLD);

		if (hostName == null)
			return false;

		logger.info("CONNECTION PARAMS: " + httpBase + ", " + hostName);
		connection.setSettings(new ConnectionSettings(httpBase, hostName, routeHost, routePort, secure, wait, hold));
		return true;
	}

	/**
	 * Will try to login session if PARAM_JID and PARAM_PASSWORD are present. <br/>
	 * PARAM_PASSWORD is optional if PARAM_JID value is set to 'anonymous'
	 * 
	 * @param session
	 *            the session to be logged in
	 * @return true if meta parameters value are presents, false otherwise
	 */
	private final boolean loginFromMeta() {
		logger.info("Loging in from meta data...");
		final String userJID = PageAssist.getMeta(PARAM_JID, null);
		final String password = PageAssist.getMeta(PARAM_PASSWORD, null);
		if (userJID != null && password != null) {
			final XmppURI jid = uri(userJID);
			session.login(new Credentials(jid, password));
			return true;
		} else if (userJID != null && "anonymous".equals(userJID.toLowerCase())) {
			session.login(Credentials.createAnonymous());
			return true;
		}
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
	private final boolean pauseSession() {
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
	private final boolean resumeSession() {
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

	private final void closeSession() {
		Cookies.removeCookie(PAUSE_COOKIE);
		session.logout();
	}

	/**
	 * An extension to provide serialize/restore from HashMap to String in order
	 * to store information in the browser's cookies.
	 */
	protected static class SerializableMap extends HashMap<String, String> {
		private static final long serialVersionUID = 1L;

		public static SerializableMap restore(final String serialized) {
			final SerializableMap map = new SerializableMap();
			final int total = serialized.length() - 1;
			String key, value;
			int begin, end, next;
			next = -1;

			do {
				begin = next + 1;
				end = serialized.indexOf('#', begin + 1);
				next = serialized.indexOf('#', end + 1);
				key = serialized.substring(begin, end);
				value = serialized.substring(end + 1, next);
				map.put(key, value);
			} while (next < total);

			return map;
		}

		public String serialize() {
			final StringBuilder builder = new StringBuilder();
			for (final String key : keySet()) {
				builder.append(key).append("#").append(get(key)).append("#");
			}
			return builder.toString();
		}

	}
}
