package com.calclab.emite.xep.muc.client;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RoomOccupantTest {

    @Test
    public void should() {
	final Occupant occupant = new Occupant(uri("valid@uri"), "not valid affiliation", "not valid role");
	assertEquals(Occupant.Affiliation.none, occupant.getAffiliation());
	assertEquals(Occupant.Role.unknown, occupant.getRole());
    }
}
