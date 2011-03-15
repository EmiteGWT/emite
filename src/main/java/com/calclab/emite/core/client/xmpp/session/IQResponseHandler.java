package com.calclab.emite.core.client.xmpp.session;

import com.calclab.emite.core.client.xmpp.stanzas.IQ;

public interface IQResponseHandler {
    void onIQ(IQ iq);

}
