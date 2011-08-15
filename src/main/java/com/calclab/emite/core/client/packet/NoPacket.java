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
import java.util.HashMap;
import java.util.List;

/**
 * A substitute of null
 */
public class NoPacket implements IPacket {
	public static final NoPacket INSTANCE = new NoPacket();
	private static final HashMap<String, String> EMPTY_ATTRIBUTTES = new HashMap<String, String>();
	private static final List<? extends IPacket> EMPTY_CHILDREN = new ArrayList<IPacket>();

	private NoPacket() {

	}

	@Override
	public IPacket addChild(final IPacket child) {
		return this;
	}

	@Override
	public IPacket addChild(final String nodeName) {
		return this;
	}

	@Override
	public IPacket addChild(final String nodeName, final String xmlns) {
		return this;
	}

	@Override
	public String getAttribute(final String name) {
		return null;
	}

	@Override
	public HashMap<String, String> getAttributes() {
		return EMPTY_ATTRIBUTTES;
	}

	@Override
	public List<? extends IPacket> getChildren() {
		return EMPTY_CHILDREN;
	}

	@Override
	public List<? extends IPacket> getChildren(final PacketMatcher filter) {
		return EMPTY_CHILDREN;
	}

	public List<? extends IPacket> getChildren(final String name) {
		return EMPTY_CHILDREN;
	}

	@Override
	public int getChildrenCount() {
		return 0;
	}

	@Override
	public IPacket getFirstChild(final PacketMatcher filter) {
		return this;
	}

	@Override
	public IPacket getFirstChild(final String childName) {
		return this;
	}

	@Override
	public IPacket getFirstChildInDeep(final PacketMatcher filter) {
		return this;
	}

	@Override
	public IPacket getFirstChildInDeep(final String name) {
		return this;
	}

	@Override
	public String getName() {
		return null;
	}

	public IPacket getParent() {
		return this;
	}

	@Override
	public String getText() {
		return null;
	}

	@Override
	public boolean hasAttribute(final String name) {
		return false;
	}

	@Override
	public boolean hasAttribute(final String name, final String value) {
		return false;
	}

	@Override
	public boolean hasChild(final String name) {
		return false;
	}

	@Override
	public boolean removeChild(final IPacket child) {
		return false;
	}

	public void render(final StringBuffer buffer) {
	}

	@Override
	public void setAttribute(final String name, final String value) {
	}

	@Override
	public void setText(final String text) {
	}

	@Override
	public void setTextToChild(final String nodeName, final String text) {
	}

	public IPacket With(final IPacket child) {
		return this;
	}

	public IPacket With(final String name, final long value) {
		return this;
	}

	@Override
	public IPacket With(final String name, final String value) {
		return this;
	}

	public IPacket WithText(final String text) {
		return this;
	}

}
