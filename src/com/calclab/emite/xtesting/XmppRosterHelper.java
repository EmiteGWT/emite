package com.calclab.emite.xtesting;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.xmpp.session.XmppSession.SessionStates;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.core.client.xmpp.stanzas.IQ.Type;
import com.calclab.emite.im.client.roster.RosterItem;

public class XmppRosterHelper {
    public static void setRosterItems(XmppSessionTester session, RosterItem... items) {
	session.setSessionState(SessionStates.loggedIn);
	IQ iq = new IQ(Type.result);
	IPacket query = iq.addQuery("jabber:iq:roster");
	for (RosterItem item : items) {
	    item.addStanzaTo(query);
	}
	session.answer(iq);
    }
}
