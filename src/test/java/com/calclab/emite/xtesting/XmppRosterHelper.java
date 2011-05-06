package com.calclab.emite.xtesting;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.xmpp.session.SessionStates;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.core.client.xmpp.stanzas.IQ.Type;
import com.calclab.emite.im.client.roster.RosterItem;

public class XmppRosterHelper {
	public static void setRosterItems(final XmppSessionTester session, final RosterItem... items) {
		session.setSessionState(SessionStates.loggedIn);
		final IQ iq = new IQ(Type.result);
		final IPacket query = iq.addQuery("jabber:iq:roster");
		for (final RosterItem item : items) {
			item.addStanzaTo(query);
		}
		session.answer(iq);
	}
}
