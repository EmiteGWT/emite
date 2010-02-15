package com.calclab.emite.core.client.xmpp.datetime;

import java.util.Date;

/**
 * http://xmpp.org/extensions/xep-0082.html
 */
public interface XmppDateTimeFormatter {

    String formatXmppDateTime(Date dateTime);

    Date parseXmppDateTime(String dateTime);

}
