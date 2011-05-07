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

package com.calclab.emite.example.pingpong.client;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

public interface PingPongDisplay {
	public static class Style {
		public static final String title = "title";
		public static final String error = "error";
		public static final String important = "important";
		public static final String received = "received";
		public static final String session = "session";
		public static final String info = "info";
		public static final String sent = "sent";
		public static final String stanzaReceived = "stanzaReceived";
		public static final String stanzaSent = "stanzaSent";
		public static final String event = "event";
		public static final String eventBus = "eventBus";
	}

	public void addAction(String label, ClickHandler handler);

	public void addClearClickHandler(ClickHandler handler);

	public void addLoginClickHandler(ClickHandler handler);

	public void addLogoutClickHandler(ClickHandler handler);

	public Widget asWidget();

	public void clearOutput();

	public HasText getCurrentUser();

	public HasText getSessionStatus();

	public void print(String message, String style);

	public void printHeader(String text, String style);

}
