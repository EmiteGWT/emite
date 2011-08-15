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

package com.calclab.emite.xep.vcard.client;

import static com.calclab.emite.core.client.packet.MatcherFactory.byName;

import com.calclab.emite.core.client.packet.DelegatedPacket;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.NoPacket;

public abstract class VCardData extends DelegatedPacket {

	public VCardData(final IPacket packet) {
		super(packet);
	}

	public String getValue(final String nodeName) {
		return getOrCreateChild(nodeName).getText();
	}

	public void setValue(final String nodeName, final String text) {
		setTextToChild(nodeName, text);
	}

	protected IPacket getOrCreateChild(final String nodeName) {
		IPacket firstChild = getFirstChild(byName(nodeName));
		if (firstChild == NoPacket.INSTANCE) {
			firstChild = addChild(nodeName);
		}
		return firstChild;
	}

}
