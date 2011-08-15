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
import java.util.logging.Logger;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.i18n.client.TimeZone;

/**
 * A Xmpp Date Time helper<br/>
 * Provides two implementations, basically for testing: one for J2SE and one for
 * GWT
 * 
 * http://xmpp.org/extensions/xep-0082.html
 */
public class XmppDateTime {
	
	private static final Logger logger = Logger.getLogger(XmppDateTime.class.getName());
	
	/*
	 * CCYY-MM-DDThh:mm:ss[.sss]TZD
	 */
	private static final DateTimeFormat dtf = DateTimeFormat.getFormat(PredefinedFormat.ISO_8601);
	private static final DateTimeFormat noMillisDtf = DateTimeFormat.getFormat("yyyy-MM-dd'T'HH:mm:ssZZZ");
	private static final DateTimeFormat deprecatedDtf = DateTimeFormat.getFormat("yyyyMMdd'T'HH:mm:ss");
	private static final TimeZone gmtTimeZone = TimeZone.createTimeZone(0);

	public static String formatLegacyFormatXMPPDateTime(final Date dateTime) {
		return deprecatedDtf.format(dateTime, gmtTimeZone);
	}

	public static String formatXMPPDateTime(final Date dateTime) {
		return dtf.format(dateTime);
	}

	@SuppressWarnings("deprecation")
	public static Date parseLegacyFormatXMPPDateTime(final String dateTime) {
		final Date retValue = deprecatedDtf.parse(dateTime);
		// The server always sends a GMT date, so we compensate the timezone offset.
		retValue.setTime(retValue.getTime() - retValue.getTimezoneOffset() * 60 * 1000);
		return retValue;
	}

	public static Date parseXMPPDateTime(final String dateTime) {
		String date = dateTime;
		if (date.endsWith("Z")) { // Hack to replace unparseable "Z".
			date = dateTime.substring(0, dateTime.length() - 1) + "GMT";
		}
		try {
			return dtf.parse(date);
		} catch (final IllegalArgumentException e) {
			logger.warning("Cannot parse date-time '" + date + "' with normal pattern");
			return noMillisDtf.parse(date);
		}
	}
}
