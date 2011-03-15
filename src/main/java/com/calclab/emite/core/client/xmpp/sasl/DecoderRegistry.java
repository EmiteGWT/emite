package com.calclab.emite.core.client.xmpp.sasl;

import java.util.HashMap;

import com.calclab.emite.core.client.xmpp.session.Credentials;

/**
 * A registry of password decorers. You can should register your own decoder
 * here if your are plannig to use a different encoding methods.
 * 
 * Currently "none" to "base64" and "base64" to "base64" decoders are provided
 * out of the box
 */
public class DecoderRegistry {
    private final HashMap<String, PasswordDecoder> decoders;

    public DecoderRegistry() {
	decoders = new HashMap<String, PasswordDecoder>();

	register(Credentials.ENCODING_NONE, new PasswordDecoder() {
	    @Override
	    public String decode(final String encodingMethod, final String encodedPassword) {
		assert Credentials.ENCODING_NONE.equals(encodingMethod) : "Input encoding should be none using this decoder";
		return encodedPassword;
	    }
	});

	register(Credentials.ENCODING_BASE64, new PasswordDecoder() {
	    @Override
	    public String decode(final String encodingMethod, final String encodedPassword) {
		assert Credentials.ENCODING_BASE64.equals(encodingMethod) : "Input encoding should be none using this decoder";
		return Base64Coder.decodeString(encodedPassword);
	    }
	});
    }

    /**
     * Get (if any) a password decoder that can handle the input encoding and
     * produces the output encoding.
     * 
     * @param encodingMethod
     *            the encoding this decoder should handle as input
     * @param outputEncoding
     *            the encoding method this decoder should produce as output
     * @return the password decoder if any that maches the conditions
     */
    public PasswordDecoder getDecoder(final String encodingMethod) {
	return decoders.get(encodingMethod);
    }

    /**
     * Register a new password decoder
     * 
     * @param encodingMethod
     *            the input encoding this decoder can handle
     * @param outputEncoding
     *            the output encoding this decoder produces
     * @param decoder
     *            the decoder (can be null to remove a decoder)
     */
    public void register(final String encodingMethod, final PasswordDecoder decoder) {
	decoders.put(encodingMethod, decoder);
    }

}
