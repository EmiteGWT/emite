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

import java.util.LinkedList;
import java.util.Random;

import javax.annotation.Nullable;

import com.calclab.emite.base.crypto.Digest;
import com.calclab.emite.base.crypto.SHA1Digest;
import com.google.common.collect.Lists;

public final class KeySequencer {
	
	private static final Random random = new Random();
	private static final Digest digest = new SHA1Digest();

	private final LinkedList<String> keyList;

	public KeySequencer() {
		keyList = Lists.newLinkedList();
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
		final int size = 10 + random.nextInt(40);

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

	@Nullable public final String next() {
		if (keyList.isEmpty())
			return null;

		return keyList.removeLast();
	}

}
