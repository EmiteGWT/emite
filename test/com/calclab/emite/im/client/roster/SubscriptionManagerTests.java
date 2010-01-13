package com.calclab.emite.im.client.roster;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static com.calclab.suco.testing.events.Eventito.anyListener;
import static com.calclab.suco.testing.events.Eventito.fire;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.core.client.xmpp.stanzas.Presence.Type;
import com.calclab.emite.xtesting.SessionTester;
import com.calclab.suco.testing.events.MockedListener2;

public class SubscriptionManagerTests {

    private SessionTester session;
    private SubscriptionManager manager;
    private Roster roster;

    @SuppressWarnings("unchecked")
    @Test
    public void addRosterStep1_shouldSendSubscriptionRequestOnNewRosterItem() {

	// only NONE subscription
	fire(new RosterItem(uri("name@domain"), SubscriptionState.both, "TheName", null)).when(roster).onItemAdded(
		anyListener());
	session.verifyNotSent("<presence />");

	fire(new RosterItem(uri("name@domain"), SubscriptionState.none, "TheName", Type.subscribe)).when(roster)
		.onItemAdded(anyListener());
	session.verifySent("<presence from='user@local' to='name@domain' type='subscribe'/>");
    }

    @Before
    public void beforeTests() {
	session = new SessionTester();
	roster = mock(Roster.class);
	manager = new SubscriptionManagerImpl(session, roster);
	session.login(uri("user@local"), "anything");
    }

    @Test
    public void shouldApproveSubscriptionRequestsAndAddItemToTheRosterIfNotThere() {
	final XmppURI otherEntityJID = XmppURI.jid("other@domain");
	when(roster.getItemByJID(eq(otherEntityJID))).thenReturn(null);

	manager.approveSubscriptionRequest(otherEntityJID, "nick");
	verify(roster).requestAddItem(eq(otherEntityJID), eq("nick"));
	session.verifySent("<presence type='subscribed' to='other@domain' />");
	session.verifySent("<presence type='subscribe' to='other@domain' />");
    }

    @Test
    public void shouldCancelSubscription() {
	manager.cancelSubscription(uri("friend@domain"));
	session.verifySent("<presence from='user@local' to='friend@domain' type='unsubscribed' />");
    }

    @Test
    public void shouldFireSubscriptionRequests() {
	final MockedListener2<XmppURI, String> listener = new MockedListener2<XmppURI, String>();
	manager.onSubscriptionRequested(listener);
	session.receives("<presence to='user@local' from='friend@domain' type='subscribe' />");
	assertEquals(1, listener.getCalledTimes());
    }

    @Test
    public void shouldSendSubscriptionRequest() {
	manager.requestSubscribe(uri("name@domain/RESOURCE"));
	session.verifySent("<presence from='user@local' to='name@domain' type='subscribe'/>");
    }

    @Test
    public void shouldUnsubscribe() {
	manager.unsubscribe(uri("friend@domain"));
	session.verifySent("<presence from='user@local' to='friend@domain' type='unsubscribe' />");
    }
}
