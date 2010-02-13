package com.calclab.emite.xep.storage.client;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.xtesting.SessionTester;
import com.calclab.emite.xtesting.services.TigaseXMLService;
import com.calclab.suco.testing.events.MockedListener;

public class PrivateStorageManagerTest {
    private SessionTester session;
    private PrivateStorageManager manager;

    String storeData = "<iq type=\"set\" ><query xmlns=\"jabber:iq:private\">"
	    + "<exodus xmlns=\"exodus:prefs\"><defaultnick>Hamlet</defaultnick></exodus></query></iq>";
    String data = "<exodus xmlns=\"exodus:prefs\"><defaultnick>Hamlet</defaultnick></exodus>";

    String dataToRetrieve = "<exodus xmlns=\"exodus:prefs\"/>";
    String retriveData = "<iq type=\"get\"><query xmlns=\"jabber:iq:private\"><exodus xmlns=\"exodus:prefs\"/></query></iq>";
    String retrieveResponse = "<iq type=\"result\" from=\"hamlet@shakespeare.lit/denmark\" to=\"hamlet@shakespeare.lit/denmark\"> <query xmlns=\"jabber:iq:private\"><exodus xmlns=\"exodus:prefs\"><defaultnick>Hamlet</defaultnick></exodus></query></iq>";

    @Before
    public void setup() {
	session = new SessionTester("test@domain");
	manager = new PrivateStorageManager(session);
    }

    @Test
    public void shouldStore() {
	final MockedListener<IQResponse> listener = new MockedListener<IQResponse>();
	manager.store(new SimpleStorageData(TigaseXMLService.toPacket(data)), listener);
	session.verifyIQSent(storeData);
	listener.isCalledOnce();
    }

    @Test
    public void shoulGet() {
	final MockedListener<IQResponse> listener = new MockedListener<IQResponse>();
	manager.retrieve(new SimpleStorageData(TigaseXMLService.toPacket(dataToRetrieve)), listener);
	session.verifyIQSent(retriveData);
	session.answer(retrieveResponse);
	listener.isCalledOnce();
	assertEquals("Hamlet", listener.getValue(0).getFirstChild("query").getFirstChild("exodus").getFirstChild(
		"defaultnick").getText());
    }
}
