package com.calclab.emite.xep.storage.client;

import com.calclab.emite.core.client.packet.DelegatedPacket;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;

public class IQResponse extends DelegatedPacket {

    public IQResponse(final IPacket result) {
	super(result);
    }

    public IQ.Type getType() {
	return IQ.getType(this);
    }

    public boolean isError() {
	return IQ.isType(IQ.Type.error, this);
    }

    public boolean isSuccess() {
	return IQ.isSuccess(this);
    }
}
