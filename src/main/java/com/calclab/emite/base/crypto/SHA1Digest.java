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
 * implementation of SHA-1 as outlined in "Handbook of Applied Cryptography",
 * pages 346 - 349.
 *
 * It is interesting to ponder why the, apart from the extra IV, the other
 * difference here from MD5 is the "endienness" of the word processing!
 */
public final class SHA1Digest extends AbstractDigest {
	private static final int DIGEST_LENGTH = 20;
	private static final int BYTE_LENGTH = 64;

	private int H1, H2, H3, H4, H5; // IV's

	private final int[] X = new int[80];
	private int xOff;

	/**
	 * Standard constructor
	 */
	public SHA1Digest() {
		reset();
	}

	/**
	 * Copy constructor. This will copy the state of the provided message
	 * digest.
	 */
	public SHA1Digest(final SHA1Digest t) {
		super(t);

		H1 = t.H1;
		H2 = t.H2;
		H3 = t.H3;
		H4 = t.H4;
		H5 = t.H5;

		System.arraycopy(t.X, 0, X, 0, t.X.length);
		xOff = t.xOff;
	}

	@Override
	public final String getAlgorithmName() {
		return "SHA-1";
	}

	@Override
	public final int getDigestSize() {
		return DIGEST_LENGTH;
	}

	@Override
	public final int getByteLength() {
		return BYTE_LENGTH;
	}

	@Override
	protected final void processWord(final byte[] in, final int inOff) {
		X[xOff] = bigEndianToInt(in, inOff);

		if (++xOff == 16) {
			processBlock();
		}
	}

	@Override
	protected final void processLength(final long bitLength) {
		if (xOff > 14) {
			processBlock();
		}

		X[14] = (int) (bitLength >>> 32);
		X[15] = (int) (bitLength & 0xffffffff);
	}

	@Override
	public final int doFinal(final byte[] out, final int outOff) {
		finish();

		intToBigEndian(H1, out, outOff);
		intToBigEndian(H2, out, outOff + 4);
		intToBigEndian(H3, out, outOff + 8);
		intToBigEndian(H4, out, outOff + 12);
		intToBigEndian(H5, out, outOff + 16);

		reset();

		return DIGEST_LENGTH;
	}

	/**
	 * reset the chaining variables
	 */
	@Override
	public final void reset() {
		super.reset();

		H1 = 0x67452301;
		H2 = 0xefcdab89;
		H3 = 0x98badcfe;
		H4 = 0x10325476;
		H5 = 0xc3d2e1f0;

		xOff = 0;
		for (int i = 0; i != X.length; i++) {
			X[i] = 0;
		}
	}

	//
	// Additive constants
	//
	private static final int Y1 = 0x5a827999;
	private static final int Y2 = 0x6ed9eba1;
	private static final int Y3 = 0x8f1bbcdc;
	private static final int Y4 = 0xca62c1d6;

	private static final int F(final int u, final int v, final int w) {
		return ((u & v) | ((~u) & w));
	}

	private static final int H(final int u, final int v, final int w) {
		return (u ^ v ^ w);
	}

	private static final int G(final int u, final int v, final int w) {
		return ((u & v) | (u & w) | (v & w));
	}

	@Override
	protected final void processBlock() {
		//
		// expand 16 word block into 80 word block.
		//
		for (int i = 16; i < 80; i++) {
			final int t = X[i - 3] ^ X[i - 8] ^ X[i - 14] ^ X[i - 16];
			X[i] = rotateLeft(t, 1);
		}

		//
		// set up working variables.
		//
		int A = H1;
		int B = H2;
		int C = H3;
		int D = H4;
		int E = H5;

		//
		// round 1
		//
		int idx = 0;

		for (int j = 0; j < 4; j++) {
			E = rotateLeft(A, 5) + F(B, C, D) + E + X[idx++] + Y1;
			B = rotateLeft(B, 30);

			D = rotateLeft(E, 5) + F(A, B, C) + D + X[idx++] + Y1;
			A = rotateLeft(A, 30);

			C = rotateLeft(D, 5) + F(E, A, B) + C + X[idx++] + Y1;
			E = rotateLeft(E, 30);

			B = rotateLeft(C, 5) + F(D, E, A) + B + X[idx++] + Y1;
			D = rotateLeft(D, 30);

			A = rotateLeft(B, 5) + F(C, D, E) + A + X[idx++] + Y1;
			C = rotateLeft(C, 30);
		}

		//
		// round 2
		//
		for (int j = 0; j < 4; j++) {
			E = rotateLeft(A, 5) + H(B, C, D) + E + X[idx++] + Y2;
			B = rotateLeft(B, 30);

			D = rotateLeft(E, 5) + H(A, B, C) + D + X[idx++] + Y2;
			A = rotateLeft(A, 30);

			C = rotateLeft(D, 5) + H(E, A, B) + C + X[idx++] + Y2;
			E = rotateLeft(E, 30);

			B = rotateLeft(C, 5) + H(D, E, A) + B + X[idx++] + Y2;
			D = rotateLeft(D, 30);

			A = rotateLeft(B, 5) + H(C, D, E) + A + X[idx++] + Y2;
			C = rotateLeft(C, 30);
		}

		//
		// round 3
		//
		for (int j = 0; j < 4; j++) {
			E = rotateLeft(A, 5) + G(B, C, D) + E + X[idx++] + Y3;
			B = rotateLeft(B, 30);

			D = rotateLeft(E, 5) + G(A, B, C) + D + X[idx++] + Y3;
			A = rotateLeft(A, 30);

			C = rotateLeft(D, 5) + G(E, A, B) + C + X[idx++] + Y3;
			E = rotateLeft(E, 30);

			B = rotateLeft(C, 5) + G(D, E, A) + B + X[idx++] + Y3;
			D = rotateLeft(D, 30);

			A = rotateLeft(B, 5) + G(C, D, E) + A + X[idx++] + Y3;
			C = rotateLeft(C, 30);
		}

		//
		// round 4
		//
		for (int j = 0; j <= 3; j++) {
			E = rotateLeft(A, 5) + H(B, C, D) + E + X[idx++] + Y4;
			B = rotateLeft(B, 30);

			D = rotateLeft(E, 5) + H(A, B, C) + D + X[idx++] + Y4;
			A = rotateLeft(A, 30);

			C = rotateLeft(D, 5) + H(E, A, B) + C + X[idx++] + Y4;
			E = rotateLeft(E, 30);

			B = rotateLeft(C, 5) + H(D, E, A) + B + X[idx++] + Y4;
			D = rotateLeft(D, 30);

			A = rotateLeft(B, 5) + H(C, D, E) + A + X[idx++] + Y4;
			C = rotateLeft(C, 30);
		}

		H1 += A;
		H2 += B;
		H3 += C;
		H4 += D;
		H5 += E;

		//
		// reset start of the buffer.
		//
		xOff = 0;
		for (int i = 0; i < 16; i++) {
			X[i] = 0;
		}
	}
}
