package com.calclab.emite.xep.storage.client;

import com.calclab.emite.core.client.packet.DelegatedPacket;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.Packet;

public class SimpleStorageData extends DelegatedPacket {

    public static SimpleStorageData parse(final IQResponse response) {
	return new SimpleStorageData(response.getFirstChild("query"));

    }

    public SimpleStorageData(final IPacket delegate) {
	super(delegate);
    }

    public SimpleStorageData(final String name, final String xmlns) {
	super(new Packet(name, xmlns));
    }
}
