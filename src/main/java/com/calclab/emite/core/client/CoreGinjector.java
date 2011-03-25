package com.calclab.emite.core.client;

import com.calclab.emite.core.client.conn.XmppConnection;
import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.services.Services;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(CoreModule.class)
public interface CoreGinjector extends Ginjector {

    EmiteEventBus getEmiteEventBus();

    Services getServices();

    XmppConnection getXmppConnection();

    XmppSession getXmppSession();
}
