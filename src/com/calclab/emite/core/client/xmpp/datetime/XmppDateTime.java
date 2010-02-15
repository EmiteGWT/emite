package com.calclab.emite.core.client.xmpp.datetime;

import java.util.Date;

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

    public static String formatXMPPDateTime(final Date dateTime) {
	return xdt.formatXmppDateTime(dateTime);
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

	    @Override
	    public String formatXmppDateTime(final Date dateTime) {
		return dtf.format(dateTime);
	    }

	    @Override
	    public Date parseXmppDateTime(final String dateTime) {
		return dtf.parse(dateTime);
	    }
	};
    }

}
