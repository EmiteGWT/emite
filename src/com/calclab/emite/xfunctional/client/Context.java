package com.calclab.emite.xfunctional.client;

import com.calclab.emite.core.client.xmpp.session.Session;

public interface Context {

    void assertEquals(String description, Object expected, Object actual);

    Session getSession();

    void success(String string);
}
