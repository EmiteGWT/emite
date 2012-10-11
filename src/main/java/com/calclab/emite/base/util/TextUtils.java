/*
 * ((e)) emite: A pure Google Web Toolkit XMPP library
 * Copyright (c) 2008-2011 The Emite development team
 * 
 * This file is part of Emite.
 *
 * Emite is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * Emite is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with Emite.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.calclab.emite.base.util;

/**
 * Utilities to transform text.
 * 
 * TODO: these are only used by Hablar, move them out of Emite.
 */
public final class TextUtils {

	/**
	 * Original regexp from http://snippets.dzone.com/posts/show/452
	 */
	public static final String URL_REGEXP = "((ftp|http|https):\\/\\/(\\w+:{0,1}\\w*@)?(\\S+)(:[0-9]+)?(\\/|\\/([\\w#!:.?+=&%@!\\-\\/]))?)";

	/**
	 * Original regexp from http://www.regular-expressions.info/email.html
	 */
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
	public static final String ellipsis(final String text, final int length) {
		return text == null ? "" : length == 0 ? text : text.length() > length ? text.substring(0, length - 3) + "..." : text;
	}

	private TextUtils(){
	}
	
}
