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

import java.util.HashMap;
import java.util.List;

public class DelegatedPacket implements IPacket {
    private final IPacket delegate;

    public DelegatedPacket(final IPacket delegate) {
        this.delegate = delegate;
    }

    public IPacket addChild(final IPacket child) {
        return delegate.addChild(child);
    }

    public final IPacket addChild(final String nodeName) {
        return delegate.addChild(nodeName);
    }

    public final IPacket addChild(final String nodeName, final String xmlns) {
        return delegate.addChild(nodeName, xmlns);
    }

    public final String getAttribute(final String name) {
        return delegate.getAttribute(name);
    }

    public HashMap<String, String> getAttributes() {
        return delegate.getAttributes();
    }

    public List<? extends IPacket> getChildren() {
        return delegate.getChildren();
    }

    public List<? extends IPacket> getChildren(final PacketMatcher filter) {
        return delegate.getChildren(filter);
    }

    public int getChildrenCount() {
        return delegate.getChildrenCount();
    }

    public IPacket getFirstChild(final PacketMatcher filter) {
        return delegate.getFirstChild(filter);
    }

    public final IPacket getFirstChild(final String childName) {
        return delegate.getFirstChild(childName);
    }

    public IPacket getFirstChildInDeep(final PacketMatcher filter) {
        return delegate.getFirstChildInDeep(filter);
    }

    public IPacket getFirstChildInDeep(final String childName) {
        return delegate.getFirstChild(childName);
    }

    public final String getName() {
        return delegate.getName();
    }

    public final String getText() {
        return delegate.getText();
    }

    public boolean hasAttribute(final String name) {
        return delegate.hasAttribute(name);
    }

    public boolean hasAttribute(final String name, final String value) {
        return delegate.hasAttribute(name, value);
    }

    public boolean hasChild(final String name) {
        return delegate.hasChild(name);
    }

    public boolean removeChild(final IPacket child) {
        return delegate.removeChild(child);
    }

    public final void setAttribute(final String name, final String value) {
        delegate.setAttribute(name, value);
    }

    public final void setText(final String text) {
        delegate.setText(text);
    }

    /**
     * Add a child with a specified text. Create the child if not exists. If the
     * text is null, then removes the child
     * 
     * @param nodeName
     * @param text
     */
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

    @Override
    public String toString() {
        return delegate.toString();
    }

    public IPacket With(final String name, final String value) {
        delegate.With(name, value);
        return this;
    }
}
