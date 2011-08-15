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

import java.util.logging.Logger;

import com.calclab.emite.core.client.conn.XmppConnection;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;

/**
 * This class object auto-configures some emite components and behaviours based
 * on some html parameters. All the parameters are specified in meta tags. *
 * 
 * <pre>
 * &lt;meta name=&quot;emite.httpBase&quot; content=&quot;proxy&quot; /&gt;
 * &lt;meta name=&quot;emite.host&quot; content=&quot;localhost&quot; /&gt;
 * </pre>
 * 
 * More detailed information in BrowserModule's javadoc.
 * 
 * @see BrowserModule
 * 
 */
public class AutoConfig {
	
	private static final Logger logger = Logger.getLogger(AutoConfig.class.getName());

	private static final String LOGIN = "login";
	private static final String RESUME = "resume";
	private static final String RESUME_OR_LOGIN = "resumeOrLogin";
	private static final String PARAM_SESSION = "emite.session";
	protected static final String LOGOUT = "logout";

	private final XmppConnection connection;
	private final XmppSession session;

	@Inject
	public AutoConfig(final XmppConnection connection, final XmppSession session) {
		this.connection = connection;
		this.session = session;
		initialize();
	}

	private void initialize() {
		PageAssist.configureFromMeta(connection);
		final String sessionBehaviour = PageAssist.getMeta(PARAM_SESSION);
		if (sessionBehaviour != null) {
			logger.finer("PageController - initializing...");
			prepareOnCloseAction(sessionBehaviour);
			prepareOnOpenAction(sessionBehaviour);
			logger.finer("PageController - done.");
		}
	}

	private void prepareOnCloseAction(final String sessionBehaviour) {
		logger.finer("PageController - configuring close action...");
		Window.addCloseHandler(new CloseHandler<Window>() {
			@Override
			public void onClose(final CloseEvent<Window> arg0) {
				if (RESUME.equals(sessionBehaviour) || RESUME_OR_LOGIN.equals(sessionBehaviour)) {
					logger.info("PAUSING SESSION...");
					PageAssist.pauseSession(session);
				} else if (LOGIN.equals(sessionBehaviour)) {
					logger.info("LOGGIN OUT SESSION...");
					PageAssist.closeSession(session);
				} else if (LOGOUT.equals(sessionBehaviour)) {
					logger.info("LOGGIN OUT SESSION...");
					PageAssist.closeSession(session);
				}
			}
		});
	}

	private void prepareOnOpenAction(final String sessionBehaviour) {
		logger.info("SESSION BEHAVIOUR: " + sessionBehaviour);
		if (sessionBehaviour.equals(LOGIN)) {
			PageAssist.loginFromMeta(session);
		} else if (sessionBehaviour.equals(RESUME)) {
			PageAssist.resumeSession(session);
		} else if (sessionBehaviour.equals(RESUME_OR_LOGIN)) {
			if (!PageAssist.resumeSession(session)) {
				PageAssist.loginFromMeta(session);
			}
		}
	}

}
