package com.calclab.emite.xep.storage.client;

import com.calclab.emite.core.client.packet.DelegatedPacket;
import com.calclab.emite.core.client.packet.IPacket;

public class SimpleStorageData extends DelegatedPacket {

    public SimpleStorageData(final IPacket delegate) {
	super(delegate);
    }

}
