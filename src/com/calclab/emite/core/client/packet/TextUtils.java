/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008-2009 The emite development team (see CREDITS for details)
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
 */
package com.calclab.emite.core.client.packet;

import com.google.gwt.regexp.shared.RegExp;

public class TextUtils {

    /* Pre-compile the regular expressions for the html escape/unescape methods */
    private static final RegExp AMP_RE = RegExp.compile("&", "g");
    private static final RegExp GT_RE = RegExp.compile(">", "g");
    private static final RegExp LT_RE = RegExp.compile("<", "g");
    private static final RegExp SQUOT_RE = RegExp.compile("\'", "g");
    private static final RegExp QUOT_RE = RegExp.compile("\"", "g");

    private static final RegExp AMP_HTML_RE = RegExp.compile("&amp;", "g");
    private static final RegExp GT_HTML_RE = RegExp.compile("&gt;", "g");
    private static final RegExp LT_HTML_RE = RegExp.compile("&lt;", "g");
    private static final RegExp SQUOT_HTML_RE = RegExp.compile("&#039;", "g");
    private static final RegExp QUOT_HTML_RE = RegExp.compile("&quot;", "g");

    // Original regexp from http://snippets.dzone.com/posts/show/452
    public static final String URL_REGEXP = "((ftp|http|https):\\/\\/(\\w+:{0,1}\\w*@)?(\\S+)(:[0-9]+)?(\\/|\\/([\\w#!:.?+=&%@!\\-\\/]))?)";

    // Original regexp from http://www.regular-expressions.info/email.html
    public static final String EMAIL_REGEXP = "[-!#$%&\'*+/=?_`{|}~a-z0-9^]+(\\.[-!#$%&\'*+/=?_`{|}~a-z0-9^]+)*@(localhost|([a-z0-9]([-a-z0-9]*[a-z0-9])?\\.)+[a-z0-9]([-a-z0-9]*[a-z0-9]))?";

    /**
     * Ellipsis.
     * 
     * @param text
     *            the string to truncate
     * @param length
     *            the size (if 0 returns the same text, if null return an empty
     *            string)
     * 
     * @return the result string
     */
    public static String ellipsis(final String text, final int length) {
	return text == null ? "" : length == 0 ? text : text.length() > length ? text.substring(0, length - 3) + "..."
		: text;
    }

    /*
     * This method escape only some dangerous html chars
     */
    public static String escape(String source) {
	if (source == null) {
	    return null;
	}
	if (source.indexOf("&") != -1) {
	    source = AMP_RE.replace(source, "&amp;");
	}
	if (source.indexOf("<") != -1) {
	    source = LT_RE.replace(source, "&lt;");
	}
	if (source.indexOf(">") != -1) {
	    source = GT_RE.replace(source, "&gt;");
	}
	if (source.indexOf("\"") != -1) {
	    source = QUOT_RE.replace(source, "&quot;");
	}
	if (source.indexOf("\'") != -1) {
	    source = SQUOT_RE.replace(source, "&#39;");
	}
	return source;
    }

    /*
     * This method unescape only some dangerous html chars for use in GWT Html
     * widget for instance
     */
    public static String unescape(String source) {
	if (source == null) {
	    return null;
	}
	if (source.indexOf("&amp;") != -1) {
	    source = AMP_HTML_RE.replace(source, "&");
	}
	if (source.indexOf("&lt;") != -1) {
	    source = LT_HTML_RE.replace(source, "<");
	}
	if (source.indexOf("&gt;") != -1) {
	    source = GT_HTML_RE.replace(source, ">");
	}
	if (source.indexOf("&quot;") != -1) {
	    source = QUOT_HTML_RE.replace(source, "\"");
	}
	if (source.indexOf("&#39;") != -1) {
	    source = SQUOT_HTML_RE.replace(source, "\'");
	}
	return source;
    }
}
