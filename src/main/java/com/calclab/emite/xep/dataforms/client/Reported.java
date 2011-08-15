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

import com.calclab.emite.core.client.packet.IPacket;

/**
 * 
 * XEP-0004 Reported element for "3.2 Multiple Items in Form Results", which can
 * be understood as a "table header" describing the data to follow. The
 * <reported/> element defines the data format for the result items by
 * specifying the fields to be expected for each item; for this reason, the
 * <field/> elements SHOULD possess a 'type' attribute and 'label' attribute in
 * addition to the 'var' attribute, and SHOULD NOT contain a <value/> element.
 * 
 */
public class Reported extends AbstractItem {

	static final String REPORTED = "reported";

	public Reported() {
		super(REPORTED);
	}

	public Reported(final IPacket packet) {
		super(packet);
	}

}
