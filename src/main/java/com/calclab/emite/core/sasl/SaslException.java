package com.calclab.emite.core.sasl;

public class SaslException extends Exception {
	
	public SaslException() {
        super();
    }

    public SaslException(String message) {
        super(message);
    }

    public SaslException(String message, Throwable cause) {
        super(message, cause);
    }

    public SaslException(Throwable cause) {
        super(cause);
    }

}
