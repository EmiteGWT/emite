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
 * interface that a message digest conforms to.
 */
public interface Digest {
	/**
	 * return the algorithm name
	 *
	 * @return the algorithm name
	 */
	String getAlgorithmName();

	/**
	 * return the size, in bytes, of the digest produced by this message digest.
	 *
	 * @return the size, in bytes, of the digest produced by this message
	 *         digest.
	 */
	int getDigestSize();

	/**
	 * Return the size in bytes of the internal buffer the digest applies it's
	 * compression function to.
	 *
	 * @return byte length of the digests internal buffer.
	 */
	int getByteLength();

	byte[] doHash(byte[] input);

	/**
	 * update the message digest with a single byte.
	 *
	 * @param in
	 *            the input byte to be entered.
	 */
	void update(byte in);

	/**
	 * update the message digest with a block of bytes.
	 *
	 * @param in
	 *            the byte array containing the data.
	 * @param inOff
	 *            the offset into the byte array where the data starts.
	 * @param len
	 *            the length of the data.
	 */
	void update(byte[] in, int inOff, int len);

	/**
	 * close the digest, producing the final digest value. The doFinal call
	 * leaves the digest reset.
	 *
	 * @param out
	 *            the array the digest is to be copied into.
	 * @param outOff
	 *            the offset into the out array the digest is to start at.
	 */
	int doFinal(byte[] out, int outOff);

	/**
	 * reset the digest back to it's initial state.
	 */
	void reset();
}
