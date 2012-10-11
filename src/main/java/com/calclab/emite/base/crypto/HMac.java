/*
 * Copyright (c) 2000-2010 The Legion Of The Bouncy Castle (http://www.bouncycastle.org)
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package com.calclab.emite.base.crypto;

/**
 * HMAC implementation based on RFC2104
 *
 * H(K XOR opad, H(K XOR ipad, text))
 */
public final class HMac {

	private final static byte IPAD = (byte) 0x36;
	private final static byte OPAD = (byte) 0x5C;

	private final Digest digest;
	private final int digestSize;
	private final int blockLength;

	private final byte[] inputPad;
	private final byte[] outputPad;

	public HMac() {
		this(new SHA1Digest());
	}

	/**
	 * Base constructor for one of the standard digest algorithms that the
	 * byteLength of the algorithm is know for.
	 *
	 * @param digest
	 *            the digest.
	 */
	public HMac(final Digest digest) {
		this.digest = digest;
		digestSize = digest.getDigestSize();

		this.blockLength = digest.getByteLength();

		inputPad = new byte[blockLength];
		outputPad = new byte[blockLength];
	}

	public final byte[] doMac(final byte[] key, final byte[] data) {
		reset();
		init(key);
		update(data, 0, data.length);
		final byte[] out = new byte[getMacSize()];
		doFinal(out, 0);
		return out;
	}

	/**
	 * Return the name of the algorithm the MAC implements.
	 *
	 * @return the name of the algorithm the MAC implements.
	 */
	public final String getAlgorithmName() {
		return "HMAC-" + digest.getAlgorithmName();
	}

	/**
	 * Initialise the MAC.
	 *
	 * @param key
	 *            the key required by the MAC.
	 */
	public final void init(final byte[] key) {
		digest.reset();

		if (key.length > blockLength) {
			digest.update(key, 0, key.length);
			digest.doFinal(inputPad, 0);
			for (int i = digestSize; i < inputPad.length; i++) {
				inputPad[i] = 0;
			}
		} else {
			System.arraycopy(key, 0, inputPad, 0, key.length);
			for (int i = key.length; i < inputPad.length; i++) {
				inputPad[i] = 0;
			}
		}

		// outputPad = new byte[inputPad.length];
		System.arraycopy(inputPad, 0, outputPad, 0, inputPad.length);

		for (int i = 0; i < inputPad.length; i++) {
			inputPad[i] ^= IPAD;
		}

		for (int i = 0; i < outputPad.length; i++) {
			outputPad[i] ^= OPAD;
		}

		digest.update(inputPad, 0, inputPad.length);
	}

	/**
	 * Return the block size for this MAC (in bytes).
	 *
	 * @return the block size for this MAC in bytes.
	 */
	public final int getMacSize() {
		return digestSize;
	}

	/**
	 * add a single byte to the mac for processing.
	 *
	 * @param in
	 *            the byte to be processed.
	 */
	public final void update(final byte in) {
		digest.update(in);
	}

	/**
	 * @param in
	 *            the array containing the input.
	 * @param inOff
	 *            the index in the array the data begins at.
	 * @param len
	 *            the length of the input starting at inOff.
	 */
	public final void update(final byte[] in, final int inOff, final int len) {
		digest.update(in, inOff, len);
	}

	/**
	 * Compute the final stage of the MAC writing the output to the out
	 * parameter.
	 * <p>
	 * doFinal leaves the MAC in the same state it was after the last init.
	 *
	 * @param out
	 *            the array the MAC is to be output to.
	 * @param outOff
	 *            the offset into the out buffer the output is to start at.
	 */
	public final int doFinal(final byte[] out, final int outOff) {
		final byte[] tmp = new byte[digestSize];
		digest.doFinal(tmp, 0);

		digest.update(outputPad, 0, outputPad.length);
		digest.update(tmp, 0, tmp.length);

		final int len = digest.doFinal(out, outOff);

		reset();

		return len;
	}

	/**
	 * Reset the MAC. At the end of resetting the MAC should be in the in the
	 * same state it was after the last init (if there was one).
	 */
	public final void reset() {
		digest.reset();
		digest.update(inputPad, 0, inputPad.length);
	}
}
