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

package com.calclab.emite.core.client.util;

import java.util.List;
import java.util.logging.Logger;

import com.google.common.collect.Lists;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;

public class Platform {

	private static final Logger logger = Logger.getLogger(Platform.class.getName());

	private static final List<Request> requests;
	
	static {
		requests = Lists.newArrayList();
		
		// On close it cancels all the pending requests except the "terminate" request
		Window.addWindowClosingHandler(new Window.ClosingHandler() {
			@Override
			public void onWindowClosing(final Window.ClosingEvent event) {
				int i = requests.size() - 2;
				logger.finer("Cancelling " + (i + 1) + " pending requests.");
				for (; i >= 0; i--) {
					requests.get(i).cancel();
				}
				logger.finer("Cancelled all requests.");
			}
		});
	}

	public static final void schedule(final int msecs, final ScheduledAction action) {
		new Timer() {
			@Override
			public void run() {
				action.run();
			}
		}.schedule(msecs);
	}

	public static final void send(final String httpBase, final String request, final ConnectorCallback listener) throws ConnectorException {
		final RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, httpBase);
		builder.setHeader("Content-Type", "text/xml; charset=utf-8");
		builder.setHeader("Cache-Control", "no-cache");
		builder.setHeader("Pragma", "no-cache");
		// TODO : Hard coded timeout to 6s, but we should set it to the wait + a delta
		// builder.setTimeoutMillis(6000);
		try {
			final Request req = builder.sendRequest(request, new RequestCallback() {
				@Override
				public void onError(final Request req, final Throwable throwable) {
					logger.severe("GWT CONNECTOR ERROR: " + throwable.getMessage());
					requests.remove(req);
					listener.onResponseError(request, throwable);
				}

				@Override
				public void onResponseReceived(final Request req, final Response res) {
					requests.remove(req);
					listener.onResponseReceived(res.getStatusCode(), res.getText(), request);
				}
			});
			requests.add(req);
		} catch (final RequestException e) {
			throw new ConnectorException(e.getMessage());
		} catch (final Exception e) {
			logger.severe("Some GWT connector exception: " + e.getMessage());
			throw new ConnectorException(e.getMessage());
		}
	}

}
