package com.calclab.emite.core.sasl;

import java.util.Random;

//TODO
public final class ScramSHA1Client extends AbstractSaslClient {
	
	private static final Random random = new Random();

	public ScramSHA1Client(final Credentials credentials) {
		super("SCRAM-SHA1", credentials);
	}
	
	
	
}
