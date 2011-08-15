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

import java.util.ArrayList;
import java.util.List;

import com.calclab.emite.core.client.packet.DelegatedPacket;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.MatcherFactory;
import com.calclab.emite.core.client.packet.NoPacket;
import com.calclab.emite.core.client.packet.Packet;

/**
 * 
 * XEP-0004 Field element : A data form of type "form", "submit", or "result"
 * SHOULD contain at least one <field/> element; a data form of type "cancel"
 * SHOULD NOT contain any <field/> elements.
 * 
 */

public class Field extends DelegatedPacket {

	public static final String FIELD = "field";
	private static final String LABEL = "label";
	private static final String DESC = "desc";
	private static final String VALUE = "value";
	private static final String TYPE = "type";
	private static final String VAR = "var";
	private static final String REQUIRED = "required";

	public static List<Field> parseFields(final List<Field> fields, final IPacket packet) {
		if (fields == null) {
			final List<Field> fieldsTmp = new ArrayList<Field>();
			for (final IPacket fieldPacket : packet.getChildren(MatcherFactory.byName(Field.FIELD))) {
				fieldsTmp.add(new Field(fieldPacket));
			}
			return fieldsTmp;
		}
		return fields;
	}

	private List<Option> options;
	private List<String> values;

	public Field() {
		this(new Packet(FIELD));
	}

	public Field(final IPacket stanza) {
		super(stanza);
	}

	public Field(final String type) {
		this();
		setType(type);
	}

	public void addOption(final Option option) {
		parseOptions();
		options.add(option);
		addChild(option);
	}

	/**
	 * Fields of type list-multi, jid-multi, text-multi, and hidden MAY contain
	 * more than one <value/> and this field is only for the other types
	 */
	public void addValue(final String value) {
		parseValues();
		values.add(value);
		addChild(VALUE).setText(value);
	}

	public String getDesc() {
		return getFirstChild(DESC).getText();
	}

	public String getLabel() {
		return getAttribute(LABEL);
	}

	public List<Option> getOptions() {
		parseOptions();
		return options;
	}

	public String getType() {
		return getAttribute(TYPE);
	}

	public List<String> getValues() {
		parseValues();
		return values;
	}

	public String getVar() {
		return super.getAttribute(VAR);
	}

	public boolean isRequired() {
		return super.getFirstChild(REQUIRED) != NoPacket.INSTANCE;
	}

	public Field Required(final boolean required) {
		setRequired(required);
		return this;
	}

	public void setDesc(final String desc) {
		super.setTextToChild(DESC, desc);
	}

	/**
	 * The <field/> element MAY possess a 'label' attribute that defines a
	 * human-readable name for the field.
	 */

	public void setLabel(final String label) {
		super.setAttribute(LABEL, label);
	}

	public void setRequired(final boolean required) {
		super.setTextToChild(REQUIRED, required ? "" : null);
	}

	/**
	 * element SHOULD possess a 'type' attribute that defines the data "type" of
	 * the field data (if no 'type' is specified, the default is "text-single")
	 */
	public void setType(final String type) {
		super.setAttribute(TYPE, type);
	}

	public void setVar(final String var) {
		super.setAttribute(VAR, var);
	}

	public Field Value(final String value) {
		addValue(value);
		return this;
	}

	public Field Var(final String var) {
		setVar(var);
		return this;
	}

	private void parseOptions() {
		if (options == null) {
			options = new ArrayList<Option>();
			for (final IPacket optionPacket : super.getChildren(MatcherFactory.byName(Option.OPTION))) {
				options.add(new Option(optionPacket));
			}
		}
	}

	private void parseValues() {
		if (values == null) {
			values = new ArrayList<String>();
			for (final IPacket valuePacket : super.getChildren(MatcherFactory.byName(VALUE))) {
				values.add(valuePacket.getText());
			}
		}
	}

}
