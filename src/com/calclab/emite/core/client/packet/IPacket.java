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

public interface IPacket {

    public HashMap<String, String> getAttributes();

    public int getChildrenCount();

    public boolean hasAttribute(String name);

    public boolean hasAttribute(String name, String value);

    /**
     * Removes a single instance of the specified child from this packet, if it
     * is present (optional operation).
     * 
     * @param child
     * @return true if the list contained the specified element.
     */
    public boolean removeChild(IPacket child);

    IPacket addChild(String nodeName, String xmlns);

    String getAttribute(String name);

    List<? extends IPacket> getChildren();

    /**
     * Return a list of descendant childs after filter by the filter
     * 
     * @param filter
     * @return
     */
    List<? extends IPacket> getChildren(PacketMatcher matcher);

    /**
     * Returns the first children that matches the matcher
     * 
     * @param matcher
     *            the matcher
     * @return the first children if found, NoPacket.INSTANCE if nothing found
     */
    IPacket getFirstChild(PacketMatcher matcher);

    /**
     * Return the first direct child with this name. NEVER returns null
     * 
     * @param childName
     * @return the Packet or NoPacket instance
     */
    IPacket getFirstChild(String childName);

    /**
     * Return the first direct child that matches the matcher, search
     * recursively (using the Breath-first search algorithm). See:
     * http://en.wikipedia.org/wiki/Breadth-first_search
     * 
     * @param matcher
     *            the matcher
     * @return the Packet or NoPacket instance
     */
    IPacket getFirstChildInDeep(PacketMatcher matcher);

    /**
     * Return the first direct child with this name, search recursively (using
     * the Breath-first search algorithm). See:
     * http://en.wikipedia.org/wiki/Breadth-first_search
     * 
     * @param childName
     * @return the Packet or NoPacket instance
     */
    IPacket getFirstChildInDeep(String childName);

    String getName();

    String getText();

    boolean hasChild(String name);

    void setAttribute(String name, String value);

    void setText(String text);

    /**
     * Chain-able method to add a attribute
     * 
     * @param name
     * @param value
     * @return
     */
    IPacket With(String name, String value);

}
