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

import com.calclab.emite.base.xml.HasXML;
import com.calclab.emite.base.xml.XMLBuilder;
import com.calclab.emite.base.xml.XMLPacket;
import com.google.common.collect.Lists;

/**
 * XEP-0004 Reported element for "3.2 Multiple Items in Form Results", which can
 * be understood as a "table header" describing the data to follow. The
 * <reported/> element defines the data format for the result items by
 * specifying the fields to be expected for each item; for this reason, the
 * <field/> elements SHOULD possess a 'type' attribute and 'label' attribute in
 * addition to the 'var' attribute, and SHOULD NOT contain a <value/> element.
 */
public class Reported implements HasXML {

	private final List<Field> fields;
	
	public Reported() {
		fields = Lists.newArrayList();
	}
	
	public final List<Field> getFields() {
		return fields;
	}
	
	public final void addField(final Field field) {
		fields.add(checkNotNull(field));
	}
	
	@Override
	public final XMLPacket getXML() {
		XMLBuilder builder = XMLBuilder.create("reported");
		
		for (final Field field : fields) {
			builder.child(field);
		}
		
		return builder.getXML();
	}
	
}