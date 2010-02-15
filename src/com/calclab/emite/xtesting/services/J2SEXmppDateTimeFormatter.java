package com.calclab.emite.xtesting.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.calclab.emite.core.client.xmpp.datetime.XmppDateTimeFormatter;

public class J2SEXmppDateTimeFormatter implements XmppDateTimeFormatter {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ss[.sss]Z");

    @Override
    public String formatXmppDateTime(final Date dateTime) {
	return sdf.format(dateTime);
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
