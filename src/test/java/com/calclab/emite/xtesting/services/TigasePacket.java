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

package com.calclab.emite.xtesting.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tigase.xml.Element;

import com.calclab.emite.core.client.packet.AbstractPacket;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.PacketRenderer;
import com.calclab.emite.core.client.packet.TextUtils;

public class TigasePacket extends AbstractPacket {

	private final Element delegate;

	public TigasePacket(final Element element) {
		delegate = element;
	}

	public TigasePacket(final String name) {
		this(new Element(name));
	}

	@Override
	public IPacket addChild(final IPacket child) {
		final TigasePacket tigaseChild = (TigasePacket) child;
		delegate.addChild(tigaseChild.delegate);
		return child;
	}

	@Override
	public IPacket addChild(final String nodeName) {
		return addChild(nodeName, null);
	}

	@Override
	public IPacket addChild(final String nodeName, final String xmlns) {
		final TigasePacket child = new TigasePacket(nodeName);
		child.setAttribute("xmlns", xmlns);
		addChild(child);
		return child;
	}

	@Override
	public String getAttribute(final String name) {
		return delegate.getAttribute(name);
	}

	@Override
	public HashMap<String, String> getAttributes() {
		final HashMap<String, String> atts = new HashMap<String, String>();
		final Map<String, String> src = delegate.getAttributes();
		if (src != null) {
			atts.putAll(src);
		}
		return atts;
	}

	@Override
	public List<? extends IPacket> getChildren() {
		final List<Element> children = delegate.getChildren();
		return wrap(children);
	}

	@Override
	public int getChildrenCount() {
		final List<Element> children = delegate.getChildren();
		return children != null ? children.size() : 0;
	}

	@Override
	public String getName() {
		return delegate.getName();
	}

	@Override
	public String getText() {
		return TextUtils.unescape(delegate.getCData());
	}

	@Override
	public boolean removeChild(final IPacket child) {
		return delegate.removeChild(((TigasePacket) child).delegate);
	}

	public void render(final StringBuffer buffer) {
		buffer.append(delegate.toString());
	}

	@Override
	public void setAttribute(final String name, final String value) {
		if (value == null) {
			delegate.removeAttribute(name);
		} else {
			delegate.setAttribute(name, value);
		}
	}

	@Override
	public void setText(final String text) {
		if (text != null) {
			delegate.setCData(text);
		}
	}

	@Override
	public String toString() {
		return PacketRenderer.toString(this);
	}

	private List<IPacket> wrap(final List<Element> children) {
		final ArrayList<IPacket> result = new ArrayList<IPacket>();
		if (children != null) {
			for (final Element e : children) {
				result.add(new TigasePacket(e));
			}
		}
		return result;
	}
}
