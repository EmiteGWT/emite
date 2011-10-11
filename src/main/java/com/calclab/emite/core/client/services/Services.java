package com.calclab.emite.core.client.services;

public interface Services {

	void schedule(int msecs, ScheduledAction action);
	
	void send(String httpBase, String request, ConnectorCallback listener) throws ConnectorException;

}
