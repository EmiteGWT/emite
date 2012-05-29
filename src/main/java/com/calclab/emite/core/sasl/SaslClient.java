package com.calclab.emite.core.sasl;

import javax.annotation.Nullable;

public interface SaslClient {

	String getMechanismName();
	
	@Nullable byte[] getInitialResponse();
	
	@Nullable byte[] evaluateChallenge(byte[] challenge) throws SaslException;
	
	boolean isComplete();
	
}
