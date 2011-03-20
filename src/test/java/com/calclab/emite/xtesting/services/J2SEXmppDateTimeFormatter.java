package com.calclab.emite.xtesting.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.calclab.emite.core.client.xmpp.datetime.XmppDateTimeFormatter;

public class J2SEXmppDateTimeFormatter implements XmppDateTimeFormatter {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ss[.sss]Z");
    SimpleDateFormat deprecatedSdf = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ss[.sss]Z");

    @Override
    public String formatLegacyFormatXmppDateTime(Date dateTime) {
	return deprecatedSdf.format(dateTime);
    }

    @Override
    public String formatXmppDateTime(final Date dateTime) {
	return sdf.format(dateTime);
    }

    @Override
    public Date parseLegacyFormatXmppDateTime(String dateTime) {
	try {
	    return deprecatedSdf.parse(dateTime);
	} catch (final ParseException e) {
	    return null;
	}
    }

    @Override
    public Date parseXmppDateTime(final String dateTime) {
	try {
	    return sdf.parse(dateTime);
	} catch (final ParseException e) {
	    return null;
	}
    }
}
