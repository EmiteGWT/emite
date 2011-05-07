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
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class PingPongWidget extends VerticalPanel implements PingPongDisplay {

	private final Button login;
	private final Button logout;
	private final VerticalPanel output;
	private final VerticalPanel header;
	private final Button clear;
	private final Label currentUser;
	private final Label sessionStatus;
	private final int maxOutput;
	private boolean hideEvents;
	private final FlowPanel buttons;

	public PingPongWidget() {
		maxOutput = 500;
		hideEvents = true;
		header = new VerticalPanel();
		add(header);
		currentUser = new Label("no user yet.");
		sessionStatus = new Label("no session status yet.");
		final FlowPanel status = new FlowPanel();
		status.add(currentUser);
		status.add(sessionStatus);
		add(status);
		login = new Button("Login");
		logout = new Button("Logout");
		clear = new Button("Clear");
		final CheckBox hideEventsCheck = new CheckBox("Hide events");
		buttons = new FlowPanel();
		buttons.add(login);
		buttons.add(logout);
		buttons.add(clear);
		buttons.add(hideEventsCheck);
		add(buttons);
		output = new VerticalPanel();
		add(output);

		hideEventsCheck.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(final ValueChangeEvent<Boolean> event) {
				hideEvents = event.getValue();
				final int widgetCount = output.getWidgetCount();
				for (int index = 0; index < widgetCount; index++) {
					final Widget w = output.getWidget(index);
					if (isEventWidget(w)) {
						w.setVisible(!hideEvents);
					}
				}
			}
		});
		hideEventsCheck.setValue(hideEvents);
	}

	@Override
	public void addAction(final String label, final ClickHandler handler) {
		buttons.add(new Button(label, handler));
	}

	@Override
	public void addClearClickHandler(final ClickHandler handler) {
		clear.addClickHandler(handler);
	}

	@Override
	public void addLoginClickHandler(final ClickHandler handler) {
		login.addClickHandler(handler);
	}

	@Override
	public void addLogoutClickHandler(final ClickHandler handler) {
		logout.addClickHandler(handler);
	}

	@Override
	public Widget asWidget() {
		return this;
	}

	@Override
	public void clearOutput() {
		output.clear();
	}

	@Override
	public Label getCurrentUser() {
		return currentUser;
	}

	@Override
	public Label getSessionStatus() {
		return sessionStatus;
	}

	@Override
	public void print(final String text, final String style) {
		final int widgetCount = output.getWidgetCount();
		if (widgetCount == maxOutput) {
			output.remove(widgetCount - 1);
		}
		final Label label = new Label(text);
		label.addStyleName(style);
		if (isEventWidget(label)) {
			label.setVisible(!hideEvents);
		}
		output.insert(label, 0);
	}

	@Override
	public void printHeader(final String text, final String style) {
		final Label label = new Label(text);
		label.addStyleName(style);
		header.add(label);

	}

	protected boolean isEventWidget(final Widget w) {
		final String styleName = w.getStyleName();
		return styleName.contains(Style.eventBus) || styleName.contains(Style.stanzaReceived) || styleName.contains(Style.stanzaSent);
	}
}
