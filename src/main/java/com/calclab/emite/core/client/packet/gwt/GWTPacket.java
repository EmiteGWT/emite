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

package com.calclab.emite.core.client.packet.gwt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.calclab.emite.core.client.packet.AbstractPacket;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.PacketRenderer;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.impl.DOMNodeException;

public class GWTPacket extends AbstractPacket {
	private static final List<IPacket> EMPTY_LIST = new ArrayList<IPacket>();
	private final Element element;

	public GWTPacket(final Element element) {
		this.element = element;
	}

	@Override
	public IPacket addChild(final IPacket child) {
		final Element childElement = ((GWTPacket) child).element;
		element.appendChild(childElement);
		return child;
	}

	@Override
	public IPacket addChild(final String nodeName) {
		return addChild(nodeName, null);
	}

	@Override
	public IPacket addChild(final String nodeName, final String xmlns) {
		final Element child = element.getOwnerDocument().createElement(nodeName);
		element.appendChild(child);
		return new GWTPacket(child);
	}

	@Override
	public String getAttribute(final String name) {
		return element.getAttribute(name);
	}

	@Override
	public HashMap<String, String> getAttributes() {
		final HashMap<String, String> map = new HashMap<String, String>();
		final NamedNodeMap attributes = element.getAttributes();
		for (int index = 0; index < attributes.getLength(); index++) {
			final Node attrib = attributes.item(index);
			map.put(attrib.getNodeName(), attrib.getNodeValue());
		}
		return map;
	}

	public Map<String, String> getAttributtes() {
		final HashMap<String, String> attributes = new HashMap<String, String>();
		final NamedNodeMap original = element.getAttributes();
		for (int index = 0; index < original.getLength(); index++) {
			final Node node = original.item(index);
			attributes.put(node.getNodeName(), node.getNodeValue());
		}
		return attributes;
	}

	@Override
	public List<? extends IPacket> getChildren() {
		return wrap(element.getChildNodes());
	}

	@Override
	public List<IPacket> getChildren(final String name) {
		final NodeList nodes = element.getElementsByTagName(name);
		return wrap(nodes);
	}

	@Override
	public int getChildrenCount() {
		return element.getChildNodes().getLength();
	}

	public Element getElement() {
		return element;
	}

	@Override
	public String getName() {
		return element.getNodeName();
	}

	public IPacket getParent() {
		return new GWTPacket((Element) element.getParentNode());
	}

	@Override
	public String getText() {
		Node item;
		final NodeList childs = element.getChildNodes();
		for (int index = 0; index < childs.getLength(); index++) {
			item = childs.item(index);
			if (item.getNodeType() == Node.TEXT_NODE)
				return item.getNodeValue();
		}
		return null;
	}

	@Override
	public boolean removeChild(final IPacket child) {
		final Element childElement = ((GWTPacket) child).element;
		try {
			return element.removeChild(childElement) != null;
		} catch (final DOMNodeException e) {
			return false;
		}
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
	public void setText(final String text) {
		final NodeList nodes = element.getChildNodes();
		for (int index = 0; index < nodes.getLength(); index++) {
			final Node child = nodes.item(index);
			if (child.getNodeType() == Node.TEXT_NODE) {
				element.removeChild(child);
			}
		}
		if (text != null) {
			element.appendChild(element.getOwnerDocument().createTextNode(text));
		}
	}

	@Override
	public String toString() {
		return PacketRenderer.toString(this);
	}

	private List<IPacket> wrap(final NodeList nodes) {
		int length;
		if (nodes == null || (length = nodes.getLength()) == 0)
			return EMPTY_LIST;
		final ArrayList<IPacket> selected = new ArrayList<IPacket>();
		for (int index = 0; index < length; index++) {
			final Node node = nodes.item(index);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				selected.add(new GWTPacket((Element) node));
			} else if (node.getNodeType() == Node.TEXT_NODE) {
			}
		}
		return selected;
	}
}
