package com.calclab.emite.xep.disco.client;

import java.util.HashMap;

import com.calclab.emite.core.client.packet.MatcherFactory;
import com.calclab.emite.core.client.packet.PacketMatcher;
import com.calclab.emite.core.client.xmpp.session.IQResponseHandler;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.core.client.xmpp.stanzas.IQ.Type;
import com.calclab.emite.xep.disco.client.events.DiscoveryInfoResultEvent;
import com.calclab.emite.xep.disco.client.events.DiscoveryInfoResultHandler;
import com.calclab.emite.xep.disco.client.events.DiscoveryItemsResultEvent;
import com.calclab.emite.xep.disco.client.events.DiscoveryItemsResultHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public class DiscoveryManagerImpl implements DiscoveryManager {
    private final XmppSession session;
    private final HashMap<XmppURI, DiscoveryInfoResults> infoResults;
    private final HashMap<XmppURI, DiscoveryItemsResults> itemsResults;
    public static final PacketMatcher ERROR_MATCHER = MatcherFactory.byName("error");

    public DiscoveryManagerImpl(final XmppSession xmppSession) {
	this.session = xmppSession;
	this.infoResults = new HashMap<XmppURI, DiscoveryInfoResults>();
	this.itemsResults = new HashMap<XmppURI, DiscoveryItemsResults>();
    }

    @Override
    public HandlerRegistration addDiscoveryInfoResultHandler(DiscoveryInfoResultHandler handler) {
	return DiscoveryInfoResultEvent.bind(session.getEventBus(), handler);
    }

    @Override
    public void areFeaturesSupported(XmppURI targetUri, final FeatureSupportedCallback callback,
	    final String... featuresName) {
	sendInfoQuery(targetUri, new DiscoveryInfoResultHandler() {
	    @Override
	    public void onDiscoveryInfoResult(DiscoveryInfoResultEvent event) {
		if (event.hasResult()) {
		    callback.onFeaturesSupported(event.getResults().areFeaturedSupported(featuresName));
		} else {
		    callback.onFeaturesSupported(false);
		}
	    }
	});
    }

    @Override
    public void sendInfoQuery(final XmppURI targetUri, final DiscoveryInfoResultHandler handler) {
	DiscoveryInfoResults cached = infoResults.get(targetUri);
	if (cached != null) {
	    if (handler != null) {
		handler.onDiscoveryInfoResult(new DiscoveryInfoResultEvent(cached));
	    }
	} else {
	    IQ iq = new IQ(Type.get, targetUri);
	    iq.addQuery("http://jabber.org/protocol/disco#info");
	    session.sendIQ("disco", iq, new IQResponseHandler() {
		@Override
		public void onIQ(IQ iq) {
		    DiscoveryInfoResultEvent event;
		    if (IQ.isSuccess(iq)) {
			DiscoveryInfoResults infoResult = new DiscoveryInfoResults(iq);
			infoResults.put(targetUri, infoResult);
			event = new DiscoveryInfoResultEvent(infoResult);
		    } else {
			event = new DiscoveryInfoResultEvent(iq.getFirstChild(ERROR_MATCHER));
		    }
		    if (handler != null) {
			handler.onDiscoveryInfoResult(event);
		    }
		    session.getEventBus().fireEvent(event);
		}
	    });
	}
    }

    @Override
    public void sendItemsQuery(final XmppURI targetUri, final DiscoveryItemsResultHandler handler) {
	DiscoveryItemsResults cached = itemsResults.get(targetUri);
	if (cached != null) {
	    if (handler != null) {
		handler.onDiscoveryItemsResult(new DiscoveryItemsResultEvent(cached));
	    }
	} else {
	    IQ iq = new IQ(Type.get, targetUri);
	    iq.addQuery("http://jabber.org/protocol/disco#items");
	    session.sendIQ("disco", iq, new IQResponseHandler() {
		@Override
		public void onIQ(IQ iq) {
		    DiscoveryItemsResultEvent event;
		    if (IQ.isSuccess(iq)) {
			DiscoveryItemsResults itemsResult = new DiscoveryItemsResults(iq);
			itemsResults.put(targetUri, itemsResult);
			event = new DiscoveryItemsResultEvent(itemsResult);
		    } else {
			event = new DiscoveryItemsResultEvent(iq.getFirstChild(ERROR_MATCHER));
		    }
		    if (handler != null) {
			handler.onDiscoveryItemsResult(event);
		    }
		    session.getEventBus().fireEvent(event);
		}
	    });
	}
    }

}
