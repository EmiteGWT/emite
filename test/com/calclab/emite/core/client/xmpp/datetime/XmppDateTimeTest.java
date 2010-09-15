package com.calclab.emite.core.client.xmpp.datetime;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

public class XmppDateTimeTest {

    @Test
    public void testUseGWT21() {
	XmppDateTime.useGWT21();
	Calendar cal = Calendar.getInstance();
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
