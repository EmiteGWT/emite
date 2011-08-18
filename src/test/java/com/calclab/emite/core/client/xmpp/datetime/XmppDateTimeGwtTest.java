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

import java.util.Date;

import org.junit.Test;

import com.google.gwt.junit.client.GWTTestCase;

public class XmppDateTimeGwtTest extends GWTTestCase {

	@Override
	public String getModuleName() {
		return "com.calclab.emite.core.EmiteCore";
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testUseGWT21() {
		Date date = new Date(80, 3, 15, 15, 15, 2);
		date.setTime(date.getTime() - (date.getTimezoneOffset() * 60000));
		
		assertEquals(date, XmppDateTime.parseLegacyFormatXMPPDateTime("19800415T15:15:02"));
		assertEquals("19800415T15:15:02", XmppDateTime.formatLegacyFormatXMPPDateTime(date));

		date = new Date(80, 3, 15, 17, 15, 2);
		date.setTime(date.getTime() - (date.getTimezoneOffset() * 60000));
		assertEquals(date, XmppDateTime.parseXMPPDateTime("1980-04-15T17:15:02Z"));
		
		date = new Date(80, 3, 15, 16, 15, 2);
		date.setTime(date.getTime() - (date.getTimezoneOffset() * 60000) + 159);
		
		assertEquals(date, XmppDateTime.parseXMPPDateTime("1980-04-15T17:15:02.159+01:00"));
		
		assertEquals(date, XmppDateTime.parseXMPPDateTime(XmppDateTime.formatXMPPDateTime(date)));
	}
}
