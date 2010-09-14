package com.calclab.emite.core.client.xmpp.datetime;

import java.util.Date;

import com.calclab.emite.core.client.xmpp.datetime.gwt.DateTimeFormat.PredefinedFormat;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * A Xmpp Date Time helper<br/>
 * Provides two implementations, basically for testing: one for J2SE and one for
 * GWT
 * 
 * http://xmpp.org/extensions/xep-0082.html
 */
public class XmppDateTime {

    private static XmppDateTimeFormatter xdt = null;

    public static String formatLegacyFormatXMPPDateTime(final Date dateTime) {
	return xdt.formatLegacyFormatXmppDateTime(dateTime);
    }

    public static String formatXMPPDateTime(final Date dateTime) {
	return xdt.formatXmppDateTime(dateTime);
    }

    public static Date parseLegacyFormatXMPPDateTime(final String dateTime) {
	return xdt.parseLegacyFormatXmppDateTime(dateTime);
    }

    public static Date parseXMPPDateTime(final String dateTime) {
	return xdt.parseXmppDateTime(dateTime);
    }

    public static void use(final XmppDateTimeFormatter formatter) {
	xdt = formatter;
    }

    /**
     * Change to GWT implementation
     */
    public static void useGWT() {
	xdt = new XmppDateTimeFormatter() {
	    /*
	     * CCYY-MM-DDThh:mm:ss[.sss]TZD
	     */
	    private final DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy-MM-ddTHH:mm:ss[.sss]Z");

	    private final DateTimeFormat deprecatedDtf = DateTimeFormat.getFormat("yyyyMMddTHH:mm:ss");

	    @Override
	    public String formatLegacyFormatXmppDateTime(Date dateTime) {
		return deprecatedDtf.format(dateTime);
	    }

	    @Override
	    public String formatXmppDateTime(final Date dateTime) {
		return dtf.format(dateTime);
	    }

	    @Override
	    public Date parseLegacyFormatXmppDateTime(String dateTime) {
		return deprecatedDtf.parse(dateTime);
	    }

	    @Override
	    public Date parseXmppDateTime(final String dateTime) {
		return dtf.parse(dateTime);
	    }
	};
    }

    /**
     * Change to GWT implementation
     */
    public static void useGWT21() {
	xdt = new XmppDateTimeFormatter() {
	    /*
	     * CCYY-MM-DDThh:mm:ss[.sss]TZD
	     */
	    private final com.calclab.emite.core.client.xmpp.datetime.gwt.DateTimeFormat dtf = com.calclab.emite.core.client.xmpp.datetime.gwt.DateTimeFormat
		    .getFormat(PredefinedFormat.ISO_8601);

	    private final com.calclab.emite.core.client.xmpp.datetime.gwt.DateTimeFormat noMillisDtf = com.calclab.emite.core.client.xmpp.datetime.gwt.DateTimeFormat
		    .getFormat("yyyy-MM-dd'T'HH:mm:ssZZZ");

	    private final com.calclab.emite.core.client.xmpp.datetime.gwt.DateTimeFormat deprecatedDtf = com.calclab.emite.core.client.xmpp.datetime.gwt.DateTimeFormat
		    .getFormat("yyyyMMdd'T'HH:mm:ss");

	    @Override
	    public String formatLegacyFormatXmppDateTime(Date dateTime) {
		return deprecatedDtf.format(dateTime);
	    }

	    @Override
	    public String formatXmppDateTime(final Date dateTime) {
		return dtf.format(dateTime);
	    }

	    @Override
	    public Date parseLegacyFormatXmppDateTime(String dateTime) {
		return deprecatedDtf.parse(dateTime);
	    }

	    @Override
	    public Date parseXmppDateTime(final String dateTime) {
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
	};
    }

}
