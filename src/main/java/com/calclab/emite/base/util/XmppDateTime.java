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

import java.util.Date;
import java.util.logging.Logger;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.i18n.client.TimeZone;

/**
 * XMPP date/time helper methods.
 * 
 * @see <a href="http://xmpp.org/extensions/xep-0082.html">XEP-0082</a>
 */
public final class XmppDateTime {

	private static final Logger logger = Logger.getLogger(XmppDateTime.class.getName());

	/*
	 * CCYY-MM-DDThh:mm:ss[.sss]TZD
	 */
	private static final DateTimeFormat dtf = DateTimeFormat.getFormat(PredefinedFormat.ISO_8601);
	private static final DateTimeFormat noMillisDtf = DateTimeFormat.getFormat("yyyy-MM-dd'T'HH:mm:ssZZZ");
	private static final DateTimeFormat deprecatedDtf = DateTimeFormat.getFormat("yyyyMMdd'T'HH:mm:ss");
	private static final TimeZone gmtTimeZone = TimeZone.createTimeZone(0);

	/**
	 * Formats a {@link Date} using the XMPP date/time format.
	 * 
	 * @param dateTime the date/time to be formatted
	 * @return the XMPP representation of the given date/time
	 */
	public static final String formatXMPPDateTime(final Date dateTime) {
		return dtf.format(dateTime);
	}
	
	/**
	 * Formats a {@link Date} using the legacy XMPP date/time format.
	 * 
	 * @param dateTime the date/time to be formatted
	 * @return the legacy representation of the given date/time
	 */
	@Deprecated
	public static final String formatLegacyFormatXMPPDateTime(final Date dateTime) {
		return deprecatedDtf.format(dateTime, gmtTimeZone);
	}

	/**
	 * Parses a XMPP date/time string into a {@link Date}.
	 * 
	 * @param dateTime the XMPP date/time string to be parsed
	 * @return the parsed date
	 */
	public static final Date parseXMPPDateTime(final String dateTime) {
		// Hack to replace unparseable "Z".
		final String date = dateTime.endsWith("Z") ? dateTime.substring(0, dateTime.length() - 1) + "GMT" : dateTime;
		
		try {
			return dtf.parse(date);
		} catch (final IllegalArgumentException e) {
			logger.warning("Cannot parse date-time '" + date + "' with normal pattern");
			return noMillisDtf.parse(date);
		}
	}

	/**
	 * Parses a legacy XMPP date/time string into a {@link Date}.
	 * 
	 * @param dateTime the legacy XMPP date/time string to be parsed
	 * @return the parsed date
	 */
	@Deprecated
	public static final Date parseLegacyFormatXMPPDateTime(final String dateTime) {
		final Date retValue = deprecatedDtf.parse(dateTime);
		// The server always sends a GMT date, so we compensate the timezone offset.
		retValue.setTime(retValue.getTime() - retValue.getTimezoneOffset() * 60 * 1000);
		return retValue;
	}

	private XmppDateTime() {
	}
}
