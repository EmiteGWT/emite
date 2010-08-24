package com.calclab.emite.xep.vcard.client;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.core.client.xmpp.stanzas.IQ.Type;
import com.calclab.emite.xtesting.XmppSessionTester;
import com.calclab.suco.testing.events.MockedListener;

public class VCardManagerTests {
    String VALID_VCARD = "<iq id='v1'\n" + "    to='stpeter@jabber.org/roundabout'\n" + "    type='result'>\n"
	    + "  <vCard xmlns='vcard-temp'>\n" + "    <FN>Peter Saint-Andre</FN>\n" + "    <N>\n"
	    + "      <FAMILY>Saint-Andre</FAMILY>\n" + "      <GIVEN>Peter</GIVEN>\n" + "      <MIDDLE/>\n"
	    + "    </N>\n" + "    <NICKNAME>stpeter</NICKNAME>\n"
	    + "    <URL>http://www.xmpp.org/xsf/people/stpeter.shtml</URL>\n" + "    <BDAY>1966-08-06</BDAY>\n"
	    + "    <ORG>\n" + "      <ORGNAME>XMPP Standards Foundation</ORGNAME>\n" + "      <ORGUNIT/>\n"
	    + "    </ORG>\n" + "    <TITLE>Executive Director</TITLE>\n" + "    <ROLE>Patron Saint</ROLE>\n"
	    + "    <TEL><WORK/><VOICE/><NUMBER>303-308-3282</NUMBER></TEL>\n"
	    + "    <TEL><WORK/><FAX/><NUMBER/></TEL>\n" + "    <TEL><WORK/><MSG/><NUMBER/></TEL>\n" + "    <ADR>\n"
	    + "      <WORK/>\n" + "      <EXTADD>Suite 600</EXTADD>\n" + "      <STREET>1899 Wynkoop Street</STREET>\n"
	    + "      <LOCALITY>Denver</LOCALITY>\n" + "      <REGION>CO</REGION>\n" + "      <PCODE>80202</PCODE>\n"
	    + "      <CTRY>USA</CTRY>\n" + "    </ADR>\n"
	    + "    <TEL><HOME/><VOICE/><NUMBER>303-555-1212</NUMBER></TEL>\n"
	    + "    <TEL><HOME/><FAX/><NUMBER/></TEL>\n" + "    <TEL><HOME/><MSG/><NUMBER/></TEL>\n" + "    <ADR>\n"
	    + "      <HOME/>\n" + "      <EXTADD/>\n" + "      <STREET/>\n" + "      <LOCALITY>Denver</LOCALITY>\n"
	    + "      <REGION>CO</REGION>\n" + "      <PCODE>80209</PCODE>\n" + "      <CTRY>USA</CTRY>\n"
	    + "    </ADR>\n" + "    <EMAIL><INTERNET/><PREF/><USERID>stpeter@jabber.org</USERID></EMAIL>\n"
	    + "    <JABBERID>stpeter@jabber.org</JABBERID>\n" + "    <DESC>\n"
	    + "      More information about me is located on my \n"
	    + "      personal website: http://www.saint-andre.com/\n" + "    </DESC>\n" + "  </vCard>\n" + "</iq>";

