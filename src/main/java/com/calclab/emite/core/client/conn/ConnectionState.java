package com.calclab.emite.core.client.conn;

public enum ConnectionState {
	/**
	 * The connection is now connected
	 */
	connected,
	/**
	 * The connection is now desconnected
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