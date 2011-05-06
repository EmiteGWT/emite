/**
 *
 */
package com.calclab.emite.core.client.packet;

import org.junit.Assert;

import com.calclab.emite.core.client.packet.PacketTestSuite.Helper;

public abstract class AbstractHelperTest implements Helper {
	@Override
	public void assertEquals(final Object expected, final Object actual) {
		Assert.assertEquals(expected, actual);
	}

	@Override
	public void assertTrue(final String message, final boolean condition) {
		Assert.assertTrue(message, condition);
	}

	@Override
	public void log(final String message) {

	}

}
