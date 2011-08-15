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

package com.calclab.emite.core.client.packet;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TextUtilsTest {

	@Test
	public void matchDemoEmail() {
		final String email = "test100@emitedemo.ourproject.org";
		assertTrue(email.matches(TextUtils.EMAIL_REGEXP));
	}

	@Test
	public void matchLocalhostEmail() {
		final String email = "me@localhost";
		assertTrue(email.matches(TextUtils.EMAIL_REGEXP));
	}

	@Test
	public void matchSimpleEmail() {
		final String email = "me@example.com";
		assertTrue(email.matches(TextUtils.EMAIL_REGEXP));
	}
}
