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

package com.calclab.emite.browser;

import static com.calclab.emite.core.XmppURI.uri;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.Nullable;

import com.calclab.emite.core.XmppURI;
import com.calclab.emite.core.conn.ConnectionSettings;
import com.calclab.emite.core.conn.StreamSettings;
import com.calclab.emite.core.conn.XmppConnection;
import com.calclab.emite.core.sasl.Credentials;
import com.calclab.emite.core.session.XmppSession;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Loads configuration settings from the HTML page.
 */
@Singleton
public class AutoConfigBoot implements Scheduler.ScheduledCommand, Window.ClosingHandler {

	private static final Logger logger = Logger.getLogger(AutoConfigBoot.class.getName());

	private static final String PARAM_AUTOCONFIG = "emite.autoConfig";

	/**
	 * Meta key to store the session behavior.
	 */
	private static final String PARAM_SESSION = "emite.session";

	/**
	 * Meta key to store the httpBase parameter.
	 */
	private static final String PARAM_HTTPBASE = "emite.httpBase";

	/**
	 * Meta key to store the host parameter.
	 */
	private static final String PARAM_HOST = "emite.host";

	/**
	 * Meta key to store the user JID.
	 */
	private static final String PARAM_JID = "emite.user";

	/**
	 * Meta key to store the user password.
	 */
	private static final String PARAM_PASSWORD = "emite.password";

	/**
	 * Meta key to store the route host parameter.
	 */
	private static final String PARAM_ROUTE_HOST = "emite.routeHost";

	/**
	 * Meta key to store the route port number parameter.
	 */
	private static final String PARAM_ROUTE_PORT = "emite.routePort";

	/**
	 * Meta key to store the secure parameter.
	 */
	private static final String PARAM_SECURE = "emite.secure";

	/**
	 * Meta key to store the wait parameter.
	 */
	private static final String PARAM_BOSH_WAIT = "emite.bosh.wait";

	/**
	 * Meta key to store the hold parameter.
	 */
	private static final String PARAM_BOSH_HOLD = "emite.bosh.hold";

	/**
	 * Name of the cookie to store the paused session.
	 */
	private static final String PAUSE_COOKIE = "emite.cookies.pause";

	private static final String LOGIN = "login";
	private static final String RESUME = "resume";
	private static final String RESUME_OR_LOGIN = "resumeOrLogin";
	private static final String LOGOUT = "logout";
	
	private static final Joiner.MapJoiner cookieJoiner = Joiner.on(",").withKeyValueSeparator("=");
	private static final Splitter.MapSplitter cookieSplitter = Splitter.on(",").withKeyValueSeparator("=");

	private final XmppConnection connection;
	private final XmppSession session;
	@Nullable private String sessionBehaviour;

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
	 * @return true if the configuration is performed (PARAM_HTTPBASE and
	 *         PARAM_HOST are present), false otherwise
	 */
	private final boolean configureFromMeta() {
		logger.info("Configuring connection...");
		final String httpBase = PageAssist.getMeta(PARAM_HTTPBASE, "/http-bind");
		final String hostName = PageAssist.getMeta(PARAM_HOST, null);
		final String routeHost = PageAssist.getMeta(PARAM_ROUTE_HOST, hostName);
		final int routePort = PageAssist.getMeta(PARAM_ROUTE_PORT, 5222);
		final boolean secure = PageAssist.getMeta(PARAM_SECURE, true);
		final int wait = PageAssist.getMeta(PARAM_BOSH_WAIT, 60);
		final int hold = PageAssist.getMeta(PARAM_BOSH_HOLD, 1);

		if (Strings.isNullOrEmpty(httpBase) || Strings.isNullOrEmpty(hostName))
			return false;

		logger.info("CONNECTION PARAMS: " + httpBase + ", " + hostName);
		connection.setSettings(new ConnectionSettings(httpBase, hostName, routeHost, routePort, secure, wait, hold));
		return true;
	}

	/**
	 * Will try to login session if PARAM_JID and PARAM_PASSWORD are present. <br/>
	 * PARAM_PASSWORD is optional if PARAM_JID value is set to 'anonymous'
	 * 
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
			session.login(Credentials.ANONYMOUS);
			return true;
		}
		return false;
	}

	/**
	 * Pause a session and serializes the stream settings in a cookie
	 * 
	 * @return true if the session is paused (if the session was ready), false
	 *         otherwise
	 */
	private final boolean pauseSession() {
		logger.info("Pausing connection...");
		final StreamSettings stream = session.pause();
		if (stream == null)
			return false;

		final String user = session.getCurrentUserURI().toString();
		final Map<String, String> map = Maps.newHashMap();
		map.put("rid", "" + stream.rid);
		map.put("sid", stream.sid);
		map.put("wait", stream.wait);
		map.put("inactivity", stream.getInactivityString());
		map.put("maxPause", stream.getMaxPauseString());
		map.put("user", user);

		final String serialized = cookieJoiner.join(map);
		Cookies.setCookie(PAUSE_COOKIE, serialized);
		logger.finer("Pausing session: " + serialized);
		return true;
	}

	/**
	 * Try to resume the given session.
	 * 
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
		final Map<String, String> map = cookieSplitter.split(pause);
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

}
