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

import java.io.ByteArrayInputStream;
import java.io.StringWriter;

import javax.annotation.Nullable;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * Java 6 implementation of XMLPacket.
 */
public final class XMLPacketImpl implements XMLPacket {

	private static final DocumentBuilder docBuilder;
	private static final Transformer transformer;

	static {
		try {
			final DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			docBuilderFactory.setIgnoringElementContentWhitespace(true);
			docBuilderFactory.setIgnoringComments(true);

			docBuilder = docBuilderFactory.newDocumentBuilder();
			transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty(OutputKeys.INDENT, "no");
		} catch (final ParserConfigurationException e) {
			throw new InternalError("Error creating Document Builder");
		} catch (final TransformerConfigurationException e) {
			throw new InternalError("Error creating Transformer");
		}
	}

	private final Document document;
	private final Element element;

	protected XMLPacketImpl(final String name) {
		this(name, null);
	}

	protected XMLPacketImpl(final String name, @Nullable final String namespace) {
		document = docBuilder.newDocument();
		if (namespace != null) {
			element = document.createElementNS(namespace, name);
		} else {
			element = document.createElement(name);
		}
		document.appendChild(element);
	}

	protected XMLPacketImpl(final Element element) {
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

		return new XMLPacketImpl((Element) parent);
	}

	@Override
	public XMLPacket getFirstParent() {
		return new XMLPacketImpl(document.getDocumentElement());
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
		final Element newElement;
		if (namespace != null) {
			newElement = document.createElementNS(namespace, checkNotNull(name));
		} else {
			newElement = document.createElement(checkNotNull(name));
		}
		element.appendChild(newElement);
		return new XMLPacketImpl(newElement);
	}

	@Override
	public XMLPacket addChild(final HasXML child) {
		checkArgument(checkNotNull(child.getXML()) instanceof XMLPacketImpl);

		final Element newElement = (Element) document.importNode(((XMLPacketImpl) child.getXML()).element, true);
		element.appendChild(newElement);
		return new XMLPacketImpl(newElement);
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

			return new XMLPacketImpl(element);
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

			result.add(new XMLPacketImpl(element));
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
		checkArgument(child.getXML() instanceof XMLPacketImpl);
		
		element.removeChild(((XMLPacketImpl) child.getXML()).element);
	}

	@Override
	public String getText() {
		return element.getTextContent();
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
		element.setTextContent(text);
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
			final Document doc = docBuilder.parse(new ByteArrayInputStream(xml.getBytes()));
			return new XMLPacketImpl(doc.getDocumentElement());
		} catch (final Exception e) {
			return null;
		}
	}

	@Override
	public String toString() {
		try {
			final StringWriter buffer = new StringWriter();
			transformer.transform(new DOMSource(element), new StreamResult(buffer));
			return buffer.toString();
		} catch (final TransformerException e) {
			throw new InternalError("Transformer error");
		}
	}

}
