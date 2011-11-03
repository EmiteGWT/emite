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

import java.util.ArrayList;
import java.util.List;

import com.calclab.emite.base.xml.HasXML;
import com.calclab.emite.base.xml.XMLBuilder;
import com.calclab.emite.base.xml.XMLPacket;

/**
 * 
 * XEP-0004 Form
 * 
 */
public class Form implements HasXML {

	public static enum Type {
		/**
		 * The form-processing entity is asking the form-submitting entity to
		 * complete a form.
		 */
		form,
		/**
		 * The form-submitting entity is submitting data to the form-processing
		 * entity. The submission MAY include fields that were not provided in
		 * the empty form, but the form-processing entity MUST ignore any fields
		 * that it does not understand.
		 */
		submit,
		/**
		 * The form-submitting entity has cancelled submission of data to the
		 * form-processing entity.
		 */
		cancel,
		/**
		 * The form-processing entity is returning data (e.g., search results)
		 * to the form-submitting entity, or the data is a generic data set.
		 */
		result
	}

	private final XMLPacket xml;

	public Form(final XMLPacket xml) {
		this.xml = xml;
	}

	public Form(final Type type) {
		xml = XMLBuilder.create("x", "jabber:x:data").getXML();
		setType(type);
	}

	public String getTitle() {
		return xml.getChildText("title");
	}

	public void setTitle(final String title) {
		xml.setChildText("title", title);
	}

	public List<String> getInstructions() {
		final List<String> instructions = new ArrayList<String>();
		for (final XMLPacket instruction : xml.getChildren("instructions")) {
			instructions.add(instruction.getText());
		}
		return instructions;
	}

	public void addInstruction(final String instruction) {
		xml.addChild("instructions").setText(instruction);
	}

	public List<Field> getFields() {
		final List<Field> fields = new ArrayList<Field>();
		for (final XMLPacket fieldPacket : xml.getChildren("field")) {
			fields.add(new Field(fieldPacket));
		}
		return fields;
	}

	public void addField(final Field field) {
		xml.addChild(field);
	}

	public List<Item> getItems() {
		final List<Item> items = new ArrayList<Item>();
		for (final XMLPacket itemPacket : xml.getChildren("item")) {
			items.add(new Item(itemPacket));
		}
		return items;
	}

	public void addItem(final Item item) {
		xml.addChild(item);
	}

	/**
	 * In some contexts (e.g., the results of a search request), it may be
	 * necessary to communicate multiple items. Therefore, a data form of type
	 * "result" MAY contain two child elements not described in the basic syntax
	 * above: 1. One and only <reported/> element, which can be understood as a
	 * "table header" describing the data to follow. 2. Zero or more <item/>
	 * elements, which can be understood as "table cells" containing data (if
	 * any) that matches the request.
	 */
	public Reported getReported() {
		return new Reported(xml.getFirstChild("reported"));
	}

	public void addReported(final Field field) {
		XMLPacket reportedPacket = xml.getFirstChild("reported");
		if (reportedPacket == null) {
			reportedPacket = xml.addChild("reported");
		}
		reportedPacket.addChild(field);
	}

	public Type getType() {
		return Type.valueOf(xml.getAttribute("type"));
	}

	public void setType(final Type type) {
		xml.setAttribute("type", type.toString());
	}

	@Override
	public XMLPacket getXML() {
		return xml;
	}

}
