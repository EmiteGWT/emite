package com.calclab.emite.core.client;

import com.calclab.emite.core.client.xmpp.datetime.XmppDateTime;
import com.google.gwt.core.client.EntryPoint;

public class EmiteCoreEntryPoint implements EntryPoint {

    @Override
    public void onModuleLoad() {
	XmppDateTime.useGWT21();
    }

}
