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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractPacket implements IPacket {

	@Override
	public List<? extends IPacket> getChildren(final PacketMatcher filter) {
		final List<IPacket> list = new ArrayList<IPacket>();
		for (final IPacket child : getChildren()) {
			if (filter.matches(child)) {
				list.add(child);
			}
		}
		return list;
	}

	public List<? extends IPacket> getChildren(final String name) {
		return getChildren(MatcherFactory.byName(name));
	}

	@Override
	public IPacket getFirstChild(final PacketMatcher filter) {
		for (final IPacket child : getChildren()) {
			if (filter.matches(child))
				return child;
		}
		return NoPacket.INSTANCE;
	}

	@Override
	public IPacket getFirstChild(final String name) {
		return getFirstChild(MatcherFactory.byName(name));
	}

	@Override
	public IPacket getFirstChildInDeep(final PacketMatcher filter) {
		final LinkedList<IPacket> queue = new LinkedList<IPacket>();
		queue.add(this);
		return bfs(queue, filter);
	}

	@Override
	public IPacket getFirstChildInDeep(final String name) {
		return getFirstChildInDeep(MatcherFactory.byName(name));
	}

	@Override
	public boolean hasAttribute(final String name) {
		return getAttribute(name) != null;
	}

	@Override
	public boolean hasAttribute(final String name, final String value) {
		return value.equals(getAttribute(name));
	}

	@Override
	public boolean hasChild(final String name) {
		return getFirstChild(name) != NoPacket.INSTANCE;
	}

	@Override
	public void setTextToChild(final String nodeName, final String text) {
		if (text != null) {
			IPacket node = getFirstChild(nodeName);
			if (node == NoPacket.INSTANCE) {
				node = this.addChild(nodeName, null);
			}
			node.setText(text);
		} else {
			removeChild(getFirstChild(nodeName));
		}
	}

	public IPacket With(final String name, final long value) {
		return With(name, String.valueOf(value));
	}

	@Override
	public IPacket With(final String name, final String value) {
		setAttribute(name, value);
		return this;
	}

	public IPacket WithText(final String text) {
		setText(text);
		return this;
	}

	private IPacket bfs(final LinkedList<IPacket> queue, final PacketMatcher filter) {
		if (queue.isEmpty())
			return NoPacket.INSTANCE;
		final IPacket current = queue.poll();
		if (filter.matches(current))
			return current;
		queue.addAll(current.getChildren());
		return bfs(queue, filter);
	}

}
