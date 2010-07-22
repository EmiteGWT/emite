/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008-2009 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emite.xep.disco.client;

import java.util.ArrayList;
import java.util.List;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.MatcherFactory;
import com.calclab.emite.core.client.packet.PacketMatcher;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.core.client.xmpp.stanzas.IQ.Type;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.events.Event;
import com.calclab.suco.client.events.Listener;

public class DiscoveryManager {
    private final Session session;

    public DiscoveryManager(final Session session) {
	this.session = session;
//	session.onStateChanged(new Listener<Session>() {
//	    @Override
//	    public void onEvent(Session session) {
//		if (session.getState() == Session.State.loggedIn) {
//		    sendDiscoQuery(session.getCurrentUser(), null, listener);
//		}
//	    }
//	});
    }

    public void sendDiscoQuery(final XmppURI uri, XmppURI hostUri, Listener<DiscoveryManagerResponse> listener) {
	final IQ iq = new IQ(Type.get, hostUri != null ? hostUri : uri.getHostURI()).From(uri);
	iq.addQuery("http://jabber.org/protocol/disco#info");
	final Event<DiscoveryManagerResponse> onReady = new Event<DiscoveryManagerResponse>("discoveryManager:onReady");
	onReady.add(listener);
	final PacketMatcher filterQuery = MatcherFactory.byNameAndXMLNS("query",
		"http://jabber.org/protocol/disco#info");
	session.sendIQ("disco", iq, new Listener<IPacket>() {
	    public void onEvent(final IPacket response) {
		final IPacket query = response.getFirstChild(filterQuery);
		onReady.fire(new DiscoveryManagerResponse(processIdentity(query.getChildren(MatcherFactory
			.byName("identity"))), processFeatures(query.getChildren(MatcherFactory.byName("feature")))));
	    }
	});
    }

    public void sendDiscoItemsQuery(final XmppURI uri, XmppURI hostUri, Listener<DiscoveryManagerResponse> listener) {
	final IQ iq = new IQ(Type.get, hostUri != null ? hostUri : uri.getHostURI()).From(uri);
	iq.addQuery("http://jabber.org/protocol/disco#items");
	final Event<DiscoveryManagerResponse> onReady = new Event<DiscoveryManagerResponse>("discoveryManager:onReady");
	onReady.add(listener);
	final PacketMatcher filterQuery = MatcherFactory.byNameAndXMLNS("query",
		"http://jabber.org/protocol/disco#items");
	session.sendIQ("disco", iq, new Listener<IPacket>() {
	    public void onEvent(final IPacket response) {
		final IPacket query = response.getFirstChild(filterQuery);
		onReady.fire(new DiscoveryManagerResponse(processItem(query.getChildren(MatcherFactory.byName("item")))));
	    }
	});
    }

    private List<Feature> processFeatures(final List<? extends IPacket> children) {
	List<Feature> features = new ArrayList<Feature>();
	for (final IPacket child : children) {
	    features.add(Feature.fromPacket(child));
	}
	return features;
    }

    private List<Identity> processIdentity(final List<? extends IPacket> children) {
	List<Identity> identities = new ArrayList<Identity>();
	for (final IPacket child : children) {
	    identities.add(Identity.fromPacket(child));
	}
	return identities;
    }

    private List<Item> processItem(final List<? extends IPacket> children) {
	List<Item> items = new ArrayList<Item>();
	for (final IPacket child : children) {
	    items.add(Item.fromPacket(child));
	}
	return items;
    }

    public static class DiscoveryManagerResponse {
	private List<Identity> identities;

	private List<Feature> features;

	private List<Item> items;

	public DiscoveryManagerResponse(List<Identity> identities, List<Feature> features) {
	    this(identities, features, null);
	}

	public DiscoveryManagerResponse(List<Item> items) {
	    this(null, null, items);
	}

	public DiscoveryManagerResponse(List<Identity> identities, List<Feature> features, List<Item> items) {
	    this.identities = identities;
	    this.features = features;
	    this.items = items;
	}

	public List<Identity> getIdentities() {
	    return identities;
	}

	public List<Feature> getFeatures() {
	    return features;
	}

	public List<Item> getItems() {
	    return items;
	}
    }
}
