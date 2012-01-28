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

package com.calclab.emite.base.util;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.LinkedList;
import java.util.Random;

import com.calclab.emite.base.digest.Digest;
import com.calclab.emite.base.digest.SHA1Digest;

public final class KeySequencer {
	private static final int DEFAULT_SIZE = 100;

	private static final Random random = new Random();
	private static final Digest digest = new SHA1Digest();

	private final LinkedList<String> keyList;

	public KeySequencer() {
		this(DEFAULT_SIZE);
	}

	public KeySequencer(final int size) {
		keyList = new LinkedList<String>();
		reset(size);
	}

	private static final String seed() {
		final byte[] seed = new byte[50];
		random.nextBytes(seed);
		return Hex.encodeHexString(digest.doHash(seed));
	}

	private static final String nextDigest(final String current) {
		return Hex.encodeHexString(digest.doHash(current.getBytes()));
	}

	public final void reset() {
		reset(DEFAULT_SIZE);
	}

	public final void reset(final int size) {
		checkArgument(size > 0);

		keyList.clear();
		String current = seed();
		for (int i = 0; i < size; i++) {
			current = nextDigest(current);
			keyList.addLast(current);
		}
	}

	public final boolean hasNext() {
		return !keyList.isEmpty();
	}

	public final String next() {
		if (keyList.isEmpty())
			return null;

		return keyList.removeLast();
	}

}
