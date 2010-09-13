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
 * WARRANTIE/**
S OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.calclab.emite.core.client.xmpp.datetime.gwt;

// DO NOT EDIT - GENERATED FROM CLDR DATA

/**
 * <strong>Copied and modified from <a href=
 * "http://google-web-toolkit.googlecode.com/svn/trunk/user/src/com/google/gwt/i18n/client/"
 * >Google Web Toolkit trunk, revision 8757</a>.</strong>
 * 
 * Default implementation of DateTimeFormatInfo interface, using values from the
 * CLDR root locale.
 * <p>
 * Users who need to create their own DateTimeFormatInfo implementation are
 * encouraged to extend this class so their implementation won't break when new
 * methods are added.
 */
public class DefaultDateTimeFormatInfo implements DateTimeFormatInfo {

    @Override
    public String[] ampms() {
	return new String[] { "AM", "PM" };
    }

    @Override
    public String dateFormat() {
	return dateFormatMedium();
    }

    @Override
    public String dateFormatFull() {
	return "EEEE, MMMM d, y";
    }

    @Override
    public String dateFormatLong() {
	return "MMMM d, y";
    }

    @Override
    public String dateFormatMedium() {
	return "MMM d, y";
    }

    @Override
    public String dateFormatShort() {
	return "M/d/yy";
    }

    @Override
    public String dateTime(String timePattern, String datePattern) {
	return datePattern + " " + timePattern;
    }

    @Override
    public String dateTimeFull(String timePattern, String datePattern) {
	return dateTime(timePattern, datePattern);
    }

    @Override
    public String dateTimeLong(String timePattern, String datePattern) {
	return dateTime(timePattern, datePattern);
    }

    @Override
    public String dateTimeMedium(String timePattern, String datePattern) {
	return dateTime(timePattern, datePattern);
    }

    @Override
    public String dateTimeShort(String timePattern, String datePattern) {
	return dateTime(timePattern, datePattern);
    }

    @Override
    public String[] erasFull() {
	return new String[] { "Before Christ", "Anno Domini" };
    }

    @Override
    public String[] erasShort() {
	return new String[] { "BC", "AD" };
    }

    @Override
    public int firstDayOfTheWeek() {
	return 0;
    }

    @Override
    public String formatDay() {
	return "d";
    }

    @Override
    public String formatHour12Minute() {
	return "h:mm a";
    }

    @Override
    public String formatHour12MinuteSecond() {
	return "h:mm:ss a";
    }

    @Override
    public String formatHour24Minute() {
	return "HH:mm";
    }

    @Override
    public String formatHour24MinuteSecond() {
	return "HH:mm:ss";
    }

    @Override
    public String formatMinuteSecond() {
	return "mm:ss";
    }

    @Override
    public String formatMonthAbbrev() {
	return "LLL";
    }

    @Override
    public String formatMonthAbbrevDay() {
	return "MMM d";
    }

    @Override
    public String formatMonthFull() {
	return "LLLL";
    }

    @Override
    public String formatMonthFullDay() {
	return "MMMM d";
    }

    @Override
    public String formatMonthFullWeekdayDay() {
	return "EEEE MMMM d";
    }

    @Override
    public String formatMonthNumDay() {
	return "M-d";
    }

    @Override
    public String formatYear() {
	return "y";
    }

    @Override
    public String formatYearMonthAbbrev() {
	return "y MMM";
    }

    @Override
    public String formatYearMonthAbbrevDay() {
	return "y MMM d";
    }

    @Override
    public String formatYearMonthFull() {
	return "y MMMM";
    }

    @Override
    public String formatYearMonthFullDay() {
	return "y MMMM d";
    }

    @Override
    public String formatYearMonthNum() {
	return "y-M";
    }

    @Override
    public String formatYearMonthNumDay() {
	return "y-M-d";
    }

    @Override
    public String formatYearMonthWeekdayDay() {
	return "EEE, y MMM d";
    }

    @Override
    public String formatYearQuarterFull() {
	return "y QQQQ";
    }

    @Override
    public String formatYearQuarterShort() {
	return "y Q";
    }

    @Override
    public String[] monthsFull() {
	return new String[] { "January", "February", "March", "April", "May", "June", "July", "August", "September",
		"October", "November", "December" };
    }

    @Override
    public String[] monthsFullStandalone() {
	return monthsFull();
    }

    @Override
    public String[] monthsNarrow() {
	return new String[] { "J", "F", "M", "A", "M", "J", "J", "A", "S", "O", "N", "D" };
    }

    @Override
    public String[] monthsNarrowStandalone() {
	return monthsNarrow();
    }

    @Override
    public String[] monthsShort() {
	return new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
    }

    @Override
    public String[] monthsShortStandalone() {
	return monthsShort();
    }

    @Override
    public String[] quartersFull() {
	return new String[] { "1st quarter", "2nd quarter", "3rd quarter", "4th quarter" };
    }

    @Override
    public String[] quartersShort() {
	return new String[] { "Q1", "Q2", "Q3", "Q4" };
    }

    @Override
    public String timeFormat() {
	return timeFormatMedium();
    }

    @Override
    public String timeFormatFull() {
	return "h:mm:ss a zzzz";
    }

    @Override
    public String timeFormatLong() {
	return "h:mm:ss a z";
    }

    @Override
    public String timeFormatMedium() {
	return "h:mm:ss a";
    }

    @Override
    public String timeFormatShort() {
	return "h:mm a";
    }

    @Override
    public String[] weekdaysFull() {
	return new String[] { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };
    }

    @Override
    public String[] weekdaysFullStandalone() {
	return weekdaysFull();
    }

    @Override
    public String[] weekdaysNarrow() {
	return new String[] { "S", "M", "T", "W", "T", "F", "S" };
    }

    @Override
    public String[] weekdaysNarrowStandalone() {
	return weekdaysNarrow();
    }

    @Override
    public String[] weekdaysShort() {
	return new String[] { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
    }

    @Override
    public String[] weekdaysShortStandalone() {
	return weekdaysShort();
    }

    @Override
    public int weekendEnd() {
	return 0;
    }

    @Override
    public int weekendStart() {
	return 6;
    }
}
