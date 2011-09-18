package com.calclab.emite.core.client.services;

import com.calclab.emite.core.client.packet.IPacket;

public interface Services {

	void schedule(int msecs, ScheduledAction action);
	
	void send(String httpBase, String request, ConnectorCallback listener) throws ConnectorException;

	/**
	 * Convert xml to IPacket
	 * 
	 * @param xml
	 *            text
	 * @return IPacket or NoPacket.INSTANCE if problems
	 */
	IPacket toXML(String xml);
	
}
