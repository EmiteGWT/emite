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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.calclab.emite.base.xml.XMLBuilder;
import com.calclab.emite.xep.dataforms.Field;
import com.calclab.emite.xep.dataforms.FieldType;
import com.calclab.emite.xep.dataforms.Option;

public class FieldTest {

	@Test
	public void testBasicFieldsParsing() {
		final Field field = new Field(XMLBuilder.fromXML("<field type='jid-multi' label='People to invite'"
				+ "var='invitelist'><desc>Tell all your friends about your new bot!</desc></field>"));
		assertEquals(FieldType.JID_MULTI, field.getType());
		assertEquals("People to invite", field.getLabel());
		assertEquals("invitelist", field.getVar());
		assertEquals("Tell all your friends about your new bot!", field.getDesc());
	}

	@Test
	public void testOptionsParsing() {
		final Field field = new Field(XMLBuilder.fromXML("<field type='list-single'" + " label='Maximum number of subscribers' var='maxsubs'>"
				+ " <value>20</value> <option label='10'><value>10</value></option>"
				+ " <option label='20'><value>20</value></option> <option label='30'><value>30</value></option>"
				+ " <option label='50'><value>50</value></option> <option label='100'><value>100</value></option>"
				+ " <option label='None'><value>none</value></option></field>"));
		assertEquals(FieldType.LIST_SINGLE, field.getType());
		assertEquals("Maximum number of subscribers", field.getLabel());
		assertEquals("maxsubs", field.getVar());
		assertEquals(null, field.getDesc());
		final List<String> values = field.getValues();
		assertEquals(1, values.size());
		assertEquals("20", values.get(0));
		final List<Option> options = field.getOptions();
		assertEquals(6, options.size());
		assertEquals("None", options.get(5).getLabel());
		assertEquals("none", options.get(5).getValue());
	}

	@Test
	public void testRequired() {
		final Field field = new Field(FieldType.FIXED);
		field.setRequired(true);
		assertTrue(field.isRequired());
		field.setRequired(false);
		assertFalse(field.isRequired());
	}

}
