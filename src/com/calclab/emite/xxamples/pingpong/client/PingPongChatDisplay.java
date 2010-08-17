package com.calclab.emite.xxamples.pingpong.client;

import com.google.gwt.event.dom.client.ClickHandler;

public interface PingPongChatDisplay {
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
    }

    void addClearClickHandler(ClickHandler handler);

    public void addLoginClickHandler(ClickHandler handler);

    public void addLogoutClickHandler(ClickHandler handler);

    void clearOutput();

    public void print(String message, String style);

    void printHeader(String text, String style);

}
