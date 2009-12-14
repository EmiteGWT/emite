/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008-2009 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emite.browser.client;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;

import com.calclab.emite.core.client.bosh.BoshSettings;
import com.calclab.emite.core.client.bosh.Connection;
import com.calclab.emite.core.client.bosh.StreamSettings;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;

/**
 * This class object auto-configures some emite components and behaviours based
 * on some html parameters. All the parameters are specified in meta tags. For
 * example:
 * 
 * <pre>
 * &lt;meta id=&quot;emite.httpBase&quot; content=&quot;proxy&quot; /&gt;
 * &lt;meta id=&quot;emite.host&quot; content=&quot;localhost&quot; /&gt;
 * </pre>
 * 
 * @see BrowserModule
 * 
 */
public class AutoConfig {

    private static final String PARAM_HOST = "emite.host";
    private static final String PARAM_HTTPBASE = "emite.httpBase";
    private static final String PARAM_PASSWORD = "emite.password";
    private static final String PARAM_JID = "emite.user";
    private static final String PARAM_CLOSE = "emite.onBeforeUnload";

    private static final String PAUSE_COOKIE = "emite.pause";

    private final DomAssist assist;
    private final Connection connection;
    private final Session session;

    public AutoConfig(final Connection connection, final Session session, final DomAssist assist) {
	this.connection = connection;
	this.session = session;
	this.assist = assist;
	initialize();
    }

    /**
     * Try to login with the meta parameters of the html page
     * 
     * @return true if the logging process started (some user JID founded)
     */
    public boolean login() {
	final String userJID = assist.getMeta(PARAM_JID, false);
	final String password = assist.getMeta(PARAM_PASSWORD, false);
	if (userJID != null) {
	    GWT.log("Loging in...", null);
	    if ("anonymous".equals(userJID.toLowerCase())) {
		session.login(Session.ANONYMOUS, null);
	    } else {
		final XmppURI jid = uri(userJID);
		session.login(jid, password);
	    }
	}
	return userJID != null;
    }

    private void configureConnection() {
	GWT.log("PageController - configuring connection...", null);
	final String httpBase = assist.getMeta(PARAM_HTTPBASE, true);
	final String host = assist.getMeta(PARAM_HOST, true);
	GWT.log(("CONNECTION PARAMS: " + httpBase + ", " + host), null);
	connection.setSettings(new BoshSettings(httpBase, host));
    }

    private void initialize() {
	GWT.log("PageController - initializing...", null);
	final String onCloseAction = assist.getMeta(PARAM_CLOSE, "pause");
	prepareOnCloseAction(onCloseAction);
	configureConnection();
	prepareOnOpenAction();
	GWT.log("PageController - done.", null);
    }

    private void pauseConnection() {
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
	}
    }

    private void prepareOnCloseAction(final String onCloseAction) {
	GWT.log("PageController - configuring close action...", null);
	final boolean shouldPause = "pause".equals(onCloseAction);
	Window.addCloseHandler(new CloseHandler<Window>() {
	    public void onClose(final CloseEvent<Window> arg0) {
		if (shouldPause) {
		    pauseConnection();
		} else {
		    Cookies.removeCookie(PAUSE_COOKIE);
		    session.logout();
		    GWT.log("Logged out!", null);
		}
	    }
	});
    }

    private void prepareOnOpenAction() {
	GWT.log("PageController - trying to resume...", null);
	if (!tryToResume()) {
	    GWT.log("PageController - no session found. Trying to login", null);
	    if (!login()) {
		GWT.log("PageController - No action perfomer on open.", null);
	    }
	}
    }

    /**
     * Try to resume a session stored in the cookies
     * 
     * @return true if the session is resumed
     */
    private boolean tryToResume() {
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
	}
	return pause != null;
    }
}
