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

package com.calclab.emite.core.client.services.gwt;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.calclab.emite.core.client.services.ConnectorCallback;
import com.calclab.emite.core.client.services.ConnectorException;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;

public class GWTConnector {

	private static final Logger logger = Logger.getLogger(GWTConnector.class.getName());
	
	static List<Request> activeRequests = new ArrayList<Request>();

	static {
		// On close it cancels all the pending requests except the "terminate"
		// request
		Window.addCloseHandler(new CloseHandler<Window>() {
			@Override
			public void onClose(final CloseEvent<Window> event) {
				int i = activeRequests.size() - 2;
				logger.finer("Cancelling " + (i + 1) + " pending requests.");
				for (; i >= 0; i--) {
					activeRequests.get(i).cancel();
				}
				logger.finer("Cancelled all requests.");
			}
		});
	}

	public static void send(final String httpBase, final String request, final ConnectorCallback listener) throws ConnectorException {
		final RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, httpBase);
		builder.setHeader("Content-Type", "text/xml; charset=utf-8");
		builder.setHeader("Cache-Control", "no-cache");
		builder.setHeader("Pragma", "no-cache");
		// TODO : Hard coded timeout to 6s, but we should set it to the wait + a
		// delta
		// builder.setTimeoutMillis(6000);
		try {
			final Request req = builder.sendRequest(request, new RequestCallback() {
				@Override
				public void onError(final Request req, final Throwable throwable) {
					logger.severe("GWT CONNECTOR ERROR: " + throwable.getMessage());
					activeRequests.remove(req);
					listener.onError(request, throwable);
				}

				@Override
				public void onResponseReceived(final Request req, final Response res) {
					activeRequests.remove(req);
					listener.onResponseReceived(res.getStatusCode(), res.getText(), request);
				}
			});
			activeRequests.add(req);
		} catch (final RequestException e) {
			throw new ConnectorException(e.getMessage());
		} catch (final Exception e) {
			logger.severe("Some GWT connector exception: " + e.getMessage());
			throw new ConnectorException(e.getMessage());
		}
	}

}
