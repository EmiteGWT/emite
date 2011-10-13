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
import com.calclab.emite.core.client.xml.XMLBuilder;
import com.calclab.emite.core.client.xml.XMLPacket;

/**
 * XEP-0004 Item and Results abstract class for
 * "3.2 Multiple Items in Form Results"
 */
public abstract class AbstractItem implements HasXML {

	protected final XMLPacket xml;
	
	public AbstractItem(final String name) {
		xml = XMLBuilder.create(name).getXML();
	}

	public AbstractItem(final XMLPacket xml) {
		this.xml = xml;
	}

	public void addField(final Field field) {
		xml.addChild(field);
	}

	public List<Field> getFields() {
		final List<Field> fields = new ArrayList<Field>();
		for (final XMLPacket fieldPacket : xml.getChildren("field")) {
			fields.add(new Field(fieldPacket));
		}
		return fields;
	}

	@Override
	public XMLPacket getXML() {
		return xml;
	}

}
