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

package com.calclab.emite.xep.dataforms;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import javax.annotation.Nullable;

import com.calclab.emite.base.xml.XMLBuilder;
import com.google.common.collect.Lists;

/**
 * A XEP-0004 field option
 */
public final class Option {

	@Nullable private String label;
	private final List<String> values;

	public Option() {
		values = Lists.newArrayList();
	}
	
	@Nullable
	public final String getLabel() {
		return label;
	}
	
	public final void setLabel(@Nullable final String label) {
		this.label = label;
	}
	
	public final List<String> getValues() {
		return values;
	}
	
	public final void addValue(final String value) {
		values.add(checkNotNull(value));
	}

	protected final void build(final XMLBuilder builder) {
		builder.child("option");
		
		builder.attribute("label", label);
		for (final String value : values) {
			builder.childText("value", value);
		}
		
		builder.parent();
	}
}
