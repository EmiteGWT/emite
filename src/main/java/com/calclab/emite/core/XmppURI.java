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

package com.calclab.emite.core;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.ConcurrentMap;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.collect.MapMaker;

/**
 * Defines a XMPP URI.
 * 
 * http://www.xmpp.org/drafts/attic/draft-saintandre-xmpp-uri-00.html
 * 
 * <code>XMPP- = ["xmpp:"] node "@" host[ "/" resource]</code>
 * 
 */
@Immutable
public final class XmppURI {
	
	// TODO: replace with Cache when available in GWT
	@SuppressWarnings("deprecation")
	private static final ConcurrentMap<String, XmppURI> cache = new MapMaker().makeComputingMap(new XmppURIParser());

	/**
	 * Parse the string and return a JID (a uri without resource)
	 * 
	 * @param jid
	 *            the string to be parsed
	 * @return a XmppURI object if the jid is a valid JID string, null otherwise
	 */
	public static XmppURI jid(final String jid) {
		return uri(jid).getJID();
	}

	/**
	 * Parse a string and return a complete XmppURI object
	 * 
	 * @param xmppUri
	 *            the string to be parsed
	 * @return a XmppURI object if the string is a valid XmppURI string, null
	 *         otherwise
	 */
	public static XmppURI uri(final String xmppUri) {
		try {
			return cache.get(xmppUri);
		} catch (final NullPointerException e) {
			return null;
		}
	}

	/**
	 * Create a new XmppURI object with the given attributes
	 * 
	 * @param node
	 *            the node of the URI
	 * @param host
	 *            the host of the URI
	 * @param resource
	 *            the resource of the URI
	 * @return a XmppURI object, never null
	 */
	public static XmppURI uri(final String node, final String host, final String resource) {
		XmppURI result = new XmppURI(node, host, resource);
		cache.putIfAbsent(result.toString(), result);
		return result;
	}

	private final String host;
	@Nullable private final String node;
	@Nullable private final String resource;

	protected XmppURI(final String node, final String host, final String resource) {
		this.host = checkNotNull(host);
		this.node = node;
		this.resource = resource;
	}

	/**
	 * Returns the JID of this URI (a XmppURI without resource)
	 * 
	 * @return the JID of this URI
	 */
	public final XmppURI getJID() {
		return uri(node, host, null);
	}

	/**
	 * @return the uri's host
	 */
	public final String getHost() {
		return host;
	}

	/**
	 * @return a new XmppURI object with the same host as this one
	 */
	public final XmppURI getHostURI() {
		return uri(null, host, null);
	}

	/**
	 * @return uri's node
	 */
	@Nullable
	public final String getNode() {
		return node;
	}

	/**
	 * @return uri's resource
	 */
	@Nullable
	public final String getResource() {
		return resource;
	}

	/**
	 * Return the node or the host if node is null
	 * 
	 * @return an never null short name representation
	 */
	public final String getShortName() {
		return node == null ? host : node;
	}

	@Override
	public final int hashCode() {
		return Objects.hashCode(host, node, resource);
	}

	@Override
	public final boolean equals(final Object obj) {
		if (obj instanceof XmppURI) {
			final XmppURI other = (XmppURI) obj;
			
			return Objects.equal(host, other.host) && Objects.equal(node, other.node) && Objects.equal(resource, other.resource);
		}
		return false;
	}

	public final boolean equalsNoResource(final XmppURI other) {
		return Objects.equal(host, other.host) && Objects.equal(node, other.node);
	}

	@Override
	public final String toString() {
		final StringBuilder builder = new StringBuilder();

		if (node != null) {
			builder.append(node);
			builder.append('@');
		}
		builder.append(this.host);
		if (resource != null) {
			builder.append('/');
			builder.append(resource);
		}

		return builder.toString();
	}
	
	/**
	 * A XmppURI parser and validator
	 */
	private static class XmppURIParser implements Function<String, XmppURI> {
		
		// TODO: Add Stringprep support.

		@Override
		public XmppURI apply(final String uri) {
			if (Strings.isNullOrEmpty(uri))
				return null;

			String node = null;
			String domain = null;
			String resource = null;

			final int atIndex = uri.indexOf('@') + 1;
			if (atIndex > 0) {
				node = uri.substring(0, atIndex - 1);
				if (node.length() == 0)
					return null;
			}

			final int barIndex = uri.indexOf('/', atIndex);
			if (atIndex == barIndex)
				return null;
			if (barIndex > 0) {
				domain = uri.substring(atIndex, barIndex);
				resource = uri.substring(barIndex + 1);
			} else {
				domain = uri.substring(atIndex);
			}
			if (domain.length() == 0)
				return null;

			return new XmppURI(node, domain, resource);
		}

	}
}
