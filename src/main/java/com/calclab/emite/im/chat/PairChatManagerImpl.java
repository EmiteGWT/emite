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

package com.calclab.emite.im.chat;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import javax.annotation.Nullable;

import com.calclab.emite.core.XmppURI;
import com.calclab.emite.core.events.MessageReceivedEvent;
import com.calclab.emite.core.events.SessionStatusChangedEvent;
import com.calclab.emite.core.events.ChangedEvent.ChangeType;
import com.calclab.emite.core.session.SessionStatus;
import com.calclab.emite.core.session.XmppSession;
import com.calclab.emite.core.stanzas.Message;
import com.calclab.emite.im.events.PairChatChangedEvent;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

@Singleton
public final class PairChatManagerImpl implements PairChatManager, SessionStatusChangedEvent.Handler, MessageReceivedEvent.Handler {

	private final EventBus eventBus;
	private final XmppSession session;
	private final Set<PairChat> chats;
	
	@Nullable private XmppURI currentChatUser;
	
	@Inject
	protected PairChatManagerImpl(@Named("emite") final EventBus eventBus, final XmppSession session) {
		this.eventBus = checkNotNull(eventBus);
		this.session = checkNotNull(session);
		chats = Sets.newHashSet();
		
		// Control chat status when the user logout and login again
		session.addSessionStatusChangedHandler(this, true);
		
		session.addMessageReceivedHandler(this);
	}

	@Override
	public final void onSessionStatusChanged(final SessionStatusChangedEvent event) {
		if (SessionStatus.loggedIn.equals(event.getStatus())) {
			final XmppURI currentUser = session.getCurrentUserURI();
			if (currentChatUser == null) {
				currentChatUser = currentUser;
			}
			if (currentUser.equalsNoResource(currentChatUser)) {
				for (final PairChat chat : chats) {
					chat.open();
				}
			}
		} else if (SessionStatus.loggingOut.equals(event.getStatus()) || SessionStatus.disconnected.equals(event.getStatus())) {
			// check both status: loggingOut is preferred, but not
			// always fired (i.e. error)
			for (final PairChat chat : chats) {
				chat.close();
			}
		}
	}
	
	@Override
	public final void onMessageReceived(final MessageReceivedEvent event) {
		final Message message = event.getMessage();
		
		if (Message.Type.groupchat.equals(message.getType()))
			return;
		
		PairChat chat = getChat(message.getFrom());
		if (chat == null && message.getBody() != null) {
			// we need to create a chat for this incoming message
			chat = openChat(message.getFrom());
		}
		
		if (chat != null) {
			chat.receiveMessage(message);
		}
	}

	@Override
	public final HandlerRegistration addPairChatChangedHandler(final PairChatChangedEvent.Handler handler) {
		return eventBus.addHandlerToSource(PairChatChangedEvent.TYPE, this, handler);
	}

	@Override
	public final PairChat openChat(final XmppURI uri) {
		PairChat chat = getChat(uri);
		if (chat == null) {
			chat = new PairChat(this, eventBus, session, uri, session.getCurrentUserURI());
			chats.add(chat);
			eventBus.fireEventFromSource(new PairChatChangedEvent(ChangeType.created, chat), this);
		}
		
		chat.open();
		eventBus.fireEventFromSource(new PairChatChangedEvent(ChangeType.opened, chat), this);
		return chat;
	}

	protected final void closeChat(final PairChat chat) {
		chats.remove(chat);
		eventBus.fireEventFromSource(new PairChatChangedEvent(ChangeType.closed, chat), this);
	}
	
	@Override
	public final PairChat getChat(final XmppURI uri) {
		for (final PairChat chat : chats) {
			if (chat.getURI().equalsNoResource(uri))
				return chat;
		}
		return null;
	}
	
	@Override
	public final Collection<PairChat> getChats() {
		return Collections.unmodifiableCollection(chats);
	}

}
