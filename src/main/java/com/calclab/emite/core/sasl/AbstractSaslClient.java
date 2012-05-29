package com.calclab.emite.core.sasl;

import javax.annotation.Nullable;

public abstract class AbstractSaslClient implements SaslClient {
	
	private final String mechanismName;
	protected final Credentials credentials;
	protected boolean complete = false;
	
	protected AbstractSaslClient(final String mechanismName, final Credentials credentials) {
		this.mechanismName = mechanismName;
		this.credentials = credentials;
	}
	
	@Override
	public final String getMechanismName() {
		return mechanismName;
	}
	
	@Override
	@Nullable
	public byte[] getInitialResponse() {
		return null;
	}

	@Override
	@Nullable
	public byte[] evaluateChallenge(final byte[] challenge) throws SaslException {
		return null;
	}
	
	protected final void setComplete() {
		complete = true;
	}
	
	@Override
	public final boolean isComplete() {
		return complete;
	}
	
}
