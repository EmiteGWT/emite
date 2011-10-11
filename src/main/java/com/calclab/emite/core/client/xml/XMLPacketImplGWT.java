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

package com.calclab.emite.core.client.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.xml.client.Attr;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

public final class XMLPacketImplGWT implements XMLPacket {

	private final Document document;
	private final Element element;

	protected XMLPacketImplGWT(final String name) {
		document = XMLParser.createDocument();
		element = document.createElement(name);
	}

	protected XMLPacketImplGWT(final String name, final String namespace) {
		document = XMLParser.createDocument();
		element = document.createElement(name);
		element.setAttribute("xmlns", namespace);
	}

	protected XMLPacketImplGWT(final Element element) {
		document = element.getOwnerDocument();
		this.element = element;
	}

	@Override
	public String getTagName() {
		return element.getTagName();
	}

	@Override
	public String getNamespace() {
		return element.getNamespaceURI();
	}

	@Override
	public XMLPacket getParent() {
		final Node parent = element.getParentNode();
		if (parent == null || !(parent instanceof Element))
			return null;

		return new XMLPacketImplGWT((Element) parent);
	}

	@Override
	public Map<String, String> getAttributes() {
		final Map<String, String> result = new HashMap<String, String>();
		final NamedNodeMap attribs = element.getAttributes();
		for (int i = 0; i < attribs.getLength(); i++) {
			final Attr attrib = (Attr) attribs.item(i);
			result.put(attrib.getName(), attrib.getValue());
		}
		return result;
	}

	@Override
	public String getAttribute(final String name) {
		if (!element.hasAttribute(name))
			return null;

		return element.getAttribute(name);
	}

	@Override
	public void setAttribute(final String name, final String value) {
		if (value != null) {
			element.setAttribute(name, value);
		} else {
			element.removeAttribute(name);
		}
	}

	@Override
	public boolean hasChild(final String name) {
		return hasChild(name, "*");
	}

	@Override
	public boolean hasChild(final String name, final String namespace) {
		return getFirstChild(name, namespace) != null;
	}

	@Override
	public XMLPacket addChild(final String name) {
		return addChild(name, null);
	}

	@Override
	public XMLPacket addChild(final String name, final String namespace) {
		final Element newElement = document.createElement(name);
		if (namespace != null) {
			newElement.setAttribute("xmlns", namespace);
		}
		element.appendChild(newElement);
		return new XMLPacketImplGWT(newElement);
	}

	@Override
	public XMLPacket addChild(final HasXML child) {
		final Element newElement = (Element) document.importNode(((XMLPacketImplGWT) child.getXML()).element, true);
		element.appendChild(newElement);
		return new XMLPacketImplGWT(newElement);
	}

	@Override
	public XMLPacket getFirstChild(final String name) {
		return getFirstChild(name, "*");
	}

	@Override
	public XMLPacket getFirstChild(final String name, final String namespace) {
		final NodeList nodes = element.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			final Node node = nodes.item(i);
			if (node.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}

			final Element element = (Element) node;
			if (!"*".equals(name) && !name.equals(element.getTagName())) {
				continue;
			}

			if (!"*".equals(namespace) && !namespace.equals(element.getNamespaceURI())) {
				continue;
			}

			return new XMLPacketImplGWT(element);
		}

		return null;
	}

	@Override
	public XMLPacket getFirstChild(final XMLMatcher matcher) {
		for (final XMLPacket packet : getChildren()) {
			if (matcher.matches(packet))
				return packet;
		}

		return null;
	}

	@Override
	public List<XMLPacket> getChildren() {
		return getChildren("*", "*");
	}

	@Override
	public List<XMLPacket> getChildren(final String name) {
		return getChildren(name, "*");
	}

	@Override
	public List<XMLPacket> getChildren(final String name, final String namespace) {
		final List<XMLPacket> result = new ArrayList<XMLPacket>();

		final NodeList nodes = element.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			final Node node = nodes.item(i);
			if (node.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}

			final Element element = (Element) node;
			if (!"*".equals(name) && !name.equals(element.getTagName())) {
				continue;
			}

			if (!"*".equals(namespace) && !namespace.equals(element.getNamespaceURI())) {
				continue;
			}

			result.add(new XMLPacketImplGWT(element));
		}

		return result;
	}

	@Override
	public List<XMLPacket> getChildren(final XMLMatcher matcher) {
		final List<XMLPacket> result = new ArrayList<XMLPacket>();

		for (final XMLPacket packet : getChildren()) {
			if (matcher.matches(packet)) {
				result.add(packet);
			}
		}

		return result;
	}

	@Override
	public void removeChild(final HasXML child) {
		if (child instanceof XMLPacketImplGWT) {
			element.removeChild(((XMLPacketImplGWT) child.getXML()).element);
		}
	}

	@Override
	public String getText() {
		final NodeList nodes = element.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			final Node child = nodes.item(i);
			if (child.getNodeType() == Node.TEXT_NODE)
				return child.getNodeValue();
		}

		return null;
	}

	@Override
	public String getChildText(final String name) {
		return getChildText(name, "*");
	}

	@Override
	public String getChildText(final String name, final String namespace) {
		final XMLPacket child = getFirstChild(name, namespace);
		if (child == null)
			return null;

		return child.getText();
	}

	@Override
	public void setText(final String text) {
		// TODO: remove ALL children?
		final NodeList nodes = element.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			final Node child = nodes.item(i);
			if (child.getNodeType() == Node.TEXT_NODE) {
				element.removeChild(child);
			}
		}
		if (text != null) {
			element.appendChild(document.createTextNode(text));
		}
	}

	@Override
	public void setChildText(final String name, final String text) {
		setChildText(name, null, text);
	}

	@Override
	public void setChildText(final String name, final String namespace, final String text) {
		XMLPacket child = getFirstChild(name, namespace != null ? namespace : "*");
		if (child == null) {
			child = addChild(name, namespace);
		}

		child.setText(text);
	}

	@Override
	public XMLPacket getXML() {
		return this;
	}

	// TODO: does this work on all browsers?
	@Override
	public String toString() {
		return element.toString();
	}

	/*@Override
	public String toString() {
		final StringBuilder buffer = new StringBuilder();
		xmlToString(this, buffer);
		return buffer.toString();
	}

	private static void xmlToString(final XMLPacket root, final StringBuilder buffer) {
		final String name = root.getTagName();
		buffer.append("<").append(name);
		final Map<String, String> attributes = root.getAttributes();
		for (final String key : attributes.keySet()) {
			final String value = attributes.get(key);
			if (value != null) {
				buffer.append(" ").append(key).append("=\"");
				buffer.append(TextUtils.escape(value)).append("\"");
			}
		}

		final String rootText = root.getText();
		if (rootText != null) {
			buffer.append(">");
			buffer.append(TextUtils.escape(rootText));
			buffer.append("</").append(name).append(">");
		} else {
			final List<XMLPacket> children = root.getChildren();
			if (children.size() > 0) {
				buffer.append(">");
				for (final XMLPacket child : children) {
					xmlToString(child, buffer);
				}
				buffer.append("</").append(name).append(">");
			} else {
				buffer.append(" />");
			}
		}
	}*/

}
