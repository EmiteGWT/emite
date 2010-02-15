package com.calclab.emite.im.client.roster;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.Packet;
import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.emite.xtesting.matchers.EmiteAsserts;
import com.calclab.emite.xtesting.services.TigaseXMLService;

public class RosterItemTests {

    @Test
    public void shouldConvertToStanza() {
	final RosterItem item = new RosterItem(uri("name@domain/RESOURCE"), null, "TheName", null);
	item.addToGroup("group1");
	item.addToGroup("group2");
	EmiteAsserts.assertPacketLike("<item jid='name@domain' name='TheName'>"
		+ "<group>group1</group><group>group2</group></item>", item.addStanzaTo(new Packet("all")));
    }

    @Test
    public void shouldParseStanza() {
	final RosterItem item = RosterItem
		.parse(p("<item jid='romeo@example.net' ask='subscribe' name='R' subscription='both'>"
			+ "<group>Friends</group><group>X</group></item>"));
	assertEquals("R", item.getName());
	assertEquals("R", item.getName());
	assertEquals(Presence.Type.subscribe, item.getAsk());
	assertEquals(2, item.getGroups().size());
	assertTrue(item.getGroups().contains("Friends"));
	assertTrue(item.getGroups().contains("X"));
    }

    private IPacket p(final String xml) {
	final IPacket packet = TigaseXMLService.toPacket(xml);
	return packet;
    }
}
