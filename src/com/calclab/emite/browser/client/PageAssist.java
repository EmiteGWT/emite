package com.calclab.emite.browser.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;

public class PageAssist {

    /**
     * Get the value of meta information writen in the html page. The meta
     * information is a html tag with name of meta usually placed inside the the
     * head section with two attributes: id and content. For example:
     * 
     * <code>&lt;meta id="name" value="userName" /&gt;</code>
     * 
     * @param id
     *            the 'id' value of the desired meta tag
     * @return the value of the attribute 'value' or null if not found
     */
    public static final String getMeta(final String id) {
	String value = null;
	final Element element = DOM.getElementById(id);
	if (element != null) {
	    value = element.getPropertyString("content");
	}
	return value;
    }
}
