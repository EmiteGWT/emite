package com.calclab.emite.core.client.xmpp.datetime;

import java.util.Date;

import com.google.gwt.core.client.GWT;
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

    public static Date parseLegacyFormatXMPPDateTime(final String dateTime) {
	Date retValue = deprecatedDtf.parse(dateTime);
	// The server always sends a GMT date, so we compensate the
	// timezone offset.
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
	} catch (IllegalArgumentException e) {
	    GWT.log("Cannot parse date-time '" + date + "' with normal pattern");
	    return noMillisDtf.parse(date);
	}
    }
}
