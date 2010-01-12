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

/**
 * A substitute of null
 */
public class NoPacket implements IPacket {
    public static final NoPacket INSTANCE = new NoPacket();
    private static final HashMap<String, String> EMPTY_ATTRIBUTTES = new HashMap<String, String>();
    private static final List<? extends IPacket> EMPTY_CHILDREN = new ArrayList<IPacket>();

    private NoPacket() {

    }

    public IPacket addChild(final IPacket child) {
        return this;
    }

    public IPacket addChild(final String nodeName, final String xmlns) {
        return this;
    }

    public String getAttribute(final String name) {
        return null;
    }

    public HashMap<String, String> getAttributes() {
        return EMPTY_ATTRIBUTTES;
    }

    public List<? extends IPacket> getChildren() {
        return EMPTY_CHILDREN;
    }

    public List<? extends IPacket> getChildren(final PacketMatcher filter) {
        return EMPTY_CHILDREN;
    }

    public List<? extends IPacket> getChildren(final String name) {
        return EMPTY_CHILDREN;
    }

    public int getChildrenCount() {
        return 0;
    }

    public IPacket getFirstChild(final PacketMatcher filter) {
        return this;
    }

    public IPacket getFirstChild(final String childName) {
        return this;
    }

    public IPacket getFirstChildInDeep(final PacketMatcher filter) {
        return this;
    }

    public IPacket getFirstChildInDeep(final String name) {
        return this;
    }

    public String getName() {
        return null;
    }

    public IPacket getParent() {
        return this;
    }

    public String getText() {
        return null;
    }

    public boolean hasAttribute(final String name) {
        return false;
    }

    public boolean hasAttribute(final String name, final String value) {
        return false;
    }

    public boolean hasChild(final String name) {
        return false;
    }

    public boolean removeChild(final IPacket child) {
        return false;
    }

    public void render(final StringBuffer buffer) {
    }

    public void setAttribute(final String name, final String value) {
    }

    public void setText(final String text) {
    }

    public IPacket With(final IPacket child) {
        return this;
    }

    public IPacket With(final String name, final long value) {
        return this;
    }

    public IPacket With(final String name, final String value) {
        return this;
    }

    public IPacket WithText(final String text) {
        return this;
    }

}
