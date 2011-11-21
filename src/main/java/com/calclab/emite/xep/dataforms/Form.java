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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import javax.annotation.Nullable;

import com.calclab.emite.base.xml.HasXML;
import com.calclab.emite.base.xml.XMLBuilder;
import com.calclab.emite.base.xml.XMLPacket;
import com.calclab.emite.core.XmppNamespaces;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

/**
 * XEP-0004 Form
 */
public final class Form implements HasXML {
	
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
	
	private Type type;
	@Nullable private String title;
	@Nullable private Reported reported;
	
	private final List<String> instructions;
	private final List<Item> items;
	private final List<Field> fields;
	
	public Form(final Type type) {
		this.type = checkNotNull(type);
		
		instructions = Lists.newArrayList();
		items = Lists.newArrayList();
		fields = Lists.newArrayList();
	}
	
	public final Type getType() {
		return type;
	}
	
	public final void setType(final Type type) {
		this.type = checkNotNull(type);
	}
	
	@Nullable
	public final String getTitle() {
		return title;
	}
	
	public final void setTitle(@Nullable final String title) {
		this.title = title;
	}
	
	@Nullable
	public final Reported getReported() {
		return reported;
	}
	
	public final void setReported(@Nullable final Reported reported) {
		this.reported = reported;
	}
	
	public final List<String> getInstructions() {
		return instructions;
	}
	
	public final void addInstruction(final String instruction) {
		instructions.add(checkNotNull(instruction));
	}
	
	public final List<Item> getItems() {
		return items;
	}
	
	public final void addItem(final Item item) {
		items.add(checkNotNull(item));
	}
	
	public final List<Field> getFields() {
		return fields;
	}
	
	public final void addField(final Field field) {
		fields.add(checkNotNull(field));
	}
	
	@Override
	public final XMLPacket getXML() {
		final XMLBuilder builder = XMLBuilder.create("x", XmppNamespaces.DATA).attribute("type", type.toString());
		
		if (!Strings.isNullOrEmpty(title)) {
			builder.childText("title", title);
		}
		
		for (final String instruction : instructions) {
			builder.child("instructions").text(instruction);
		}
		
		if (reported != null) {
			reported.build(builder);
		}
		
		for (final Item item : items) {
			item.build(builder);
		}
		
		for (final Field field : fields) {
			field.build(builder);
		}
		
		return builder.getXML();
	}
	
	public static final Form fromXML(final XMLPacket xml) {
		checkArgument("x".equals(xml.getTagName()) && XmppNamespaces.DATA.equals(xml.getNamespace()));
		
		final Form form = new Form(Type.valueOf(xml.getAttribute("type")));
		form.setTitle(xml.getChildText("title"));
		
		for (final XMLPacket xmlInstructions : xml.getChildren("instructions")) {
			form.addInstruction(xmlInstructions.getText());
		}
		
		if (xml.hasChild("reported")) {
			final XMLPacket xmlReported = xml.getFirstChild("reported");
			
			final Reported reported = new Reported();
			for (final XMLPacket xmlField : xmlReported.getChildren("field")) {
				reported.addField(parseField(xmlField));
			}
		}
		
		if (xml.hasChild("item")) {
			final XMLPacket xmlItem = xml.getFirstChild("item");
			
			final Item item = new Item();
			for (final XMLPacket xmlField : xmlItem.getChildren("field")) {
				item.addField(parseField(xmlField));
			}
		}
		
		for (final XMLPacket xmlField : xml.getChildren("field")) {
			form.addField(parseField(xmlField));
		}
		
		return null;
	}
	
	private static final Field parseField(final XMLPacket xml) {
		final Field field = new Field();
		
		if (xml.hasAttribute("type")) {
			field.setType(Field.Type.fromString(xml.getAttribute("type")));
		}
		
		field.setLabel(xml.getAttribute("label"));
		field.setVar(xml.getAttribute("var"));
		field.setDesc(xml.getChildText("desc"));
		field.setRequired(xml.hasChild("required"));
		
		for (final XMLPacket xmlValue : xml.getChildren("value")) {
			field.addValue(xmlValue.getText());
		}
		
		for (final XMLPacket xmlOption : xml.getChildren("option")) {
			final Option option = new Option();
			option.setLabel(xmlOption.getAttribute("label"));
			for (final XMLPacket xmlValue : xml.getChildren("value")) {
				option.addValue(xmlValue.getText());
			}
			field.addOption(option);
		}
		
		return field;
	}

}
