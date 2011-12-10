package com.calclab.emite.core.client;

//import com.calclab.emite.core.client.conn.Connection;
//import com.calclab.emite.core.client.xmpp.session.Session;
//import com.calclab.emite.im.client.roster.Roster;
import com.calclab.emite.core.client.conn.XmppConnection;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.im.client.roster.XmppRoster;
import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.events.EventBusFactory;
import com.calclab.emite.core.client.services.Services;
import com.calclab.emite.core.client.xmpp.resource.ResourceBindingManager;
import com.calclab.emite.core.client.xmpp.sasl.SASLManager;
import com.calclab.emite.core.client.xmpp.session.IMSessionManager;
import com.calclab.emite.core.client.xmpp.session.SessionComponentsRegistry;

import com.calclab.emite.im.client.ImComponents;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.im.client.chat.ChatSelectionStrategy;
import com.calclab.emite.im.client.presence.PresenceManager;
import com.calclab.emite.im.client.roster.SubscriptionHandler;
import com.calclab.emite.im.client.roster.SubscriptionManager;
import com.calclab.emite.reconnect.client.SessionReconnect;
import com.calclab.emite.xep.avatar.client.AvatarManager;
import com.calclab.emite.xep.chatstate.client.ChatStateComponents;
import com.calclab.emite.xep.chatstate.client.StateManager;
import com.calclab.emite.xep.disco.client.DiscoveryManager;
import com.calclab.emite.xep.muc.client.MucComponents;
import com.calclab.emite.xep.muc.client.RoomManager;
import com.calclab.emite.xep.mucchatstate.client.MUCChatStateManager;
import com.calclab.emite.xep.mucdisco.client.RoomDiscoveryManager;
import com.calclab.emite.xep.privacylists.client.PrivacyListsManager;
import com.calclab.emite.xep.search.client.SearchManager;
import com.calclab.emite.xep.storage.client.PrivateStorageManager;
import com.calclab.emite.xep.vcard.client.VCardManager;
import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;


public class LoginXmpp implements  MultiInstance{
	public XmppConnection xmppConnection; //Converted from singleton
	public XmppSession xmppSession;	//Converted from singleton
	public SASLManager saslManager; //Converted from singleton
	public ResourceBindingManager bindingManager; //Converted from singleton
	public IMSessionManager iMSessionManager; //Converted from singleton
	
	//Gets set inside xmppBoshConnection
	public EmiteEventBus eventBus; //Converted from singleton
	
	//Services is tied to a connection
	public Services services; //Doesn't have internal state so does not need to be converted from Singleton
	
	//public Connection connection; //Converted from singleton
	//public Session session; //Converted from singleton
	
	public ChatManager chatManager; //Is bound to PairChatManager - search for bind(ChatManager
									//Converted from Singleton
	public PresenceManager presenceManager; //Converted from Singleton
	//public Roster roster; //Converted from Singleton
	public SubscriptionManager subscriptionManager; //Converted from Singleton
	public SubscriptionHandler subscriptionHandler; //Converted from Singleton
	/*The following two items do not care about outside classes but they are singletons so they could step on each others toes*/
	//public ImComponents imComponents;
	
	
	//The following is a singleton that is tied to Session and it has internal state, therefore even though its creation does not require
	//knowing instance id of hablar and it does not depend on others, others depend on it and its internal state
	//, we still need to convert it to a non singleton and tie it to this instance.  
	public SessionComponentsRegistry registry;	 //Converted from Singleton
	
	//public ChatSelectionStrategy chatSelectionStrategy;	
	public XmppRoster xmppRoster; //Converted from Singleton
	public StateManager stateManager; //Converted from Singleton
	public RoomManager roomManager; //Converted from Singleton
	public DiscoveryManager discoveryManager; //Converted from Singleton
	public RoomDiscoveryManager roomDiscoveryManager; //Converted from Singleton
	public PrivateStorageManager privateStorageManager; //Converted from Singleton
	public VCardManager vCardManager; //Converted from Singleton 
	public SearchManager searchManager; //Converted from Singleton
	public ChatStateComponents chatStateComponents;  //Singleton
	public MucComponents mucComponents; //Singleton
	public MUCChatStateManager mucChatStateManager;
	private PrivacyListsManager privacyListsManager;
	private SessionReconnect reconnectModule;
	private AvatarManager avatarManager;
	
	
	
