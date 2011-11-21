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

import com.google.common.base.Objects;
import com.google.common.collect.MapMaker;

/**
 * Defines a XMPP URI.
 * 
 * <code>[ node "@" ] domain [ "/" resource ]</code>
 * 
 * @see <a href="http://xmpp.org/rfcs/rfc6122.html">RFC 6122</a>
 */
@Immutable
public final class XmppURI {
	
	// TODO: replace with Cache when available in GWT
	@SuppressWarnings("deprecation")
	private static final ConcurrentMap<String, XmppURI> cache = new MapMaker().makeComputingMap(XmppURIParser.INSTANCE);

	/**
	 * Parse a string and return a JID (the URI without resource).
	 * 
	 * @param jid
	 *            the string to be parsed
	 * @return a JID if it is a valid JID string, {@code null} otherwise
	 */
	@Nullable
	public static final XmppURI jid(final String jid) {
		final XmppURI uri = uri(jid);
		return uri != null ? uri : null;
	}

	/**
	 * Parse a string and return a URI.
	 * 
	 * @param xmppUri
	 *            the string to be parsed
	 * @return a URI if it is a valid URI string, {@code null} otherwise
	 */
	@Nullable
	public static final XmppURI uri(final String xmppUri) {
		try {
			return cache.get(xmppUri);
		} catch (final NullPointerException e) {
			return null;
		}
	}

	/**
	 * Create a new URI object with the given attributes.
	 * 
	 * @param node
	 *            the node of the URI
	 * @param host
	 *            the host of the URI
	 * @param resource
	 *            the resource of the URI
	 * @return a URI object
	 */
	public static final XmppURI uri(@Nullable final String node, final String host, @Nullable final String resource) {
		final XmppURI result = new XmppURI(node, host, resource);
		cache.putIfAbsent(result.toString(), result);
		return result;
	}

	private final String host;
	@Nullable private final String node;
	@Nullable private final String resource;

	protected XmppURI(@Nullable final String node, final String host, @Nullable final String resource) {
		this.host = checkNotNull(host);
		this.node = node;
		this.resource = resource;
	}

	/**
	 * Returns the JID for this URI (the URI without resource).
	 * 
	 * @return the JID for this URI
	 */
	public final XmppURI getJID() {
		return uri(node, host, null);
	}

	/**
	 * Returns the host for this URI.
	 * 
	 * @return the host for this URI
	 */
	public final String getHost() {
		return host;
	}

	/**
	 * A new URI with the host part only.
	 * 
	 * @return a new URI with the same host as this one
	 */
	public final XmppURI getHostURI() {
		return uri(null, host, null);
	}

	/**
	 * Returns the node for this URI.
	 * 
	 * @return the node for this URI
	 */
	@Nullable
	public final String getNode() {
		return node;
	}

	/**
	 * Returns the resource for this URI.
	 * 
	 * @return the resource for this URI
	 */
	@Nullable
	public final String getResource() {
		return resource;
	}

	/**
	 * Return the node or the host if node is {@code null}.
	 * 
	 * @return a short name representation
	 */
	public final String getShortName() {
		return node != null ? node : host;
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

	/**
	 * Compares two JIDs, ignoring the resource.
	 * 
	 * @param other the URI to compare to this one
	 * @return {@code true} if both JIDs are equal, {@code false} otherwise
	 */
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
		builder.append(host);
		if (resource != null) {
			builder.append('/');
			builder.append(resource);
		}

		return builder.toString();
	}
	
}
