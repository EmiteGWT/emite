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

import java.util.List;

import com.calclab.emite.core.client.packet.DelegatedPacket;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.Packet;

/**
 * 
 * XEP-0004 Item and Results abstract class for
 * "3.2 Multiple Items in Form Results"
 * 
 */
public class AbstractItem extends DelegatedPacket {

	/**
	 * Each of these elements MUST contain one or more <field/> children.
	 */
	private List<Field> fields;

	public AbstractItem(final IPacket delegate) {
		super(delegate);
	}

	public AbstractItem(final String name) {
		this(new Packet(name));
	}

	public void addField(final Field field) {
		parseFields();
		super.addChild(field);
		fields.add(field);
	}

	public List<Field> getFields() {
		parseFields();
		return fields;
	}

	private void parseFields() {
		fields = Field.parseFields(fields, this);
	}
}
