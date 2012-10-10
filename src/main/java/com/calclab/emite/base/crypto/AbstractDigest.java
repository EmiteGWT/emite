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
 * base implementation of MD4 family style digest as outlined in
 * "Handbook of Applied Cryptography", pages 344 - 347.
 */
abstract class AbstractDigest implements Digest {
	private final byte[] xBuf;
	private int xBufOff;
	private long byteCount;

	/**
	 * Standard constructor
	 */
	protected AbstractDigest() {
		xBuf = new byte[4];
	}

	@Override
	public final byte[] doHash(final byte[] input) {
		reset();
		update(input, 0, input.length);
		final byte[] out = new byte[getDigestSize()];
		doFinal(out, 0);
		return out;
	}

	/**
	 * Copy constructor. We are using copy constructors in place of the
	 * Object.clone() interface as this interface is not supported by J2ME.
	 */
	protected AbstractDigest(final AbstractDigest t) {
		xBuf = new byte[t.xBuf.length];
		System.arraycopy(t.xBuf, 0, xBuf, 0, t.xBuf.length);

		xBufOff = t.xBufOff;
		byteCount = t.byteCount;
	}

	@Override
	public final void update(final byte in) {
		xBuf[xBufOff++] = in;

		if (xBufOff == xBuf.length) {
			processWord(xBuf, 0);
			xBufOff = 0;
		}

		byteCount++;
	}

	@Override
	public final void update(final byte[] in, int inOff, int len) {
		//
		// fill the current word
		//
		while ((xBufOff != 0) && (len > 0)) {
			update(in[inOff]);

			inOff++;
			len--;
		}

		//
		// process whole words.
		//
		while (len > xBuf.length) {
			processWord(in, inOff);

			inOff += xBuf.length;
			len -= xBuf.length;
			byteCount += xBuf.length;
		}

		//
		// load in the remainder.
		//
		while (len > 0) {
			update(in[inOff]);

			inOff++;
			len--;
		}
	}

	protected final void finish() {
		final long bitLength = (byteCount << 3);

		//
		// add the pad bytes.
		//
		update((byte) 128);

		while (xBufOff != 0) {
			update((byte) 0);
		}

		processLength(bitLength);

		processBlock();
	}

	@Override
	public void reset() {
		byteCount = 0;

		xBufOff = 0;
		for (int i = 0; i < xBuf.length; i++) {
			xBuf[i] = 0;
		}
	}

	/*
	 * rotate int x left n bits.
	 */
	protected static final int rotateLeft(final int x, final int n) {
		return (x << n) | (x >>> (32 - n));
	}

	protected static final int bigEndianToInt(final byte[] bs, final int off) {
		int n = (bs[off] & 0xff) << 24;
		n |= (bs[off + 1] & 0xff) << 16;
		n |= (bs[off + 2] & 0xff) << 8;
		n |= (bs[off + 3] & 0xff);
		return n;
	}

	protected static int littleEndianToInt(final byte[] bs, final int off) {
		int n = (bs[off] & 0xff);
		n |= (bs[off + 1] & 0xff) << 8;
		n |= (bs[off + 2] & 0xff) << 16;
		n |= (bs[off + 3] & 0xff) << 24;
		return n;
	}

	protected static final void intToBigEndian(final int n, final byte[] bs, final int off) {
		bs[off] = (byte) (n >>> 24);
		bs[off + 1] = (byte) (n >>> 16);
		bs[off + 2] = (byte) (n >>> 8);
		bs[off + 3] = (byte) (n);
	}

	protected static final void intToLittleEndian(final int n, final byte[] bs, final int off) {
		bs[off] = (byte) (n);
		bs[off + 1] = (byte) (n >>> 8);
		bs[off + 2] = (byte) (n >>> 16);
		bs[off + 3] = (byte) (n >>> 24);
	}

	protected abstract void processWord(byte[] in, int inOff);

	protected abstract void processLength(long bitLength);

	protected abstract void processBlock();
}
