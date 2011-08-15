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

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.services.ConnectorCallback;
import com.calclab.emite.core.client.services.ConnectorException;
import com.calclab.emite.core.client.services.ScheduledAction;
import com.calclab.emite.core.client.services.Services;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class GWTServices implements Services {
	
	@Inject
	public GWTServices() {
	}

	@Override
	public long getCurrentTime() {
		return GWTScheduler.getCurrentTime();
	}

	@Override
	public void schedule(final int msecs, final ScheduledAction action) {
		GWTScheduler.schedule(msecs, action);
	}

	@Override
	public void send(final String httpBase, final String request, final ConnectorCallback listener) throws ConnectorException {
		GWTConnector.send(httpBase, request, listener);
	}

	@Override
	public String toString(final IPacket packet) {
		return GWTXMLService.toString(packet);
	}

	@Override
	public IPacket toXML(final String xml) {
		return GWTXMLService.toXML(xml);
	}

}
