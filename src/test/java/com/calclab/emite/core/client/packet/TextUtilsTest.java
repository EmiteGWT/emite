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
