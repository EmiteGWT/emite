package com.calclab.emite.core.client;

import com.calclab.emite.core.client.conn.Connection;
import com.calclab.emite.core.client.conn.XmppConnection;
import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.services.Services;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(CoreModule.class)
public interface CoreGinjector extends Ginjector {
    /**
     * Use the new XmppConnection interface instead
     */
    @Deprecated
    Connection getConnection();

    EmiteEventBus getEmiteEventBus();

    Services getServices();

    /**
     * Use the XmppSession interface instead
     * 
     * @return
     */
    @Deprecated
    Session getSession();

    XmppConnection getXmppConnection();

    XmppSession getXmppSession();
}
