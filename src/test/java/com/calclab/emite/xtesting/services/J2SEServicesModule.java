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

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.services.ConnectorCallback;
import com.calclab.emite.core.client.services.ConnectorException;
import com.calclab.emite.core.client.services.ScheduledAction;
import com.calclab.emite.core.client.services.Services;

public class J2SEServicesModule implements Services {
	private final HttpConnector connector;

	private final ThreadScheduler scheduler;
	private final TigaseXMLService xmler;

	public J2SEServicesModule() {
		connector = new HttpConnector();
		scheduler = new ThreadScheduler();
		xmler = new TigaseXMLService();
	}

	@Override
	public long getCurrentTime() {
		return scheduler.getCurrentTime();
	}

	/*
	 * public void onInstall(final Container container) {
	 * container.removeProvider(Services.class);
	 * container.registerProvider(null, Services.class, new Provider<Services>()
	 * { public Services get() { return J2SEServicesModule.this; } }); }
	 */

	@Override
	public void schedule(final int msecs, final ScheduledAction action) {
		scheduler.schedule(msecs, action);
	}

	@Override
	public void send(final String httpBase, final String xml, final ConnectorCallback listener) throws ConnectorException {
		connector.send(httpBase, xml, listener);
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
