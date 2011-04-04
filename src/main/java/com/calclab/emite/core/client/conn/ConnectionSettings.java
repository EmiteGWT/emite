package com.calclab.emite.core.client.conn;

public class ConnectionSettings {
	public static String BOSH_VERSION = "1.6";
	public static int DEFAULT_WAIT = 60;
	public static int DEFAULT_HOLD = 1;
	public static int DEFAULT_MAX_REQUESTS = 2;
	
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
	this(httpBase, hostName, BOSH_VERSION, DEFAULT_WAIT, DEFAULT_HOLD, DEFAULT_MAX_REQUESTS, null, null, true);
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
	this(httpBase, hostName, BOSH_VERSION, DEFAULT_WAIT, DEFAULT_HOLD, DEFAULT_MAX_REQUESTS, routeHost, routePort, secure);
    }
    
    public ConnectionSettings(final String httpBase, final String hostName,  final Integer wait,
    	    final Integer hold, final String routeHost, final Integer routePort, final boolean secure) {
    	this(httpBase, hostName, BOSH_VERSION, wait == null ? DEFAULT_WAIT : wait,hold == null ? DEFAULT_HOLD : hold,
    			DEFAULT_MAX_REQUESTS, routeHost, routePort, secure);
    }
}
