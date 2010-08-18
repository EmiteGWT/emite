package com.calclab.emite.xxamples.pingpong.client;

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
