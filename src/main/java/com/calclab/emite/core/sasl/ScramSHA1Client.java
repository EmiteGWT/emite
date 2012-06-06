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

package com.calclab.emite.core.sasl;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.calclab.emite.base.crypto.HMac;
import com.calclab.emite.base.crypto.PBKDF2;
import com.calclab.emite.base.crypto.SHA1Digest;
import com.calclab.emite.base.util.Base64;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

public final class ScramSHA1Client extends AbstractSaslClient {

	private static enum State {
		START, AUTH, DONE
	};

	private static final Random random = new Random();

	private String gs2hdr;
	private String cnonce;
	private String snonce;
	private byte[] salt;
	private int icount;

	private State state;

	public ScramSHA1Client(final Credentials credentials) {
		super("SCRAM-SHA-1", credentials);
		state = State.START;
		gs2hdr = "n,,"; // no channel binding, no authzid
		final byte[] rnd = new byte[16];
		random.nextBytes(rnd);
		cnonce = Base64.toBase64(rnd);
	}

	@Override
	@Nullable
	public byte[] getInitialResponse() {
		checkState(state == State.START);

		return (gs2hdr + "n=" + quote(credentials.getURI().getNode()) + ",r=" + cnonce).getBytes();
	}

	@Override
	@Nullable
	public byte[] evaluateChallenge(byte[] challenge) throws SaslException {
		checkState(!isComplete());

		final List<String> split = Lists.newArrayList(Splitter.on(',').split(new String(challenge)));

		switch (state) {
		case START:
			checkArgument(split.get(0).startsWith("r="));
			checkArgument(split.get(1).startsWith("s="));
			checkArgument(split.get(2).startsWith("i="));

			snonce = split.get(0).substring(2);
			if (!snonce.startsWith(cnonce))
				throw new SaslException("Invalid server nonce");

			salt = Base64.fromBase64(split.get(1).substring(2));
			icount = Integer.parseInt(split.get(2).substring(2));

			state = State.AUTH;
			return ("c=" + Base64.toBase64(gs2hdr.getBytes()) + ",r=" + snonce + ",p=" + Base64.toBase64(clientProof())).getBytes();
		case AUTH:
			checkArgument(split.get(0).startsWith("v="));
			
			final byte[] serverSignature = Base64.fromBase64(split.get(0).substring(2));
			
			if (!Arrays.equals(serverSignature, serverSignature()))
				throw new SaslException("Invalid server signature");

			state = State.DONE;
			complete = true;
			return null;
		default:
			throw new SaslException("Authentication is complete");
		}
	}

	private final byte[] authMessage() {
		final String cfm = "n=" + quote(credentials.getURI().getNode()) + ",r=" + cnonce;
		final String sfm = "r=" + snonce + ",s=" + Base64.toBase64(salt) + ",i=" + Integer.toString(icount);
		final String clm = "c=" + Base64.toBase64(gs2hdr.getBytes()) + ",r=" + snonce;
		return (cfm + "," + sfm + "," + clm).getBytes();
	}

	private final byte[] clientProof() {
		final byte[] saltedPassword = new PBKDF2().doKey(credentials.getPassword().getBytes(), salt, icount);
		final byte[] clientKey = new HMac().doMac(saltedPassword, "Client Key".getBytes());
		final byte[] storedKey = new SHA1Digest().doHash(clientKey);
		final byte[] clientSignature = new HMac().doMac(storedKey, authMessage());
		return XOR(clientKey, clientSignature);
	}

	private final byte[] serverSignature() {
		final byte[] saltedPassword = new PBKDF2().doKey(credentials.getPassword().getBytes(), salt, icount);
		final byte[] serverKey = new HMac().doMac(saltedPassword, "Server Key".getBytes());
		return new HMac().doMac(serverKey, authMessage());
	}

	private static final String quote(final String input) {
		return input.replace("=", "=3D").replace(",", "=2C");
	}

	private static final byte[] XOR(final byte[] a, final byte[] b) {
		checkArgument(a.length == b.length, "Both arrays must be the same length");

		final byte[] r = new byte[a.length];
		for (int i = 0; i < a.length; i++) {
			r[i] = (byte) (a[i] ^ b[i]);
		}

		return r;
	}

}
