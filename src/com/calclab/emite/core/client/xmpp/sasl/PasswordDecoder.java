package com.calclab.emite.core.client.xmpp.sasl;

/**
 * A object that can decode a password
 * 
 * @author dani
 */
public interface PasswordDecoder {
    String decode(String encodingMethod, String encodedPassword);
}
