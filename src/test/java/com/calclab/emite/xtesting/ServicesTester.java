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

package com.calclab.emite.xtesting;

import java.util.ArrayList;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.services.ConnectorCallback;
import com.calclab.emite.core.client.services.ConnectorException;
import com.calclab.emite.core.client.services.ScheduledAction;
import com.calclab.emite.core.client.services.Services;
import com.calclab.emite.xtesting.services.TigaseXMLService;

/**
 * Object of this classes are used to test against services
 * 
 */
public class ServicesTester implements Services {
	public static class Request {
		public final String httpBase;
		public final String request;
		public final ConnectorCallback listener;

		public Request(final String httpBase, final String request, final ConnectorCallback listener) {
			this.httpBase = httpBase;
			this.request = request;
			this.listener = listener;
		}

	}

	public static final TigaseXMLService xmler = TigaseXMLService.instance;
	private final ArrayList<Request> requests;

	public ServicesTester() {
		requests = new ArrayList<Request>();
	}

	@Override
	public long getCurrentTime() {
		return 0;
	}

	public IPacket getSentPacket(final int index) {
		final String request = requests.get(index).request;
		return xmler.toXML(request);
	}

	public int requestSentCount() {
		return requests.size();
	}

	@Override
	public void schedule(final int msecs, final ScheduledAction action) {
	}

	@Override
	public void send(final String httpBase, final String request, final ConnectorCallback listener) throws ConnectorException {
		requests.add(new Request(httpBase, request, listener));
	}

	@Override
	public String toString(final IPacket packet) {
		return xmler.toString(packet);
	}

	@Override
	public IPacket toXML(final String xml) {
		return xmler.toXML(xml);
	}

}
