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

package com.calclab.emite.xep.delay;

import java.util.Date;

import javax.annotation.Nullable;

import com.calclab.emite.base.util.XmppDateTime;
import com.calclab.emite.base.xml.XMLPacket;
import com.calclab.emite.core.XmppNamespaces;
import com.calclab.emite.core.stanzas.Stanza;

/**
 * Helper methods for obtaining delay information from stanzas.
 */
public final class DelayHelper {

	/**
	 * Get delay stamp from stanza.
	 * 
	 * @param stanza
	 *            the stanza to get the delay from
	 * @return the date stamp if present, {@code null} otherwise
	 */
	@Nullable
	public static final Date getDelayStamp(final Stanza stanza) {
		XMLPacket x = stanza.getExtension("delay", XmppNamespaces.DELAY);
		if (x != null)
			return XmppDateTime.parseXMPPDateTime(x.getAttribute("stamp"));
		
		x = stanza.getExtension("x", XmppNamespaces.DELAY_LEGACY);
		if (x != null)
			return XmppDateTime.parseLegacyFormatXMPPDateTime(x.getAttribute("stamp"));
		
		return null;
	}
	
	private DelayHelper() {
	}

}
