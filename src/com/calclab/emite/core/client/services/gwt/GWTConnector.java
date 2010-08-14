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
package com.calclab.emite.core.client.services.gwt;

import java.util.ArrayList;
import java.util.List;

import com.calclab.emite.core.client.services.ConnectorCallback;
import com.calclab.emite.core.client.services.ConnectorException;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;

public class GWTConnector {

    static List<Request> activeRequests = new ArrayList<Request>();

    static {
	// On close it cancels all the pending requests except the "terminate"
	// request
	Window.addCloseHandler(new CloseHandler<Window>() {
	    @Override
	    public void onClose(final CloseEvent<Window> event) {
		int i = activeRequests.size() - 2;
		GWT.log("Cancelling " + (i + 1) + " pending requests.");
		for (; i >= 0; i--) {
		    activeRequests.get(i).cancel();
		}
		GWT.log("Cancelled all requests.");
	    }
	});
    }

    public static void send(final String httpBase, final String request, final ConnectorCallback listener)
	    throws ConnectorException {
	final RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, httpBase);
	builder.setHeader("Content-Type", "text/xml; charset=utf-8");
	// TODO : Hard coded timeout to 6s, but we should set it to the wait + a
	// delta
	// builder.setTimeoutMillis(6000);
	try {
	    final Request req = builder.sendRequest(request, new RequestCallback() {
		public void onError(final Request req, final Throwable throwable) {
		    GWT.log(("GWT CONNECTOR ERROR: " + throwable), null);
		    activeRequests.remove(req);
		    listener.onError(request, throwable);
		}

		public void onResponseReceived(final Request req, final Response res) {
		    activeRequests.remove(req);
		    listener.onResponseReceived(res.getStatusCode(), res.getText(), request);
		}
	    });
	    activeRequests.add(req);
	} catch (final RequestException e) {
	    throw new ConnectorException(e.getMessage());
	} catch (final Exception e) {
	    GWT.log(("Some GWT connector exception: " + e), null);
	    throw new ConnectorException(e.getMessage());
	}
    }

}
