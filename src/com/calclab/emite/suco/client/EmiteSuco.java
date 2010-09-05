package com.calclab.emite.suco.client;

import com.calclab.emite.browser.client.AutoConfig;
import com.calclab.emite.browser.client.BrowserGinjector;
import com.calclab.emite.core.client.CoreGinjector;
import com.calclab.emite.core.client.conn.Connection;
import com.calclab.emite.core.client.conn.XmppConnection;
import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.services.Services;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.im.client.ImGinjector;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.im.client.presence.PresenceManager;
import com.calclab.emite.im.client.roster.Roster;
import com.calclab.emite.im.client.roster.SubscriptionHandler;
import com.calclab.emite.im.client.roster.SubscriptionManager;
import com.calclab.emite.im.client.roster.XmppRoster;
import com.calclab.emite.reconnect.client.ReconnectGinjector;
import com.calclab.emite.reconnect.client.SessionReconnect;
import com.calclab.emite.xep.avatar.client.AvatarGinjector;
import com.calclab.emite.xep.avatar.client.AvatarManager;
import com.calclab.emite.xep.chatstate.client.ChatStateGinjector;
import com.calclab.emite.xep.chatstate.client.StateManager;
import com.calclab.emite.xep.muc.client.MucGinjector;
import com.calclab.emite.xep.muc.client.RoomManager;
import com.calclab.emite.xep.mucchatstate.client.MUCChatStateManager;
import com.calclab.emite.xep.mucchatstate.client.MucChatStateGinjector;
import com.calclab.emite.xep.mucdisco.client.MucDiscoveryGinjector;
import com.calclab.emite.xep.mucdisco.client.RoomDiscoveryManager;
import com.calclab.emite.xep.search.client.SearchGinjector;
import com.calclab.emite.xep.search.client.SearchManager;
import com.calclab.emite.xep.storage.client.PrivateStorageGinjector;
import com.calclab.emite.xep.storage.client.PrivateStorageManager;
import com.calclab.emite.xep.vcard.client.VCardGinjector;
import com.calclab.emite.xep.vcard.client.VCardManager;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.ioc.Provider;
import com.google.gwt.core.client.GWT;

public class EmiteSuco {

    public static EmiteSucoGinjector install() {
	EmiteSucoGinjector ginjector = GWT.create(EmiteSucoGinjector.class);
	installCore(ginjector);
	installBrowser(ginjector);
	installChatState(ginjector);
	installIM(ginjector);
	installReconnect(ginjector);
	installAvatar(ginjector);
	installMuc(ginjector);
	installMucChatState(ginjector);
	installMucDiscovery(ginjector);
	installSearch(ginjector);
	installPrivateStorage(ginjector);
	installVCard(ginjector);
	return ginjector;
    }

    public static <T> void install(final Class<T> type, final Provider<T> provider) {
	Suco.add(type, new Provider<T>() {
	    @Override
	    public T get() {
		GWT.log("Provide: " + type);
		return provider.get();
	    }
	});
    }

    public static void installAvatar(final AvatarGinjector ginjector) {
	install(AvatarManager.class, new Provider<AvatarManager>() {
	    @Override
	    public AvatarManager get() {
		return ginjector.getAvatarManager();
	    }
	});
    }

    public static void installBrowser(final BrowserGinjector ginjector) {
	install(AutoConfig.class, new Provider<AutoConfig>() {
	    @Override
	    public AutoConfig get() {
		return ginjector.getAutoConfig();
	    }
	});

    }

    public static void installChatState(final ChatStateGinjector ginjector) {
	install(StateManager.class, new Provider<StateManager>() {
	    @Override
	    public StateManager get() {
		return ginjector.getStateManager();
	    }
	});
    }

    public static void installCore(final CoreGinjector ginjector) {
	install(Connection.class, new Provider<Connection>() {
	    @Override
	    public Connection get() {
		return ginjector.getConnection();
	    }
	});
	install(EmiteEventBus.class, new Provider<EmiteEventBus>() {
	    @Override
	    public EmiteEventBus get() {
		return ginjector.getEmiteEventBus();
	    }
	});
	install(Services.class, new Provider<Services>() {
	    @Override
	    public Services get() {
		return ginjector.getServices();
	    }
	});
	install(Session.class, new Provider<Session>() {
	    @Override
	    public Session get() {
		return ginjector.getSession();
	    }
	});
	install(XmppConnection.class, new Provider<XmppConnection>() {
	    @Override
	    public XmppConnection get() {
		return ginjector.getXmppConnection();
	    }
	});
	install(XmppSession.class, new Provider<XmppSession>() {
	    @Override
	    public XmppSession get() {
		return ginjector.getXmppSession();
	    }
	});
    }

    public static void installIM(final ImGinjector ginjector) {

	install(ChatManager.class, new Provider<ChatManager>() {
	    @Override
	    public ChatManager get() {
		return ginjector.getChatManager();
	    }
	});
	install(PresenceManager.class, new Provider<PresenceManager>() {
	    @Override
	    public PresenceManager get() {
		return ginjector.getPresenceManager();
	    }
	});
	install(Roster.class, new Provider<Roster>() {
	    @Override
	    public Roster get() {
		return ginjector.getRoster();
	    }
	});
	install(SubscriptionHandler.class, new Provider<SubscriptionHandler>() {
	    @Override
	    public SubscriptionHandler get() {
		return ginjector.getSubscriptionHandler();
	    }
	});
	install(SubscriptionManager.class, new Provider<SubscriptionManager>() {
	    @Override
	    public SubscriptionManager get() {
		return ginjector.getSubscriptionManager();
	    }
	});
	install(XmppRoster.class, new Provider<XmppRoster>() {
	    @Override
	    public XmppRoster get() {
		return ginjector.getXmppRoster();
	    }
	});
    }

    public static void installMuc(final MucGinjector ginjector) {
	install(RoomManager.class, new Provider<RoomManager>() {
	    @Override
	    public RoomManager get() {
		return ginjector.getRoomManager();
	    }
	});
    }

    public static void installMucChatState(final MucChatStateGinjector ginjector) {
	install(MUCChatStateManager.class, new Provider<MUCChatStateManager>() {
	    @Override
	    public MUCChatStateManager get() {
		return ginjector.getMUCChatStateManager();
	    }
	});
    }

    public static void installMucDiscovery(final MucDiscoveryGinjector ginjector) {
	install(RoomDiscoveryManager.class, new Provider<RoomDiscoveryManager>() {
	    @Override
	    public RoomDiscoveryManager get() {
		return ginjector.getRoomDiscoveryManager();
	    }
	});
    }

    public static void installPrivateStorage(final PrivateStorageGinjector ginjector) {
	install(PrivateStorageManager.class, new Provider<PrivateStorageManager>() {
	    @Override
	    public PrivateStorageManager get() {
		return ginjector.getPrivateStorageManager();
	    }
	});
    }

    public static void installReconnect(final ReconnectGinjector ginjector) {
	install(SessionReconnect.class, new Provider<SessionReconnect>() {
	    @Override
	    public SessionReconnect get() {
		return ginjector.getSessionReconnect();
	    }
	});

    }

    public static void installSearch(final SearchGinjector ginjector) {
	install(SearchManager.class, new Provider<SearchManager>() {
	    @Override
	    public SearchManager get() {
		return ginjector.getSearchManager();
	    }
	});

    }

    public static void installVCard(final VCardGinjector ginjector) {
	install(VCardManager.class, new Provider<VCardManager>() {
	    @Override
	    public VCardManager get() {
		return ginjector.getVCardManager();
	    }
	});
    }
}
