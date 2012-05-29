package com.calclab.emite.core.sasl;

import com.calclab.emite.core.XmppURI;
import com.google.common.base.Ascii;

//TODO
public final class PlainClient extends AbstractSaslClient {
	
	public PlainClient(final Credentials credentials) {
		super("PLAIN", credentials);
	}

	@Override
	public final byte[] getInitialResponse() {
		final XmppURI uri = credentials.getURI();
		final String response = uri.toString() + Ascii.NUL + uri.getNode() + Ascii.NUL + credentials.getPassword();
		return response.getBytes();
	}

}
