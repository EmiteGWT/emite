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

public interface IPacket {

	IPacket addChild(IPacket child);

	IPacket addChild(String nodeName);

	IPacket addChild(String nodeName, String xmlns);

	String getAttribute(String name);

	HashMap<String, String> getAttributes();

	List<? extends IPacket> getChildren();

	/**
	 * Return a list of descendant childs after filter by the filter
	 * 
	 * @param filter
	 * @return
	 */
	List<? extends IPacket> getChildren(PacketMatcher matcher);

	int getChildrenCount();

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

	boolean hasAttribute(String name);

	boolean hasAttribute(String name, String value);

	boolean hasChild(String name);

	/**
	 * Removes a single instance of the specified child from this packet, if it
	 * is present (optional operation).
	 * 
	 * @param child
	 * @return true if the list contained the specified element.
	 */
	boolean removeChild(IPacket child);

	void setAttribute(String name, String value);

	void setText(String text);

	/**
	 * Add a child with a specified text. Create the child if not exists. If the
	 * text is null, then removes the child
	 * 
	 * @param nodeName
	 * @param text
	 */
	void setTextToChild(final String nodeName, final String text);

	/**
	 * Chain-able method to add a attribute
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	IPacket With(String name, String value);

}