	//XmppInjector ginjector = GWT.create(XmppInjector.class);
	
	@Inject
	LoginXmpp(XmppConnection xmppConnection, XmppSession xmppSession, SASLManager saslManager,  ResourceBindingManager bindingManager, 
			IMSessionManager iMSessionManager, SessionComponentsRegistry registry, 
			//Connection connection, 
			//Session session, 
			//Roster roster, 
			//EmiteEventBus eventBus, 
			Services services, 			
			ChatManager chatManager, PresenceManager presenceManager,  
			SubscriptionManager subscriptionManager, SubscriptionHandler subscriptionHandler,
			ImComponents imComponents,  XmppRoster xmppRoster, RoomManager roomManager, DiscoveryManager discoveryManager, RoomDiscoveryManager roomDiscoveryManager,
			PrivateStorageManager privateStorageManager, VCardManager vCardManager, SearchManager searchManager, StateManager stateManager, 
			ChatStateComponents chatStateComponents, MucComponents mucComponents, MUCChatStateManager mucChatStateManager, 
			PrivacyListsManager privacyListsManager, 
			SessionReconnect reconnectModule, 
			AvatarManager avatarManager
			//ChatSelectionStrategy chatSelectionStrategy,		
			
			)  {
		
		this.xmppConnection = xmppConnection;
		this.xmppSession = xmppSession;
		this.saslManager = saslManager;
		this.bindingManager = bindingManager;
		this.iMSessionManager = iMSessionManager;
		this.registry = registry; 
		//this.connection =  Suco.get(Connection.class);
		//this.session =  Suco.get(Session.class);
		//this.eventBus = eventBus;				
		this.services = services;
		
		
		this.chatManager = chatManager;		
		this.stateManager = stateManager;
		this.discoveryManager = discoveryManager;
		this.roomDiscoveryManager = roomDiscoveryManager;
		this.presenceManager = presenceManager;
		//this.roster = roster;
		this.subscriptionManager = subscriptionManager;
		this.subscriptionHandler = subscriptionHandler;
		//this.imComponents = imComponents;
		//this.chatSelectionStrategy = chatSelectionStrategy;
		this.xmppRoster = xmppRoster;
		this.roomManager = roomManager;
		this.privateStorageManager = privateStorageManager;
		this.vCardManager = vCardManager;
		this.searchManager = searchManager;
		this.chatStateComponents = chatStateComponents;
		this.mucComponents = mucComponents;
		
		this.stateManager = stateManager;
		this.mucChatStateManager = mucChatStateManager;
		this.privacyListsManager = privacyListsManager;
		this.reconnectModule = reconnectModule;
		this.avatarManager = avatarManager;
		
	}

	@Override
	public void setInstanceId(String instanceId) {
		
		eventBus = EventBusFactory.create("emite" + instanceId);
		
		xmppConnection.setInstanceId(instanceId);
		xmppSession.setInstanceId(instanceId );
		saslManager.setInstanceId(instanceId);
		bindingManager.setInstanceId(instanceId);
		iMSessionManager.setInstanceId(instanceId);
		registry.setInstanceId(instanceId);
		//connection.setInstanceId(instanceId);
		//session.setInstanceId(instanceId);
		eventBus.setInstanceId(instanceId);
		services.setInstanceId(instanceId);
		
		//Registry adder
		chatStateComponents.setInstanceId(instanceId);		
		stateManager.setInstanceId(instanceId);
		
		//Registry adder
		mucComponents.setInstanceId(instanceId);
		roomManager.setInstanceId(instanceId);	
		
		mucChatStateManager.setInstanceId(instanceId);
		
		discoveryManager.setInstanceId(instanceId);
		roomDiscoveryManager.setInstanceId(instanceId);
		presenceManager.setInstanceId(instanceId);
		privateStorageManager.setInstanceId(instanceId);
		subscriptionManager.setInstanceId(instanceId);
		xmppRoster.setInstanceId(instanceId);	
		//roster.setInstanceId(instanceId);
		subscriptionHandler.setInstanceId(instanceId);
		chatManager.setInstanceId(instanceId);
		vCardManager.setInstanceId(instanceId);
		searchManager.setInstanceId(instanceId);
		reconnectModule.setInstanceId(instanceId);
		
		privacyListsManager.setInstanceId(instanceId);
		avatarManager.setInstanceId(instanceId);
	}
	
}
