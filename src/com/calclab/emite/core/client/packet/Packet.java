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
package com.calclab.emite.core.client.packet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Packet extends AbstractPacket {
    private final HashMap<String, String> attributes;
    private final ArrayList<IPacket> children;
    private final String name;
    private Packet parent;

    public Packet(final String name) {
	this(name, null);
    }

    public Packet(final String name, final String xmlns) {
	this.name = name;
	this.attributes = new HashMap<String, String>();
	this.children = new ArrayList<IPacket>();
	if (xmlns != null) {
	    setAttribute("xmlns", xmlns);
	}
	parent = null;
    }

    public IPacket addChild(final IPacket child) {
	children.add(child);
	return child;
    }

    public IPacket addChild(final String name) {
	return addChild(name, null);
    }

    public IPacket addChild(final String name, final String xmlns) {
	final Packet child = new Packet(name, xmlns);
	child.parent = this;
	add(child);
	return child;
    }

    public String getAttribute(final String name) {
	return attributes.get(name);
    }

    /**
     * WARNING: broken encapsulation
     */
    public HashMap<String, String> getAttributes() {
	return attributes;
    }

    public List<? extends IPacket> getChildren() {
	return children;
    }

    public int getChildrenCount() {
	return children.size();
    }

    public String getName() {
	return name;
    }

    public IPacket getParent() {
	return parent;
    }

    /**
     * Return a child (If it has a child)
     */
    public String getText() {
	for (final IPacket child : children) {
	    if (child.getName() == null) {
		return TextUtils.unescape(child.toString());
	    }
	}
	return null;
    }

    public boolean removeChild(final IPacket child) {
	return children.remove(child);
    }

    public void setAttribute(final String name, final String value) {
	attributes.put(name, value);
    }

    public void setText(final String value) {
	children.clear();
	if (value != null) {
	    children.add(new TextPacket(TextUtils.escape(value)));
	}
    }

    @Override
    public String toString() {
	return PacketRenderer.toString(this);
    }

    public IPacket With(final IPacket child) {
	addChild(child);
	return this;
    }

    protected void add(final Packet node) {
	children.add(node);
    }
}
