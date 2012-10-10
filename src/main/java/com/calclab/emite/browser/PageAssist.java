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

package com.calclab.emite.browser;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nullable;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.DOM;

/**
 * An utility class to perform actions based on meta tags.
 */
public final class PageAssist {

	/**
	 * Get the value of meta information written in the html page. The meta
	 * information is a html tag with name of meta usually placed inside the the
	 * head section with two attributes: id and content. For example:
	 * 
	 * <code>&lt;meta name="name" value="userName" /&gt;</code>
	 * 
	 * @param id
	 *            the 'id' value of the desired meta tag
	 * @param defaultValue
	 *            the default value to return if the meta is not found
	 * @return the value of the attribute 'value' or null if not found
	 */
	@Nullable
	public static final String getMeta(final String id, @Nullable final String defaultValue) {
		checkNotNull(id);
		final NodeList<Element> elements = Document.get().getElementsByTagName("meta");
		for (int i = 0; i < elements.getLength(); i++) {
			final Element candidate = elements.getItem(i);
			if (id.equals(candidate.getAttribute("name")))
				return candidate.getPropertyString("content");
		}

		final Element domElement = DOM.getElementById(id);
		if (domElement != null)
			return domElement.getPropertyString("content");

		return defaultValue;
	}

	/**
	 * Return an int value for the meta
	 * 
	 * @param id
	 *            the 'id' value of the desired meta tag
	 * @param defaultValue
	 *            the default value to return if the meta is not found
	 * @return the int value of the meta tag, null if it is invalid or doesn't
	 *         exist
	 */
	public static final int getMeta(final String id, final int defaultValue) {
		final String metaValue = getMeta(id, null);

		if (metaValue == null)
			return defaultValue;

		try {
			return Integer.parseInt(metaValue);
		} catch (final NumberFormatException e) {
			return defaultValue;
		}
	}

	/**
	 * Returns the true/false value of a meta tag.
	 * 
	 * @param id
	 *            the 'id' value of the desired meta tag
	 * @param defaultValue
	 *            the value to return if the meta value is neither
	 *            <code>"true"</code> nor <code>"false"</code>
	 * @return true if the given meta is "true", false if the given meta is
	 *         "false" otherwise the defaultValue.
	 */
	public static final boolean getMeta(final String id, final boolean defaultValue) {
		final String metaValue = getMeta(id, null);

		if (metaValue == null)
			return defaultValue;

		return Boolean.parseBoolean(metaValue);
	}

	private PageAssist() {
	}
	
}
