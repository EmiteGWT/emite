package com.calclab.emite.xep.vcard.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.xep.vcard.client.VCardTelephone.Place;
import com.calclab.emite.xep.vcard.client.VCardTelephone.Service;
import com.calclab.emite.xtesting.services.TigaseXMLService;

public class VCardTests {

    static String VALID_VCARD = "<vCard xmlns='vcard-temp'>\n" + "    <FN>Peter Saint-Andre</FN>\n" + "    <N>\n"
	    + "      <FAMILY>Saint-Andre</FAMILY>\n" + "      <GIVEN>Peter</GIVEN>\n" + "      <MIDDLE/>\n"
	    + "    </N>\n" + "    <NICKNAME>stpeter</NICKNAME>\n"
	    + "    <URL>http://www.xmpp.org/xsf/people/stpeter.shtml</URL>\n" + "    <BDAY>1966-08-06</BDAY>\n"
	    + "    <ORG>\n" + "      <ORGNAME>XMPP Standards Foundation</ORGNAME>\n"
	    + "      <ORGUNIT>Org Unit</ORGUNIT>\n" + "    </ORG>\n" + "    <TITLE>Executive Director</TITLE>\n"
	    + "    <ROLE>Patron Saint</ROLE>\n" + "    <TEL><WORK/><VOICE/><NUMBER>303-308-3282</NUMBER></TEL>\n"
	    + "    <TEL><WORK/><FAX/><NUMBER/></TEL>\n" + "    <TEL><WORK/><MSG/><NUMBER/></TEL>\n" + "    <ADR>\n"
	    + "      <WORK/>\n" + "      <EXTADD>Suite 600</EXTADD>\n" + "      <STREET>1899 Wynkoop Street</STREET>\n"
	    + "      <LOCALITY>Denver</LOCALITY>\n" + "      <REGION>CO</REGION>\n" + "      <PCODE>80202</PCODE>\n"
	    + "      <CTRY>USA</CTRY>\n" + "    </ADR>\n"
	    + "    <TEL><HOME/><VOICE/><NUMBER>303-555-1212</NUMBER></TEL>\n"
	    + "    <TEL><HOME/><FAX/><NUMBER/></TEL>\n" + "    <TEL><HOME/><MSG/><NUMBER/></TEL>\n" + "    <ADR>\n"
	    + "      <HOME/>\n" + "      <EXTADD/>\n" + "      <STREET/>\n" + "      <LOCALITY>Denver</LOCALITY>\n"
	    + "      <REGION>CO</REGION>\n" + "      <PCODE>80209</PCODE>\n" + "      <CTRY>USA</CTRY>\n"
	    + "    </ADR>\n" + "    <EMAIL><INTERNET/><PREF/><USERID>stpeter@jabber.org</USERID></EMAIL>\n"
	    + "    <JABBERID>stpeter@jabber.org</JABBERID>\n"
	    + "    <DESC>Check out my blog at https://stpeter.im/</DESC>" + "  </vCard>\n";
    private VCard vCard;

    @Before
    public void setup() {
	final IPacket packet = TigaseXMLService.toPacket(VALID_VCARD);
	vCard = new VCard(packet);
    }

    @Test
    public void shouldAddEmails() {
	final VCardEmail newEmail = new VCardEmail("some@example.com", false);
	final VCard vCard = new VCard();
	vCard.addEmail(newEmail);
	final List<VCardEmail> emails = vCard.getEmails();
	assertEquals(1, emails.size());
	final VCardEmail first = emails.get(0);
	assertEquals("some@example.com", first.getUserId());
	assertFalse(first.isPreferred());
    }

    @Test
    public void shouldParseAddress() {
	final List<VCardAddress> addresses = vCard.getAddresses();
	assertEquals(2, addresses.size());
	final VCardAddress first = addresses.get(0);
	assertTrue(first.hasPlace(VCardAddress.Place.WORK));
	assertEquals("1899 Wynkoop Street", first.getData(VCardAddress.Data.STREET));
    }

    @Test
    public void shouldParseEmails() {
	final List<VCardEmail> emails = vCard.getEmails();
	assertEquals(1, emails.size());
	final VCardEmail first = emails.get(0);
	assertTrue(first.isPreferred());
	assertTrue(first.isType(VCardEmail.Type.INTERNET));
	assertEquals("stpeter@jabber.org", first.getUserId());
    }

    @Test
    public void shouldParseOrganization() {
	final VCardOrganization organization = vCard.getOrganization();
	assertEquals("XMPP Standards Foundation", organization.getData(VCardOrganization.Data.ORGNAME));
	assertEquals("Org Unit", organization.getData(VCardOrganization.Data.ORGUNIT));
    }

    @Test
    public void shouldParseSimpleValues() {
	assertEquals("Peter Saint-Andre", vCard.getDisplayName());
	assertEquals("Saint-Andre", vCard.getFamilyName());
	assertEquals("Peter", vCard.getGivenName());
	assertEquals(null, vCard.getMiddleName());
	assertEquals("stpeter", vCard.getNickName());
	assertEquals("http://www.xmpp.org/xsf/people/stpeter.shtml", vCard.getURL());
	assertEquals("stpeter@jabber.org", vCard.getJabberID());
	assertEquals("Executive Director", vCard.getTitle());
	assertEquals("Check out my blog at https://stpeter.im/", vCard.getDescription());
    }

    @Test
    public void shouldParseTelephones() {
	final List<VCardTelephone> telephones = vCard.getTelephones();
	assertEquals(6, telephones.size());
	final VCardTelephone first = telephones.get(0);
	assertTrue(first.hasPlace(Place.WORK));
	assertTrue(first.hasService(Service.VOICE));
	assertEquals("303-308-3282", first.getNumber());
    }

    @Test
    public void shouldSetBasicValues() {
	final VCard vCard = new VCard();
	vCard.setNickName("somenick");
	vCard.setFamilyName("family name");
	vCard.setMiddleName("middle name");
	vCard.setGivenName("given name");
	assertEquals("somenick", vCard.getNickName());
	assertEquals("family name", vCard.getFamilyName());
	assertEquals("middle name", vCard.getMiddleName());
	assertEquals("given name", vCard.getGivenName());
    }

    @Test
    public void shouldSetOrganization() {
	final VCardOrganization organization = vCard.getOrganization();
	organization.setData(VCardOrganization.Data.ORGNAME, "XMPP Standards Foundation");
	organization.setData(VCardOrganization.Data.ORGUNIT, "Org Unit");
	assertEquals("XMPP Standards Foundation", organization.getData(VCardOrganization.Data.ORGNAME));
	assertEquals("Org Unit", organization.getData(VCardOrganization.Data.ORGUNIT));
	final VCard vCard = new VCard();
	vCard.setOrganization(organization);
	final VCardOrganization orgaAgain = vCard.getOrganization();
	assertEquals("XMPP Standards Foundation", orgaAgain.getData(VCardOrganization.Data.ORGNAME));
	assertEquals("Org Unit", orgaAgain.getData(VCardOrganization.Data.ORGUNIT));
    }
}
