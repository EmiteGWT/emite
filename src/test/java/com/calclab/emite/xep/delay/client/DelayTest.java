package com.calclab.emite.xep.delay.client;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.Packet;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.google.gwt.junit.client.GWTTestCase;

public class DelayTest extends GWTTestCase {

	@Override
	public String getModuleName() {
		return "com.calclab.emite.xep.delay.EmiteDelay";
	}

	@Test
	public void shouldCalculateDelay() {

		final XmppURI uri = uri("name@domain/resource");
		final IPacket delayNode = new Packet("delay", "urn:xmpp:delay");
		delayNode.setAttribute("from", "name@domain/resource");
		delayNode.setAttribute("stamp", "1980-04-15T17:15:02.159+01:00");
		final Delay delay = new Delay(delayNode);
		assertNotNull(delay);
		final Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(1980, Calendar.APRIL, 15, 17, 15, 02);
		cal.setTimeZone(TimeZone.getTimeZone("GMT+01:00"));
		cal.set(Calendar.MILLISECOND, 159);
		final Date date = cal.getTime();
		assertEquals(uri, delay.getFrom());
		assertEquals(date, delay.getStamp());
	}

	@Test
	public void shouldCalculateDelayLegacyFormat() {

		final XmppURI uri = uri("name@domain/resource");
		final IPacket delayNode = new Packet("x", "jabber:x:delay");
		delayNode.setAttribute("xmlns", "jabber:x:delay");
		delayNode.setAttribute("from", "name@domain/resource");
		delayNode.setAttribute("stamp", "19800415T17:15:02");
		final Delay delay = new Delay(delayNode);
		assertNotNull(delay);
		final Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(1980, Calendar.APRIL, 15, 17, 15, 02);
		final Date date = cal.getTime();
		assertEquals(uri, delay.getFrom());
		assertEquals(date, delay.getStamp());
	}

}
