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

package com.calclab.emite.xtesting.services;

import java.text.MessageFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.calclab.emite.core.client.services.ConnectorCallback;

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

	public synchronized void send(final String httpBase, final String xml, final ConnectorCallback callback) {

		sendService.execute(createSendAction(httpBase, xml, callback));
	}

	protected void debug(final String pattern, final Object... arguments) {
		// FIXME
		MessageFormat.format(pattern, arguments);
	}

	private Runnable createResponseAction(final String xml, final ConnectorCallback callback, final String id, final int status, final String response) {
		final Runnable runnable = new Runnable() {
			@Override
			public void run() {
				if (status == HttpStatus.SC_OK) {
					System.out.println("RECEIVED: " + response);
					debug("Connector [{0}] receive: {1}", id, response);
					callback.onResponseReceived(status, response, xml);
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
			@Override
			public void run() {
				final String id = HttpConnectorID.getNext();
				debug("Connector [{0}] send: {1}", id, xml);
				final HttpClient client = new DefaultHttpClient();
				int status = 0;
				String responseString = null;
				final HttpPost post = new HttpPost(httpBase);

				try {
					post.setEntity(new StringEntity(xml, "utf-8"));
					System.out.println("SENDING: " + xml);
					final HttpResponse response = client.execute(post);
					responseString = EntityUtils.toString(response.getEntity());
				} catch (final Exception e) {
					callback.onError(xml, e);
					e.printStackTrace();
				}

				try {
					post.setEntity(new StringEntity(xml, "text/xml"));
					System.out.println("SENDING: " + xml);
					HttpResponse response = client.execute(post);
					responseString = EntityUtils.toString(response.getEntity());
					status = response.getStatusLine().getStatusCode();
				} catch (final Exception e) {
					callback.onError(xml, e);
					e.printStackTrace();
				}

				receiveService.execute(createResponseAction(xml, callback, id, status, responseString));
			}
		};
	}
}
