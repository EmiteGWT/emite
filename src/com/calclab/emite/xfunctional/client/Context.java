package com.calclab.emite.xfunctional.client;

import com.calclab.emite.core.client.xmpp.session.Session;

public interface Context {

    void equal(String description, Object expected, Object actual);

    Session getSession();
}
