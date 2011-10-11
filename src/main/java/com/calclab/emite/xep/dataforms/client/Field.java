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

import com.calclab.emite.core.client.xml.HasXML;
import com.calclab.emite.core.client.xml.XMLPacket;
import com.calclab.emite.core.client.xml.XMLUtils;

/**
 * XEP-0004 Field element : A data form of type "form", "submit", or "result"
 * SHOULD contain at least one <field/> element; a data form of type "cancel"
 * SHOULD NOT contain any <field/> elements.
 */

public class Field implements HasXML {

	private final XMLPacket xml;

	public Field() {
		this(XMLUtils.createPacket("field"));
	}

	public Field(final XMLPacket xml) {
		this.xml = xml;
	}

	public Field(final String type) {
		this();
		setType(type);
	}

	public void addOption(final Option option) {
		xml.addChild(option);
	}

	/**
	 * Fields of type list-multi, jid-multi, text-multi, and hidden MAY contain
	 * more than one <value/> and this field is only for the other types
	 */
	public void addValue(final String value) {
		xml.setChildText("value", value);
	}

	public String getDesc() {
		return xml.getChildText("desc");
	}

	public String getLabel() {
		return xml.getAttribute("label");
	}

	public List<Option> getOptions() {
		final List<Option> options = new ArrayList<Option>();
		for (final XMLPacket optionPacket : xml.getChildren("option")) {
			options.add(new Option(optionPacket));
		}
		return options;
	}

	public String getType() {
		return xml.getAttribute("type");
	}

	public List<String> getValues() {
		final List<String> values = new ArrayList<String>();
		for (final XMLPacket valuePacket : xml.getChildren("value")) {
			values.add(valuePacket.getText());
		}
		return values;
	}

	public String getVar() {
		return xml.getAttribute("var");
	}

	public boolean isRequired() {
		return xml.getFirstChild("required") != null;
	}

	public void setDesc(final String desc) {
		xml.setChildText("desc", desc);
	}

	/**
	 * The <field/> element MAY possess a 'label' attribute that defines a
	 * human-readable name for the field.
	 */

	public void setLabel(final String label) {
		xml.setAttribute("label", label);
	}

	public void setRequired(final boolean required) {
		xml.setChildText("required", required ? "" : null);
	}

	/**
	 * element SHOULD possess a 'type' attribute that defines the data "type" of
	 * the field data (if no 'type' is specified, the default is "text-single")
	 */
	public void setType(final String type) {
		xml.setAttribute("type", type);
	}

	public void setVar(final String var) {
		xml.setAttribute("var", var);
	}
	
	@Override
	public XMLPacket getXML() {
		return xml;
	}

}
