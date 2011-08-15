/*
 * ((e)) emite: A pure Google Web Toolkit XMPP library
 * Copyright (c) 2008-2011 The Emite development team
 * 
 * This file is part of Emite.
 *
 * Emite is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * Emite is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with Emite.  If not, see <http://www.gnu.org/licenses/>.
 */

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
