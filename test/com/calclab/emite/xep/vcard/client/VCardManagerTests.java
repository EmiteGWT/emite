package com.calclab.emite.xep.vcard.client;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.core.client.xmpp.stanzas.IQ.Type;
import com.calclab.emite.xtesting.SessionTester;
import com.calclab.suco.testing.events.MockedListener;

public class VCardManagerTests {
    String VALID_VCARD = "<iq id='v1'\n" + "    to='stpeter@jabber.org/roundabout'\n"
	    + "    type='result'>\n" + "  <vCard xmlns='vcard-temp'>\n"
	    + "    <FN>Peter Saint-Andre</FN>\n" + "    <N>\n"
	    + "      <FAMILY>Saint-Andre</FAMILY>\n" + "      <GIVEN>Peter</GIVEN>\n"
	    + "      <MIDDLE/>\n" + "    </N>\n" + "    <NICKNAME>stpeter</NICKNAME>\n"
	    + "    <URL>http://www.xmpp.org/xsf/people/stpeter.shtml</URL>\n"
	    + "    <BDAY>1966-08-06</BDAY>\n" + "    <ORG>\n"
	    + "      <ORGNAME>XMPP Standards Foundation</ORGNAME>\n" + "      <ORGUNIT/>\n"
	    + "    </ORG>\n" + "    <TITLE>Executive Director</TITLE>\n"
	    + "    <ROLE>Patron Saint</ROLE>\n"
	    + "    <TEL><WORK/><VOICE/><NUMBER>303-308-3282</NUMBER></TEL>\n"
	    + "    <TEL><WORK/><FAX/><NUMBER/></TEL>\n" + "    <TEL><WORK/><MSG/><NUMBER/></TEL>\n"
	    + "    <ADR>\n" + "      <WORK/>\n" + "      <EXTADD>Suite 600</EXTADD>\n"
	    + "      <STREET>1899 Wynkoop Street</STREET>\n"
	    + "      <LOCALITY>Denver</LOCALITY>\n" + "      <REGION>CO</REGION>\n"
	    + "      <PCODE>80202</PCODE>\n" + "      <CTRY>USA</CTRY>\n" + "    </ADR>\n"
	    + "    <TEL><HOME/><VOICE/><NUMBER>303-555-1212</NUMBER></TEL>\n"
	    + "    <TEL><HOME/><FAX/><NUMBER/></TEL>\n" + "    <TEL><HOME/><MSG/><NUMBER/></TEL>\n"
	    + "    <ADR>\n" + "      <HOME/>\n" + "      <EXTADD/>\n" + "      <STREET/>\n"
	    + "      <LOCALITY>Denver</LOCALITY>\n" + "      <REGION>CO</REGION>\n"
	    + "      <PCODE>80209</PCODE>\n" + "      <CTRY>USA</CTRY>\n" + "    </ADR>\n"
	    + "    <EMAIL><INTERNET/><PREF/><USERID>stpeter@jabber.org</USERID></EMAIL>\n"
	    + "    <JABBERID>stpeter@jabber.org</JABBERID>\n" + "    <DESC>\n"
	    + "      More information about me is located on my \n"
	    + "      personal website: http://www.saint-andre.com/\n" + "    </DESC>\n"
	    + "  </vCard>\n" + "</iq>";

    private SessionTester session;
    private VCardManager manager;

    @Before
    public void setup() {
	session = new SessionTester("test@domain");
	manager = new VCardManager(session);
    }

    @Test
    public void shouldParseVCard() {
	MockedListener<VCardResponse> listener = new MockedListener<VCardResponse>();
	manager.requestOwnVCard(listener);
	session.verifyIQSent(new IQ(Type.get));
	session.answer(VALID_VCARD);
	assertTrue(listener.isCalledOnce());
    }

    @Test
    public void shouldSendRetrievalRequest() {
	manager.requestOwnVCard(null);
	IQ iq = new IQ(IQ.Type.get).With("from", "test@domain");
	iq.addChild("vCard", "vcard-temp");
	session.verifyIQSent(iq);
    }
}
