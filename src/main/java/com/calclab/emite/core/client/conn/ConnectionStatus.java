package com.calclab.emite.core.client.conn;

public enum ConnectionStatus {
	/**
	 * The connection is now connected
	 */
	connected,
	/**
	 * The connection is now disconnected
	 */
	disconnected,
	/**
	 * The connection received an error
	 */
	error,
	/**
	 * The connection will try to re-connect in the given milliseconds
	 */
	waitingForRetry;
}