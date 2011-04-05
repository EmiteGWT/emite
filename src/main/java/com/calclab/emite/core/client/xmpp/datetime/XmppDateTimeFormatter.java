package com.calclab.emite.core.client.xmpp.datetime;

import java.util.Date;

/**
 * http://xmpp.org/extensions/xep-0082.html
 */
public interface XmppDateTimeFormatter {

    String formatLegacyFormatXmppDateTime(Date dateTime);

    /**
     * This method allows to format a date using the deprecated
     * "CCYYMMDDThh:mm:ss" format.
     * 
     * @param dateTime
     *            The string to parse to a date.
     * @return The parsed date.
     */
    String formatXmppDateTime(Date dateTime);

    /**
     * This method allows to parse a deprecated formatted date, with the
     * "CCYYMMDDThh:mm:ss" format.
     * 
     * @param dateTime
     *            The string to parse to a date.
     * @return The parsed date.
     */
    Date parseLegacyFormatXmppDateTime(String dateTime);

    Date parseXmppDateTime(String dateTime);
}
