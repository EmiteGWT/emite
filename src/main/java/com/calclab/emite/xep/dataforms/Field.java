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

import com.calclab.emite.base.xml.HasXML;
import com.calclab.emite.base.xml.XMLBuilder;
import com.calclab.emite.base.xml.XMLPacket;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

/**
 * XEP-0004 Field element : A data form of type "form", "submit", or "result"
 * SHOULD contain at least one <field/> element; a data form of type "cancel"
 * SHOULD NOT contain any <field/> elements.
 */
public final class Field implements HasXML {

	public static enum Type {
		BOOLEAN, FIXED, HIDDEN, JID_MULTI, JID_SINGLE, LIST_MULTI, LIST_SINGLE, TEXT_MULTI, TEXT_PRIVATE, TEXT_SINGLE;
		
		public static final Type fromString(final String str) {
			if ("boolean".equals(str))
				return BOOLEAN;
			
			return Type.valueOf(str.replace('-', '_').toUpperCase());
		}
		
		@Override
		public final String toString() {
			return this.name().replace('_', '-').toLowerCase();
		}
	}
	
	@Nullable private Type type;
	@Nullable private String label;
	@Nullable private String var;
	
	@Nullable private String desc;
	private boolean required;
	
	private final List<String> values;
	private final List<Option> options;
	
	public Field() {
		values = Lists.newArrayList();
		options = Lists.newArrayList();
	}

	@Nullable
	public final Type getType() {
		return type;
	}
	
	public final void setType(@Nullable final Type type) {
		this.type = type;
	}
	
	@Nullable
	public final String getLabel() {
		return label;
	}
	
	public final void setLabel(@Nullable final String label) {
		this.label = label;
	}
	
	@Nullable
	public final String getVar() {
		return var;
	}
	
	public final void setVar(@Nullable String var) {
		this.var = var;
	}
	
	@Nullable
	public final String getDesc() {
		return desc;
	}
	
	public final void setDesc(@Nullable String desc) {
		this.desc = desc;
	}
	
	public final boolean isRequired() {
		return required;
	}
	
	public final void setRequired(boolean required) {
		this.required = required;
	}
	
	public final List<String> getValues() {
		return values;
	}
	
	public final void addValue(final String value) {
		values.add(checkNotNull(value));
	}
	
	public final List<Option> getOptions() {
		return options;
	}
	
	public final void addOption(final Option option) {
		options.add(checkNotNull(option));
	}

	@Override
	public final XMLPacket getXML() {
		final XMLBuilder builder = XMLBuilder.create("field");
		
		if (type != null) {
			builder.attribute("type", type.toString());
		}
		
		if (!Strings.isNullOrEmpty(label)) {
			builder.attribute("label", label);
		}
		
		if (!Strings.isNullOrEmpty(var)) {
			builder.attribute("var", var);
		}
		
		if (!Strings.isNullOrEmpty(desc)) {
			builder.childText("desc", desc);
		}
		
		if (required) {
			builder.child("required").parent();
		}
		
		for (final String value : values) {
			builder.childText("value", value);
		}
		
		for (final Option option : options) {
			builder.child(option);
		}
		
		return builder.getXML();
	}

}
