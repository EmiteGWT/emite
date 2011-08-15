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

package com.calclab.emite.xep.dataforms.client;

/**
 * XEP-0004 Field type constants (a enum it's not a good idea because the
 * boolean type, and the hyphen (-) in the types)
 * 
 */
public final class FieldType {

	/**
	 * The field enables an entity to gather or provide an either-or choice
	 * between two options. The default value is "false".
	 */
	public static final String BOOLEAN = "boolean";

	/**
	 * The field is intended for data description (e.g., human-readable text
	 * such as "section" headers) rather than data gathering or provision. The
	 * <value/> child SHOULD NOT contain newlines (the \n and \r characters);
	 * instead an application SHOULD generate multiple fixed fields, each with
	 * one <value/> child.
	 */
	public static final String FIXED = "fixed";

	/**
	 * The field is not shown to the form-submitting entity, but instead is
	 * returned with the form. The form-submitting entity SHOULD NOT modify the
	 * value of a hidden field, but MAY do so if such behavior is defined for
	 * the "using protocol".
	 */
	public static final String HIDDEN = "hidden";

	/**
	 * The field enables an entity to gather or provide multiple Jabber IDs.
	 * Each provided JID SHOULD be unique (as determined by comparison that
	 * includes application of the Nodeprep, Nameprep, and Resourceprep profiles
	 * of Stringprep as specified in XMPP Core), and duplicate JIDs MUST be
	 * ignored.
	 */
	public static final String JID_MULTI = "jid-multi";

	/** The field enables an entity to gather or provide a single Jabber ID. */
	public static final String JID_SINGLE = "jid-single";

	/**
	 * The field enables an entity to gather or provide one or more options from
	 * among many. A form-submitting entity chooses one or more items from among
	 * the options presented by the form-processing entity and MUST NOT insert
	 * new options. The form-submitting entity MUST NOT modify the order of
	 * items as received from the form-processing entity, since the order of
	 * items MAY be significant.
	 */
	public static final String LIST_MULTI = "list-multi";

	/**
	 * The field enables an entity to gather or provide one option from among
	 * many. A form-submitting entity chooses one item from among the options
	 * presented by the form-processing entity and MUST NOT insert new options.
	 */
	public static final String LIST_SINGLE = "list-single";

	/**
	 * The field enables an entity to gather or provide multiple lines of text.
	 */
	public static final String TEXT_MULTI = "text-multi";

	/**
	 * The field enables an entity to gather or provide a single line or word of
	 * text, which shall be obscured in an interface (e.g., with multiple
	 * instances of the asterisk character).
	 */
	public static final String TEXT_PRIVATE = "text-private";

	/**
	 * The field enables an entity to gather or provide a single line or word of
	 * text, which may be shown in an interface. This field type is the default
	 * and MUST be assumed if a form-submitting entity receives a field type it
	 * does not understand.
	 */
	public static final String TEXT_SINGLE = "text-single";

	FieldType() {
		// Final...
	}
}
