/*
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008-2010 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * ***** this code has been copied by Google Web Toolkit trunk,
 * candidate for 2.1 version. *****
 *
 * Here follows the original Copyright statement
 *
 * -----------------------------------------
 *
 * Copyright 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.calclab.emite.core.client.xmpp.datetime.gwt;

import com.google.gwt.i18n.client.constants.DateTimeConstants;

/**
 * <strong>Copied and modified from <a href=
 * "http://google-web-toolkit.googlecode.com/svn/trunk/user/src/com/google/gwt/i18n/client/"
 * >Google Web Toolkit trunk, revision 8757</a>.</strong>
 * 
 * Adapter that makes a {@link DateTimeConstants} implementation suitable for
 * use with something that wants a
 * {@link com.google.gwt.i18n.client.DateTimeFormatInfo}. Values not present in
 * {@link DateTimeConstants} receive default values.
 */
class DateTimeFormatInfoAdapter extends DefaultDateTimeFormatInfo {

    private final DateTimeConstants dtc;

    public DateTimeFormatInfoAdapter(DateTimeConstants dtc) {
	this.dtc = dtc;
    }

    // CHECKSTYLE_OFF

    @Override
    public String[] ampms() {
	return dtc.ampms();
    }

    @Override
    public String dateFormatFull() {
	return dtc.dateFormats()[0];
    }

    @Override
    public String dateFormatLong() {
	return dtc.dateFormats()[1];
    }

    @Override
    public String dateFormatMedium() {
	return dtc.dateFormats()[2];
    }

    @Override
    public String dateFormatShort() {
	return dtc.dateFormats()[3];
    }

    @Override
    public String[] erasFull() {
	return dtc.eraNames();
    }

    @Override
    public String[] erasShort() {
	return dtc.eras();
    }

    @Override
    public int firstDayOfTheWeek() {
	return Integer.valueOf(dtc.firstDayOfTheWeek()) - 1;
    }

    @Override
    public String[] monthsFull() {
	return dtc.months();
    }

    @Override
    public String[] monthsFullStandalone() {
	return dtc.standaloneMonths();
    }

    @Override
    public String[] monthsNarrow() {
	return dtc.narrowMonths();
    }

    @Override
    public String[] monthsNarrowStandalone() {
	return dtc.standaloneNarrowMonths();
    }

    @Override
    public String[] monthsShort() {
	return dtc.shortMonths();
    }

    @Override
    public String[] monthsShortStandalone() {
	return dtc.standaloneShortMonths();
    }

    @Override
    public String[] quartersFull() {
	return dtc.quarters();
    }

    @Override
    public String[] quartersShort() {
	return dtc.shortQuarters();
    }

    @Override
    public String timeFormatFull() {
	return dtc.timeFormats()[0];
    }

    @Override
    public String timeFormatLong() {
	return dtc.timeFormats()[1];
    }

    @Override
    public String timeFormatMedium() {
	return dtc.timeFormats()[2];
    }

    @Override
    public String timeFormatShort() {
	return dtc.timeFormats()[3];
    }

    @Override
    public String[] weekdaysFull() {
	return dtc.weekdays();
    }

    @Override
    public String[] weekdaysFullStandalone() {
	return dtc.standaloneWeekdays();
    }

    @Override
    public String[] weekdaysNarrow() {
	return dtc.narrowWeekdays();
    }

    @Override
    public String[] weekdaysNarrowStandalone() {
	return dtc.standaloneNarrowWeekdays();
    }

    @Override
    public String[] weekdaysShort() {
	return dtc.shortWeekdays();
    }

    @Override
    public String[] weekdaysShortStandalone() {
	return dtc.standaloneShortWeekdays();
    }

    @Override
    public int weekendEnd() {
	return Integer.valueOf(dtc.weekendRange()[1]) - 1;
    }

    @Override
    public int weekendStart() {
	return Integer.valueOf(dtc.weekendRange()[0]) - 1;
    }
}
