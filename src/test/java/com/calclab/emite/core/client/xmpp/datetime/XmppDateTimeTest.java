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

package com.calclab.emite.core.client.xmpp.datetime;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

import com.google.gwt.junit.client.GWTTestCase;

public class XmppDateTimeTest extends GWTTestCase {

	@Override
	public String getModuleName() {
		return "com.calclab.emite.core.EmiteCore";
	}

	@Test
	public void testUseGWT21() {
		final Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(1980, Calendar.APRIL, 15, 15, 15, 02);
		cal.add(Calendar.MILLISECOND, cal.getTimeZone().getOffset(cal.getTimeInMillis()));
		Date date = cal.getTime();
		assertEquals(date, XmppDateTime.parseLegacyFormatXMPPDateTime("19800415T15:15:02"));
		assertEquals("19800415T15:15:02", XmppDateTime.formatLegacyFormatXMPPDateTime(date));
		cal.setTimeZone(TimeZone.getTimeZone("GMT"));
		cal.set(Calendar.MILLISECOND, 0); // Curious... need to do it otherwise
		// the next test fails...
		date = cal.getTime();
		assertEquals(date, XmppDateTime.parseXMPPDateTime("1980-04-15T17:15:02Z"));
		cal.setTimeZone(TimeZone.getTimeZone("GMT+01:00"));
		cal.set(Calendar.MILLISECOND, 159);
		date = cal.getTime();
		assertEquals(date, XmppDateTime.parseXMPPDateTime("1980-04-15T17:15:02.159+01:00"));
		// Cannot test it in another way, unfortunately the TimeZone of my
		// country comes in the way
		// forcing to use +2:00 as a time zone offset.
		assertEquals(date, XmppDateTime.parseXMPPDateTime(XmppDateTime.formatXMPPDateTime(date)));
	}
}
