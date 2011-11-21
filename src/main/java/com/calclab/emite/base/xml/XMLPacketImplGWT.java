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

package com.calclab.emite.base.xml;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gwt.xml.client.Attr;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;
import com.google.gwt.xml.client.impl.DOMParseException;

/**
 * GWT implementation of XMLPacket
 */
public final class XMLPacketImplGWT implements XMLPacket {

	private final Document document;
	private final Element element;

	protected XMLPacketImplGWT(final String name) {
		this(name, null);
	}

	protected XMLPacketImplGWT(final String name, @Nullable final String namespace) {
		document = XMLParser.createDocument();
		element = document.createElement(name);
		if (namespace != null) {
			element.setAttribute("xmlns", namespace);
		}
		document.appendChild(element);
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
	public XMLPacket getFirstParent() {
		return new XMLPacketImplGWT(document.getDocumentElement());
	}

	@Override
	public final boolean hasAttribute(final String name) {
		return element.hasAttribute(checkNotNull(name));
	}

	@Override
	public ImmutableMap<String, String> getAttributes() {
		final ImmutableMap.Builder<String, String> result = ImmutableMap.builder();
		final NamedNodeMap attribs = element.getAttributes();
		for (int i = 0; i < attribs.getLength(); i++) {
			final Attr attrib = (Attr) attribs.item(i);
			result.put(attrib.getName(), attrib.getValue());
		}
		return result.build();
	}

	@Override
	public String getAttribute(final String name) {
		checkNotNull(name);

		if (!element.hasAttribute(name))
			return null;

		return element.getAttribute(name);
	}

	@Override
	public void setAttribute(final String name, final String value) {
		checkNotNull(name);

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
		final Element newElement = document.createElement(checkNotNull(name));
		if (namespace != null) {
			newElement.setAttribute("xmlns", namespace);
		}
		element.appendChild(newElement);
		return new XMLPacketImplGWT(newElement);
	}

	@Override
	public XMLPacket addChild(final HasXML child) {
		checkArgument(checkNotNull(child.getXML()) instanceof XMLPacketImplGWT);

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
		checkNotNull(name);
		checkNotNull(namespace);

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
	public XMLPacket getFirstChild(final Predicate<XMLPacket> matcher) {
		checkNotNull(matcher);

		for (final XMLPacket packet : getChildren()) {
			if (matcher.apply(packet))
				return packet;
		}

		return null;
	}

	@Override
	public ImmutableList<XMLPacket> getChildren() {
		return getChildren("*", "*");
	}

	@Override
	public ImmutableList<XMLPacket> getChildren(final String name) {
		return getChildren(name, "*");
	}

	@Override
	public ImmutableList<XMLPacket> getChildren(final String name, final String namespace) {
		checkNotNull(name);
		checkNotNull(namespace);

		final ImmutableList.Builder<XMLPacket> result = ImmutableList.builder();

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

		return result.build();
	}

	@Override
	public ImmutableList<XMLPacket> getChildren(final Predicate<XMLPacket> matcher) {
		checkNotNull(matcher);
		
		final ImmutableList.Builder<XMLPacket> result = ImmutableList.builder();

		for (final XMLPacket packet : getChildren()) {
			if (matcher.apply(packet)) {
				result.add(packet);
			}
		}

		return result.build();
	}

	@Override
	public void removeChild(final HasXML child) {
		checkArgument(child.getXML() instanceof XMLPacketImplGWT);
		
		element.removeChild(((XMLPacketImplGWT) child.getXML()).element);
	}

	@Override
	public String getText() {
		final StringBuilder result = new StringBuilder();
		final NodeList nodes = element.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			final Node child = nodes.item(i);
			if (child.getNodeType() == Node.TEXT_NODE)
				result.append(child.getNodeValue());
		}

		return result.toString();
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
		if (!Strings.isNullOrEmpty(text)) {
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

	/**
	 * Parses a string into a XMLPacket.
	 * 
	 * @param xml the string to parse
	 * @return the resulting packet
	 */
	public static XMLPacket fromString(final String xml) {
		try {
			return new XMLPacketImplGWT(XMLParser.parse(xml).getDocumentElement());
		} catch (final DOMParseException e) {
			return null;
		}
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
