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

package com.calclab.emite.xtesting;

import com.calclab.emite.core.client.session.SessionStatus;
import com.calclab.emite.core.client.stanzas.IQ;
import com.calclab.emite.core.client.xml.XMLPacket;
import com.calclab.emite.im.client.roster.RosterItem;

public class XmppRosterHelper {
	public static void setRosterItems(final XmppSessionTester session, final RosterItem... items) {
		session.setStatus(SessionStatus.loggedIn);
		final IQ iq = new IQ(IQ.Type.result);
		final XMLPacket query = iq.addChild("query", "jabber:iq:roster");
		for (final RosterItem item : items) {
			item.addStanzaTo(query);
		}
		session.answerSuccess(iq);
	}
}
