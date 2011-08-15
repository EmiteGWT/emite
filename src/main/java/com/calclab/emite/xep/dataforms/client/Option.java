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

import com.calclab.emite.core.client.packet.DelegatedPacket;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.Packet;

/**
 * A XEP-0004 field option
 * 
 */
public class Option extends DelegatedPacket {

	public static final String OPTION = "option";
	private static final String LABEL = "label";
	private static final String VALUE = "value";

	public Option() {
		super(new Packet(OPTION));
	}

	public Option(final IPacket stanza) {
		super(stanza);
	}

	public String getLabel() {
		return super.getAttribute(LABEL);
	}

	public String getValue() {
		return super.getFirstChild(VALUE).getText();
	}

	public void setLabel(final String label) {
		super.setAttribute(LABEL, label);
	}

	public void setValue(final String value) {
		setTextToChild(VALUE, value);
	}
}
