/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.calclab.emite.base.util;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Converts hexadecimal Strings.
 * 
 * @author Apache Software Foundation
 */
public final class Hex {

	/**
	 * Used to build output as Hex
	 */
	private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * Converts an array of characters representing hexadecimal values into an
	 * array of bytes of those same values. The returned array will be half the
	 * length of the passed array, as it takes two characters to represent any
	 * given byte. An exception is thrown if the passed char array has an odd
	 * number of elements.
	 * 
	 * @param data
	 *            An array of characters containing hexadecimal digits
	 * @return A byte array containing binary data decoded from the supplied
	 *         char array.
	 * @throws IllegalArgumentException
	 *             Thrown if an odd number or illegal of characters is supplied
	 */
	public static final byte[] decodeHex(final char[] data) {
		checkArgument(data.length % 2 == 0, "Odd number of characters.");

		final byte[] out = new byte[data.length / 2];

		// two characters form the hex value.
		for (int i = 0, j = 0; j < data.length; i++) {
			int f = toDigit(data[j++]) << 4;
			f = f | toDigit(data[j++]);
			out[i] = (byte) (f & 0xFF);
		}

		return out;
	}

	/**
	 * Converts an array of bytes into an array of characters representing the
	 * hexadecimal values of each byte in order. The returned array will be
	 * double the length of the passed array, as it takes two characters to
	 * represent any given byte.
	 * 
	 * @param data
	 *            a byte[] to convert to Hex characters
	 * @return A char[] containing hexadecimal characters
	 */
	public static final char[] encodeHex(final byte[] data) {
		final char[] out = new char[data.length * 2];

		// two characters form the hex value.
		for (int i = 0, j = 0; i < data.length; i++) {
			out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
			out[j++] = DIGITS[0x0F & data[i]];
		}

		return out;
	}

	/**
	 * Converts an array of bytes into a String representing the hexadecimal
	 * values of each byte in order. The returned String will be double the
	 * length of the passed array, as it takes two characters to represent any
	 * given byte.
	 * 
	 * @param data
	 *            a byte[] to convert to Hex characters
	 * @return A String containing hexadecimal characters
	 * @since 1.4
	 */
	public static final String encodeHexString(final byte[] data) {
		return new String(encodeHex(data));
	}

	/**
	 * Converts a hexadecimal character to an integer.
	 * 
	 * @param ch
	 *            A character to convert to an integer digit
	 * @param index
	 *            The index of the character in the source
	 * @return An integer
	 * @throws IllegalArgumentException
	 *             Thrown if ch is an illegal hex character
	 */
	private static final int toDigit(final char ch) {
		final int digit = Character.digit(ch, 16);
		checkArgument(digit >= 0, "Illegal hexadecimal character %s.", String.valueOf(ch));
		return digit;
	}

	private Hex() {
	}

}
