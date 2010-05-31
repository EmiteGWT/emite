package com.calclab.emite.core.client.xmpp.session;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;

public class Credentials {
    /**
     * The password is not encoded at all
     */
    public static final String ENCODING_NONE = "none";
    /**
     * The password is encoding using a Base64 algorithm
     */
    public static final String ENCODING_BASE64 = "plain";

    /**
     * The URI required to perform an anonymous login
     */
    public static final XmppURI ANONYMOUS = XmppURI.uri("anonymous", "", null);

    public static Credentials createAnonymous() {
	return new Credentials(ANONYMOUS, null, ENCODING_NONE);
    }
    XmppURI uri;
    String encodedPassword;

    String encodingMethod;

    public Credentials(final XmppURI uri, final String encodedPassword, final String encodingMethod) {
	if (uri == null) {
	    throw new NullPointerException("uri can't be null in LoginCredentials");
	}

	this.uri = uri;
	this.encodedPassword = encodedPassword;
	this.encodingMethod = encodingMethod;
    }

    public String getEncodedPassword() {
	return encodedPassword;
    }

    public String getEncodingMethod() {
	return encodingMethod;
    }

    public XmppURI getXmppUri() {
	return uri;
    }

    public boolean isAnoymous() {
	return uri == ANONYMOUS;
    }
}
