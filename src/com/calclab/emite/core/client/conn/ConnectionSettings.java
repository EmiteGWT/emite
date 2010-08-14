package com.calclab.emite.core.client.conn;

public class ConnectionSettings {
    public final String hostName;
    public final String httpBase;
    public final String version;
    public final int maxRequests;
    public final int hold;
    public final int wait;
    public final String routeHost;
    public final Integer routePort;
    public final boolean secure;

    public ConnectionSettings(final String httpBase, final String hostName) {
	this(httpBase, hostName, "1.6", 60, 1, 2, null, null, true);
    }

    public ConnectionSettings(final String httpBase, final String hostName, final String version, final int wait,
	    final int hold, final int maxRequests) {
	this(httpBase, hostName, version, wait, hold, maxRequests, null, null, true);
    }

    public ConnectionSettings(final String httpBase, final String hostName, final String version, final int wait,
	    final int hold, final int maxRequests, final String routeHost, final Integer routePort, final boolean secure) {
	this.httpBase = httpBase;
	this.hostName = hostName;
	this.version = version;
	this.wait = wait;
	this.hold = hold;
	this.maxRequests = maxRequests;
	this.routeHost = routeHost;
	this.routePort = routePort;
	this.secure = secure;
    }

    public ConnectionSettings(final String httpBase, final String hostName, final String routeHost,
	    final Integer routePort, final boolean secure) {
	this(httpBase, hostName, "1.6", 60, 1, 2, routeHost, routePort, secure);
    }
}
