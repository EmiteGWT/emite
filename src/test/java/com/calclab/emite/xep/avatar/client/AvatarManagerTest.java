/*
 * ((e)) emite: A pure Google Web Toolkit XMPP library
 * Copyright (c) 2008-2011 The Emite development team
 * 
 * This file is part of Emite.
 *
 * Emite is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * Emite is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with Emite.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.calclab.emite.xep.avatar.client;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xtesting.XmppSessionTester;
import com.calclab.emite.xtesting.handlers.AvatarVCardTestHandler;
import com.calclab.emite.xtesting.handlers.PresenceTestHandler;

public class AvatarManagerTest {
	private AvatarManager avatarManager;
	private XmppSessionTester session;

	@Before
	public void aaaCreateManager() {
		session = new XmppSessionTester();
		avatarManager = new AvatarManager(session);
	}

	@Test
	public void managerShouldListenPresenceWithPhoto() {
		final PresenceTestHandler handler = new PresenceTestHandler();
		avatarManager.addHashPresenceReceviedHandler(handler);
		final Presence presence = new Presence(XmppURI.uri("juliet@capulet.com/balcony"));
		presence.addChild("x", "vcard-temp:x:update").addChild("photo", null).setText("sha1-hash-of-image");
		session.receives(presence);
		assertTrue(handler.isCalledOnce());
		assertEquals(presence, handler.getLastPresence());
	}

	@Test
	public void managerShouldPublishAvatar() {
		session.setLoggedIn(uri("romeo@montague.net/orchard"));
		final String photo = "some base64 encoded photo";
		avatarManager.setVCardAvatar(photo);
		session.verifyIQSent("<iq type='set'><vCard prodid='-//HandGen//NONSGML vGen v1.0//EN' " + "version='2.0' xmlns='vcard-temp' xdbns='vcard-temp'>"
				+ "<PHOTO><BINVAL>some base64 encoded photo</BINVAL></PHOTO></vCard></iq>");
		session.answerSuccess();
		// User's Server Acknowledges Publish:
		// <iq to='juliet@capulet.com' type='result' id='vc1'/>
	}

	@Test
	public void verifySendVcardRequest() {
		final AvatarVCardTestHandler handler = new AvatarVCardTestHandler();
		avatarManager.addAvatarVCardReceivedHandler(handler);

		session.setLoggedIn(uri("romeo@montague.net/orchard"));
		avatarManager.requestVCard(XmppURI.uri("juliet@capulet.com"));
		session.verifyIQSent("<iq to='juliet@capulet.com' type='get'><vCard xmlns='vcard-temp'/></iq>");
		session.answer("<iq from='juliet@capulet.com' to='romeo@montague.net/orchard' type='result'>"
				+ "<vCard xmlns='vcard-temp'><PHOTO><TYPE>image/jpeg</TYPE>" + "<BINVAL>Base64-encoded-avatar-file-here!</BINVAL></PHOTO></vCard></iq>");
		assertTrue(handler.isCalledOnce());
	}
}
