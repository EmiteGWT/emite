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

package com.calclab.emite.core.client.packet;

import java.util.HashMap;
import java.util.List;

public class DelegatedPacket implements IPacket {
	private final IPacket delegate;

	public DelegatedPacket(final IPacket delegate) {
		this.delegate = delegate;
	}

	@Override
	public IPacket addChild(final IPacket child) {
		return delegate.addChild(child);
	}

	@Override
	public final IPacket addChild(final String nodeName) {
		return delegate.addChild(nodeName);
	}

	@Override
	public final IPacket addChild(final String nodeName, final String xmlns) {
		return delegate.addChild(nodeName, xmlns);
	}

	@Override
	public final String getAttribute(final String name) {
		return delegate.getAttribute(name);
	}

	@Override
	public HashMap<String, String> getAttributes() {
		return delegate.getAttributes();
	}

	@Override
	public List<? extends IPacket> getChildren() {
		return delegate.getChildren();
	}

	@Override
	public List<? extends IPacket> getChildren(final PacketMatcher filter) {
		return delegate.getChildren(filter);
	}

	@Override
	public int getChildrenCount() {
		return delegate.getChildrenCount();
	}

	@Override
	public IPacket getFirstChild(final PacketMatcher filter) {
		return delegate.getFirstChild(filter);
	}

	@Override
	public final IPacket getFirstChild(final String childName) {
		return delegate.getFirstChild(childName);
	}

	@Override
	public IPacket getFirstChildInDeep(final PacketMatcher filter) {
		return delegate.getFirstChildInDeep(filter);
	}

	@Override
	public IPacket getFirstChildInDeep(final String childName) {
		return delegate.getFirstChild(childName);
	}

	@Override
	public final String getName() {
		return delegate.getName();
	}

	@Override
	public final String getText() {
		return delegate.getText();
	}

	@Override
	public boolean hasAttribute(final String name) {
		return delegate.hasAttribute(name);
	}

	@Override
	public boolean hasAttribute(final String name, final String value) {
		return delegate.hasAttribute(name, value);
	}

	@Override
	public boolean hasChild(final String name) {
		return delegate.hasChild(name);
	}

	@Override
	public boolean removeChild(final IPacket child) {
		return delegate.removeChild(child);
	}

	@Override
	public final void setAttribute(final String name, final String value) {
		delegate.setAttribute(name, value);
	}

	@Override
	public final void setText(final String text) {
		delegate.setText(text);
	}

	@Override
	public void setTextToChild(final String nodeName, final String text) {
		delegate.setTextToChild(nodeName, text);
	}

	@Override
	public String toString() {
		return delegate.toString();
	}

	@Override
	public IPacket With(final String name, final String value) {
		delegate.With(name, value);
		return this;
	}
}
