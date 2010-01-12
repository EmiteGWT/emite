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
package com.calclab.emite.xtesting.services;

import java.text.MessageFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;

import com.calclab.emite.core.client.services.ConnectorCallback;
import com.calclab.emite.core.client.services.ConnectorException;

public class HttpConnector {

    private static class HttpConnectorID {
	private static int id = 0;

	public static String getNext() {
	    id++;
	    return String.valueOf(id);
	}

    }

    private final ExecutorService sendService;
    private final ExecutorService receiveService;

    public HttpConnector() {
	sendService = Executors.newCachedThreadPool();
	receiveService = Executors.newFixedThreadPool(1);
    }

    public synchronized void send(final String httpBase, final String xml, final ConnectorCallback callback)
	    throws ConnectorException {

	sendService.execute(createSendAction(httpBase, xml, callback));
    }

    protected void debug(final String pattern, final Object... arguments) {
	// FIXME
	MessageFormat.format(pattern, arguments);
    }

    private Runnable createResponseAction(final String xml, final ConnectorCallback callback, final String id,
	    final int status, final String response) {
	final Runnable runnable = new Runnable() {
	    public void run() {
		if (status == HttpStatus.SC_OK) {
		    System.out.println("RECEIVED: " + response);
		    debug("Connector [{0}] receive: {1}", id, response);
		    callback.onResponseReceived(status, response);
		} else {
		    debug("Connector [{0}] bad status: {1}", id, status);
		    callback.onError(xml, new Exception("bad http status " + status));
		}

	    }

	};
	return runnable;
    }

    private Runnable createSendAction(final String httpBase, final String xml, final ConnectorCallback callback) {
	return new Runnable() {
	    public void run() {
		final String id = HttpConnectorID.getNext();
		debug("Connector [{0}] send: {1}", id, xml);
		final HttpClientParams params = new HttpClientParams();
		params.setConnectionManagerTimeout(10000);
		final HttpClient client = new HttpClient(params);
		int status = 0;
		String response = null;
		final PostMethod post = new PostMethod(httpBase);

		try {
		    post.setRequestEntity(new StringRequestEntity(xml, "text/xml", "utf-8"));
		    System.out.println("SENDING: " + xml);
		    status = client.executeMethod(post);
		    response = post.getResponseBodyAsString();
		} catch (final Exception e) {
		    callback.onError(xml, e);
		    e.printStackTrace();
		} finally {
		    post.releaseConnection();
		}

		receiveService.execute(createResponseAction(xml, callback, id, status, response));
	    }
	};
    }
}
