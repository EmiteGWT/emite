package com.calclab.emite.im.client.presence;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.calclab.emite.core.client.xmpp.session.SessionReady;
import com.calclab.emite.core.client.xmpp.session.XmppSession.SessionStates;
import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.emite.core.client.xmpp.stanzas.Presence.Show;
import com.calclab.emite.core.client.xmpp.stanzas.Presence.Type;
import com.calclab.emite.xtesting.XmppSessionTester;
import com.calclab.suco.testing.events.MockedListener;

public class PresenceManagerTest {

    private PresenceManager manager;
    private XmppSessionTester session;

    @Before
    public void beforeTest() {
	session = new XmppSessionTester();
	SessionReady sessionReady = Mockito.mock(SessionReady.class);
	manager = new PresenceManagerImpl(session, sessionReady);
    }

    @Test
    public void shouldBroadcastPresenceIfLoggedin() {
	session.setLoggedIn("myself@domain");
	manager.changeOwnPresence(Presence.build("this is my new status", Show.away));
	session.verifySent("<presence><show>away</show>" + "<status>this is my new status</status></presence>");
	final Presence current = manager.getOwnPresence();
	assertEquals(Show.away, current.getShow());
	assertEquals("this is my new status", current.getStatus());
    }

    @Test
    public void shouldEventOwnPresence() {
	session.setLoggedIn(uri("myself@domain"));
	final MockedListener<Presence> listener = new MockedListener<Presence>();
	manager.onOwnPresenceChanged(listener);
	manager.changeOwnPresence(Presence.build("status", Show.away));
	assertTrue(listener.isCalledOnce());
	assertEquals("status", listener.getValue(0).getStatus());
	assertEquals(Show.away, listener.getValue(0).getShow());
    }

    @Test
    public void shouldHavePresenceEvenLoggedOut() {
	assertNotNull(manager.getOwnPresence());
    }

    @Test
    public void shouldResetOwnPresenceWhenLoggedOut() {
	session.setLoggedIn(uri("myself@domain"));
	manager.changeOwnPresence(Presence.build("status", Show.away));
	assertEquals("status", manager.getOwnPresence().getStatus());
	session.logout();
	assertEquals(Type.unavailable, manager.getOwnPresence().getType());
    }

    @Test
    public void shouldSendFinalPresence() {
	session.setLoggedIn(uri("myself@domain"));
	session.logout();
	session.verifySent("<presence from='myself@domain' type='unavailable' />");
    }

    @Test
    public void shouldSendInitialPresenceAfterRosterReady() {
	session.setLoggedIn("myself@domain");
	session.setSessionState(SessionStates.rosterReady);
	session.verifySent("<presence from='myself@domain'></presence>");
    }

    @Test
    public void shouldSendPresenceIfLoggedIn() {
	session.setLoggedIn(uri("myself@domain"));
	manager.changeOwnPresence(new Presence().With(Presence.Show.dnd));
	session.verifySent("<presence><show>dnd</show></presence>");

    }

}
