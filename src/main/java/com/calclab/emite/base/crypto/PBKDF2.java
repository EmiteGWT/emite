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
 * Generator for PBE derived keys and ivs as defined by PKCS 5 V2.0 Scheme 2.
 * This generator uses a SHA-1 HMac as the calculation function.
 * <p>
 * The document this implementation is based on can be found at <a
 * href=http://www.rsasecurity.com/rsalabs/pkcs/pkcs-5/index.html> RSA's PKCS5
 * Page</a>
 */
public final class PBKDF2 {

	private final HMac hMac;

	protected byte[] password;
	protected byte[] salt;
	protected int iterationCount;

	/**
	 * construct a PKCS5 Scheme 2 Parameters generator.
	 */
	public PBKDF2() {
		hMac = new HMac(new SHA1Digest());
	}

	public final byte[] doKey(final byte[] password, final byte[] salt, final int iterationCount) {
		init(password, salt, iterationCount);
		return generateDerivedKey(hMac.getMacSize());
	}

	/**
	 * initialise the PBE generator.
	 *
	 * @param password
	 *            the password converted into bytes (see below).
	 * @param salt
	 *            the salt to be mixed with the password.
	 * @param iterationCount
	 *            the number of iterations the "mixing" function is to be
	 *            applied for.
	 */
	public final void init(final byte[] password, final byte[] salt, final int iterationCount) {
		this.password = password;
		this.salt = salt;
		this.iterationCount = iterationCount;
	}

	public final byte[] generateDerivedKey(final int dkLen) {
		final int hLen = hMac.getMacSize();
		final int l = (dkLen + hLen - 1) / hLen;
		final byte[] iBuf = new byte[4];
		final byte[] out = new byte[l * hLen];

		for (int i = 1; i <= l; i++) {
			intToOctet(iBuf, i);

			F(password, salt, iterationCount, iBuf, out, (i - 1) * hLen);
		}

		return out;
	}

	private void F(final byte[] P, final byte[] S, final int c, final byte[] iBuf, final byte[] out, final int outOff) {
		final byte[] state = new byte[hMac.getMacSize()];

		hMac.init(P);

		if (S != null) {
			hMac.update(S, 0, S.length);
		}

		hMac.update(iBuf, 0, iBuf.length);

		hMac.doFinal(state, 0);

		System.arraycopy(state, 0, out, outOff, state.length);

		if (c == 0) {
			throw new IllegalArgumentException("iteration count must be at least 1.");
		}

		for (int count = 1; count < c; count++) {
			hMac.init(P);
			hMac.update(state, 0, state.length);
			hMac.doFinal(state, 0);

			for (int j = 0; j != state.length; j++) {
				out[outOff + j] ^= state[j];
			}
		}
	}

	private void intToOctet(final byte[] buf, final int i) {
		buf[0] = (byte) (i >>> 24);
		buf[1] = (byte) (i >>> 16);
		buf[2] = (byte) (i >>> 8);
		buf[3] = (byte) i;
	}

}
