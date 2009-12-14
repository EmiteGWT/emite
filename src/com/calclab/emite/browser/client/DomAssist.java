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
package com.calclab.emite.browser.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.DOM;

/**
 * A helper class to perform typical DOM manipulations related with elements and
 * its properties
 */
public class DomAssist {

    public DomAssist() {
    }

    /**
     * Remove all the childs of the given element
     * 
     * @param element
     *            the element
     */
    public void clearElement(final Element element) {
	final NodeList<Node> childs = element.getChildNodes();
	for (int index = 0, total = childs.getLength(); index < total; index++) {
	    element.removeChild(childs.getItem(index));
	}

    }

    /**
     * Retrieves all the div on the page that matches the given class
     * 
     * @param cssClass
     *            the css class to match
     * @return a list of elements
     */
    public ArrayList<Element> findElementsByClass(final String cssClass) {
	GWT.log(("Finding elements div." + cssClass), null);
	final ArrayList<Element> elements = new ArrayList<Element>();
	final Document document = Document.get();
	final NodeList<Element> divs = document.getElementsByTagName("div");
	for (int index = 0, total = divs.getLength(); index < total; index++) {
	    final Element div = divs.getItem(index);
	    final String[] classes = div.getClassName().split(" ");
	    for (final String className : classes) {
		if (cssClass.equals(className)) {
		    elements.add(div);
		}
	    }
	}
	return elements;
    }

    /**
     * Get the value of meta information writen in the html page. The meta
     * information is a html tag with name of meta usually placed inside the the
     * head section with two attributes: id and content. For example:
     * 
     * <code>&lt;meta id="name" value="userName" /&gt;</code>
     * 
     * 
     * 
     * @param id
     *            the 'id' value of the desired meta tag
     * @param isRequired
     *            if isRequired is true, this method throws a RuntimeException
     *            if the meta tag is not found
     * @return the value of the attribute 'value'
     */
    public String getMeta(final String id, final boolean isRequired) {
	String value = null;
	final Element element = DOM.getElementById(id);
	if (element != null) {
	    value = element.getPropertyString("content");
	    GWT.log(("Meta: " + id + ": " + value), null);
	}
	if (isRequired && value == null) {
	    throw new RuntimeException("Required meta-attribute " + id + " not found.");
	}

	return value;
    }

    /**
     * The same as getMeta(string, boolean) but returns the defaultValue if no
     * meta is found
     * 
     * @param id
     *            the 'id' value of the desired meta tag
     * @param defaultValue
     *            the default value to be return if no meta found
     * @return the value of the 'value' attribute or defaultValue if not found
     */
    public String getMeta(final String id, final String defaultValue) {
	final String value = getMeta(id, false);
	return value != null ? value : defaultValue;
    }

    /**
     * Given an DOM element and a list of parameters, returns a HashMap with the
     * paramName associated to the value of this parameter defined in the
     * element.
     * 
     * The optional prefix is added to the paramName before extracting the
     * value.
     * 
     * @param element
     *            the element to be inspected
     * @param paramNames
     *            the desired paramNames
     * @param prefix
     *            The string added as prefix to the paramName before get the
     *            value. It can be null
     * @return a HashMap that associates each paramName with the value in the
     *         element (or null if nothing found)
     */
    public HashMap<String, String> getProperties(final Element element, final String[] paramNames, final String prefix) {
	final String before = (prefix == null ? "" : prefix);
	final HashMap<String, String> properties = new HashMap<String, String>();
	if (paramNames != null) {
	    for (final String name : paramNames) {
		GWT.log(("Param name of widget: " + name), null);
		final String value = element.getAttribute(before + name);
		GWT.log(("Value: " + value), null);
		properties.put(name, value);
	    }
	}
	return properties;
    }

}
