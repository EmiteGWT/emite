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

package com.calclab.emite.xep.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.calclab.emite.base.xml.XMLBuilder;
import com.calclab.emite.base.xml.XMLPacket;
import com.calclab.emite.core.IQCallback;
import com.calclab.emite.core.XmppURI;
import com.calclab.emite.core.session.SessionStatus;
import com.calclab.emite.core.session.XmppSession;
import com.calclab.emite.core.stanzas.IQ;
import com.calclab.emite.xep.dataforms.Field;
import com.calclab.emite.xep.dataforms.Form;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class SearchManagerImpl implements SearchManager {

	private static final String SHOULD_BE_CONNECTED = "You should be connected before use this service.";

	private final XmppSession session;
	private XmppURI host;

	@Inject
	protected SearchManagerImpl(final XmppSession session) {
		this.session = session;
	}

	@Override
	public void requestSearchFields(final ResultListener<SearchFields> listener) {
		requestGenericSearchFields(new IQCallback() {
			@Override
			public void onIQSuccess(final IQ iq) {
				listener.onSuccess(processFieldsResults(session.getCurrentUserURI(), iq.getQuery("jabber:iq:search")));
			}

			@Override
			public void onIQFailure(final IQ iq) {
				// TODO
				listener.onFailure(null);
			}
		});
	}

	@Override
	public void requestSearchForm(final ResultListener<Form> listener) {
		requestGenericSearchFields(new IQCallback() {
			@Override
			public void onIQSuccess(final IQ iq) {
				final XMLPacket xSearch = iq.getExtension("x", "jabber:x:data");
				if (xSearch != null) {
					listener.onSuccess(Form.fromXML(xSearch));
					return;
				}

				// This is not a extended search. Try to create a form
				// with returned fields
				final SearchFields fieldResults = processFieldsResults(session.getCurrentUserURI(), iq.getQuery("jabber:iq:search"));
				final Form form = new Form(Form.Type.form);
				form.addInstruction(fieldResults.getInstructions());
				for (final String fieldName : fieldResults.getFieldNames()) {
					final Field field = new Field();
					field.setType(Field.Type.TEXT_SINGLE);
					field.setVar(fieldName);
					form.addField(field);
				}
				listener.onSuccess(form);
			}

			@Override
			public void onIQFailure(final IQ iq) {
				// TODO
				listener.onFailure(null);
			}
		});
	}

	@Override
	public void search(final Form searchForm, final ResultListener<Form> listener) {
		searchGeneric(Arrays.asList(searchForm.getXML()), new IQCallback() {
			@Override
			public void onIQSuccess(final IQ iq) {
				listener.onSuccess(Form.fromXML(iq.getXML()));
			}

			@Override
			public void onIQFailure(final IQ iq) {
				// TODO
				listener.onFailure(null);
			}
		});
	}

	@Override
	public void search(final Map<String, String> query, final ResultListener<List<SearchResultItem>> listener) {
		final List<XMLPacket> queryPacket = new ArrayList<XMLPacket>();
		for (final String field : query.keySet()) {
			queryPacket.add(XMLBuilder.create(field).text(query.get(field)).getXML());
		}
		searchGeneric(queryPacket, new IQCallback() {
			@Override
			public void onIQSuccess(final IQ iq) {
				listener.onSuccess(processResults(session.getCurrentUserURI(), iq.getQuery("jabber:iq:search")));
			}

			@Override
			public void onIQFailure(final IQ iq) {
				// TODO
				listener.onFailure(null);
			}
		});
	}

	@Override
	public void setHost(final XmppURI host) {
		this.host = host;
	}

	protected static List<SearchResultItem> processResults(final XmppURI from, final XMLPacket query) {
		final List<SearchResultItem> result = new ArrayList<SearchResultItem>();
		for (final XMLPacket child : query.getChildren("item")) {
			final SearchResultItem searchResultItem = SearchResultItem.parse(child);
			result.add(searchResultItem);
		}
		return result;
	}

	private static SearchFields processFieldsResults(final XmppURI from, final XMLPacket query) {
		final SearchFields fields = new SearchFields();
		for (final XMLPacket child : query.getChildren()) {
			if (!child.getTagName().equals("instructions")) {
				fields.add(child.getTagName());
			} else {
				fields.setInstructions(child.getText());
			}
		}
		return fields;
	}

	// FIXME: change listener for handler
	private void requestGenericSearchFields(final IQCallback callback) {
		if (SessionStatus.ready.equals(session.getStatus())) {
			final IQ iq = new IQ(IQ.Type.get);
			iq.setTo(host);
			iq.getXML().setAttribute("xml:lang", "en");
			iq.addChild("query", "jabber:iq:search");

			session.sendIQ("search", iq, callback);
		} else
			throw new RuntimeException(SHOULD_BE_CONNECTED);
	}

	private void searchGeneric(final List<XMLPacket> queryChilds, final IQCallback callback) {
		if (SessionStatus.ready.equals(session.getStatus())) {
			final IQ iq = new IQ(IQ.Type.set);
			iq.setTo(host);
			iq.getXML().setAttribute("xml:lang", "en");
			final XMLPacket queryPacket = iq.addChild("query", "jabber:iq:search");
			for (final XMLPacket child : queryChilds) {
				queryPacket.addChild(child);
			}

			session.sendIQ("search", iq, callback);
		} else
			throw new RuntimeException(SHOULD_BE_CONNECTED);
	}
}
