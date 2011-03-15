package com.calclab.emite.im.client.roster;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.events.ChangedEvent.ChangeTypes;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.core.client.xmpp.stanzas.Presence.Type;
import com.calclab.emite.im.client.roster.events.RosterItemChangedEvent;
import com.calclab.emite.xtesting.XmppSessionTester;
import com.calclab.suco.testing.events.MockedListener2;

public class SubscriptionManagerTests {

    private XmppSessionTester session;
    private SubscriptionManager manager;
    private XmppRoster roster;
    private EmiteEventBus eventBus;

    @Before
    public void beforeTests() {
	session = new XmppSessionTester();
	eventBus = session.getEventBus();
	roster = mock(XmppRoster.class);
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
    public void shouldSendSubscriptionRequestOnNewRosterItem_addRosterStep1() {

	// only NONE subscription
	RosterItem item = new RosterItem(uri("name@domain"), SubscriptionState.both, "TheName", null);
	eventBus.fireEvent(new RosterItemChangedEvent(ChangeTypes.added, item));
	session.verifyNotSent("<presence />");

	RosterItem item2 = new RosterItem(uri("name@domain"), SubscriptionState.none, "TheName", Type.subscribe);
	eventBus.fireEvent(new RosterItemChangedEvent(ChangeTypes.added, item2));
	session.verifySent("<presence from='user@local' to='name@domain' type='subscribe'/>");
    }

    @Test
    public void shouldUnsubscribe() {
	manager.unsubscribe(uri("friend@domain"));
	session.verifySent("<presence from='user@local' to='friend@domain' type='unsubscribe' />");
    }
}