    String OTHER_VALID_VCARD = "<iq type=\"result\" id=\"vcard_2\" to=\"admin@localhost/Gajim\">\n"
	    + "<vCard xmlns=\"vcard-temp\">\n" + "<PHOTO>\n" + "<TYPE>jpeg</TYPE>\n"
	    + "<BINVAL>/9j/4AAQSkZJRgABAQEASABIAAD/4QAWRXhpZgAATU0AKgAAAAgAAAAAAAD/2wBDAAUDBAQEAwUE\n"
	    + "BAQFBQUGBwwIBwcHBw8LCwkMEQ8SEhEPERETFhwXExQaFRERGCEYGh0dHx8fExciJCIeJBweHx7/\n"
	    + "wAALCAA8ACkBAREA/8QAHAAAAgMBAAMAAAAAAAAAAAAAAAQDBQYCAQcI/8QAMBAAAgEDAwQBAgIL\n"
	    + "AAAAAAAAAQIDAAQRBRIhBhMxQVEikRVhBxQjMlNxcoGToeH/2gAIAQEAAD8A+y6KS1iW/S0ddNg7\n"
	    + "lyUJQsQFBAJ9+84A/nnwDWXvour1BtY5Z3h3cSAosjKsn1sWAOMoBtUYOXwRtU0l0f11Lf63JpN5\n"
	    + "2Eitn7TXDErvBQdtsknJYo5PwHjHk17CopFtRPcYQ2F3PGrMplTYFyuQQAWDHBBHApSLqbSpzClq\n"
	    + "9zcSzgmGNLZwZAPOCwA49kkY909Bfo92trLBNbzOhdFk2/WoIBIKkjjI4PPNY270mxTrUP2zKiXA\n"
	    + "nuSpOQ0hVUB4xjL/ALvBxk/1b2uZZEijaWV1SNBuZmOAAPZNZyTTb7U5pjHdXdlZkmS3kfY00bsC\n"
	    + "GZAyFkBVmwS24biMAcVPp3TdlpNwJtNt4wTBHExkc727ZbaS/JbhyMH4GPGKetLCX8ROo3kyyTiM\n"
	    + "xQogwkKEgsPliSoyTjhRgDnNDLc291cPLaRZbU7u3aCVBkTRwyqHz8YUOeccHjkVqNlz/Hj/AMf/\n"
	    + "AGq2e6sn1KUaheQQpbSKIoZJAoZsAhznzycD0CM+cYL/AF0W3dZLR3ihALySOEBBJAKjliODzgDg\n"
	    + "nOAatYJY54+5E4ZdxXI+QSCP7EEVHeQpd2s1o5PblRo3wcHBGOPvXdtDFbW8dvAgSKNQqKPAA8Cp\n"
	    + "KKorgLNrslvLd3ETu6osaxxkSRiMtzuUnbneMjHLEVLo04Md1OFeO2lkEyK3O0MAWz6AOQ35bj+d\n"
	    + "PafewXhmEAb9lIyPkYwwZgR9x/sU1RRQQD5ANYX9MCatcaC+l2Wp/h8GqodPEqRZZZZcqvPoEkLn\n"
	    + "jGT5zxJ010kmj9U2t09/eqyWsqxW360ZY2GUDu5KrljlfIOcA5yK21FFFIa3YwalBDZ3VmLmAzxy\n"
	    + "uGI2qY2DqSD5+pRxzUlsryyQ3d1AIZwjRhA27bk5POPB2qabooope+tRddnM88PalEgMT7dxAPB+\n"
	    + "Rz4915hhVQiOzS9nGxn5bO3Gc/OCfvU9Ff/Z</BINVAL>\n" + "</PHOTO>\n" + "<NICKNAME>ad</NICKNAME>\n"
	    + "<FN>adminnnn</FN>\n" + "</vCard>\n" + "</iq>";

    String OTHER_VCARD = "<iq from='test@domain' to='test2@domain'" + "type='get'><vCard xmlns='vcard-temp'/></iq>";
    String VCARD_SETTED = "<iq type='set'><vCard xmlns='vcard-temp'><FN>Peter Saint-Andre</FN><N><FAMILY>Saint-Andre</FAMILY><GIVEN>Peter</GIVEN><MIDDLE/></N><NICKNAME>stpeter</NICKNAME></vCard></iq>";

    private XmppSessionTester session;
    private VCardManager manager;

    @Before
    public void setup() {
	session = new XmppSessionTester("test@domain");
	manager = new VCardManager(session);
    }

    @Test
    public void shouldParseOtherVCard() {
	shouldParseVCardImpl(OTHER_VALID_VCARD);
    }

    @Test
    public void shouldParseVCard() {
	shouldParseVCardImpl(VALID_VCARD);
    }

    @Test
    public void shouldRequestVCard() {
	final MockedListener<VCardResponse> listener = new MockedListener<VCardResponse>();
	manager.getUserVCard(XmppURI.uri("test2@domain"), listener);
	session.verifyIQSent(OTHER_VCARD);
    }

    @Test
    public void shouldSendRetrievalRequest() {
	manager.requestOwnVCard(null);
	final IQ iq = new IQ(IQ.Type.get).With("from", "test@domain");
	iq.addChild("vCard", "vcard-temp");
	session.verifyIQSent(iq);
    }

    @Test
    public void shouldSetOwnVCard() {
	final MockedListener<VCardResponse> listener = new MockedListener<VCardResponse>();
	final VCard vCard = new VCard();
	vCard.setDisplayName("Peter Saint-Andre");
	vCard.setFamilyName("Saint-Andre");
	vCard.setGivenName("Peter");
	vCard.setMiddleName("");
	vCard.setNickName("stpeter");
	manager.updateOwnVCard(vCard, listener);
	session.verifyIQSent(VCARD_SETTED);
    }

    private void shouldParseVCardImpl(final String vcard) {
	final MockedListener<VCardResponse> listener = new MockedListener<VCardResponse>();
	manager.requestOwnVCard(listener);
	session.verifyIQSent(new IQ(Type.get));
	session.answer(vcard);
	assertTrue(listener.isCalledOnce());
	assertTrue(listener.getValue(0).hasVCard());
	assertNotNull(listener.getValue(0).getVCard().getNickName());
    }
}
