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

package com.calclab.emite.base.xml;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * Represents an XML element.
 * 
 * This interface provides common XML manipulation methods
 * to hide differences between implementations. 
 */
public interface XMLPacket extends HasXML {

	/**
	 * Returns the tag name of this element.
	 * 
	 * @return the tag name
	 */
	String getTagName();

	/**
	 * Returns the namespace of this element.
	 * 
	 * @return the namespace
	 */
	String getNamespace();

	/**
	 * Returns the parent of this element.
	 * 
	 * @return the parent, or {@code null} if there is no parent
	 */
	@Nullable
	XMLPacket getParent();

	/**
	 * Returns the first parent of this element.
	 * 
	 * @return the first parent of this element
	 */
	XMLPacket getFirstParent();

	/**
	 * Checks if the attribute exists.
	 * 
	 * @param name the attribute name to check
	 * @return {@code true} if the attribute exists, {@code false} otherwise
	 */
	boolean hasAttribute(String name);

	/**
	 * Returns all attributes with their values.
	 * 
	 * This list is immutable, use {@link #getAttribute(String)} or
	 * {@link #setAttribute(String, String)} to modify them. This list will NOT
	 * be updated when attributes are added or removed.
	 * 
	 * @return an immutable list of attributes
	 */
	ImmutableMap<String, String> getAttributes();

	/**
	 * Returns the requested attribute.
	 * 
	 * @param name the attribute to get
	 * @return the value of the attribute, or {@code null} if it doesn't exist
	 */
	@Nullable
	String getAttribute(String name);

	/**
	 * Sets or changes the value of an attribute.
	 * 
	 * If value is {@code null}, the attribute will be removed.
	 * 
	 * @param name the attribute to set
	 * @param value the new value of the attribute, or {@code null} to remove it.
	 */
	void setAttribute(String name, @Nullable String value);

	/**
	 * Checks if this element has a child with a given name.
	 * 
	 * @param name the child name to check for
	 * @return {@code true} if there is a child with the given name, {@code false} otherwise
	 */
	boolean hasChild(String name);

	/**
	 * Checks if this element has a child with a given name and namespace.
	 * 
	 * @param name the child name to check for
	 * @param namespace the child namespace to check for
	 * @return {@code true} if there is a child with the given name, {@code false} otherwise
	 */
	boolean hasChild(String name, String namespace);

	/**
	 * Adds a new child with the given name.
	 * 
	 * @param name the name for the new child
	 * @return a XMLPacket representing the new child
	 */
	XMLPacket addChild(String name);

	/**
	 * Adds a new child with the given name and namespace.
	 * 
	 * @param name the name for the new child
	 * @param namespace the namespace for the new child
	 * @return a XMLPacket representing the new child
	 */
	XMLPacket addChild(String name, @Nullable String namespace);

	/**
	 * Adds another element as a child of this one.
	 * 
	 * A copy of the given element is inserted. Changes to the original will not be reflected on the child.
	 * 
	 * @param child the element to add as a child
	 * @return a XMLPacket representing the new child
	 */
	XMLPacket addChild(HasXML child);

	/**
	 * Returns the first child with the given name.
	 * 
	 * This is useful when only one child is expected.
	 * 
	 * @param name the name of the child
	 * @return the requested child, or {@code null} if not found
	 */
	@Nullable
	XMLPacket getFirstChild(String name);

	/**
	 * Returns the first child with the given name and namespace.
	 * 
	 * This is useful when only one child is expected.
	 * 
	 * @param name the name of the child
	 * @param namespace the namespace of the child
	 * @return the requested child, or {@code null} if not found
	 */
	@Nullable
	XMLPacket getFirstChild(String name, String namespace);

	/**
	 * Returns the first child that matches the predicate.
	 * 
	 * This is useful when only one matching child is expected.
	 * 
	 * @param matcher the predicate to select a child
	 * @return the requested child, or {@code null} if not found
	 */
	@Nullable
	XMLPacket getFirstChild(Predicate<XMLPacket> matcher);

	/**
	 * Returns all children of this element.
	 * 
	 * This list is immutable, it will NOT update when children are added or removed.
	 * 
	 * @return the children for this element
	 */
	ImmutableList<XMLPacket> getChildren();

	/**
	 * Returns all children with the requested name.
	 * 
	 * This list is immutable, it will NOT update when children are added or removed.
	 * 
	 * @param name the name of the requested children
	 * @return the requested children
	 */
	ImmutableList<XMLPacket> getChildren(String name);

	/**
	 * Returns all children with the requested name and namespace.
	 * 
	 * This list is immutable, it will NOT update when children are added or removed.
	 * 
	 * @param name the name of the requested children
	 * @param namespace the namespace of the requested children
	 * @return the requested children
	 */
	ImmutableList<XMLPacket> getChildren(String name, String namespace);

	/**
	 * Returns all children that match the predicate.
	 * 
	 * This list is immutable, it will NOT update when children are added or removed.
	 * 
	 * @param matcher the predicate to select the children
	 * @return the requested children
	 */
	ImmutableList<XMLPacket> getChildren(Predicate<XMLPacket> matcher);

	/**
	 * Removes a child from this element.
	 * 
	 * @param child the child to be removed
	 */
	void removeChild(HasXML child);

	/**
	 * Returns the text contents of this element.
	 * 
	 * @return the text content of this element
	 */
	String getText();

	/**
	 * Returns the text contents of a child from this element.
	 * 
	 * This is shortcut for getting the first child and its contents.
	 * 
	 * @param name the name of the child
	 * @return the text contents of the child, or {@code null} if no child found
	 */
	@Nullable
	String getChildText(String name);

	/**
	 * Returns the text contents of a child from this element.
	 * 
	 * This is shortcut for getting the first child and its contents.
	 * 
	 * @param name the name of the child
	 * @param namespace the namespace of the child
	 * @return the text contents of the child, or {@code null} if no child found
	 */
	@Nullable
	String getChildText(String name, String namespace);

	/**
	 * Sets the text contents of this element.
	 * 
	 * @param text the new text contents
	 */
	void setText(@Nullable String text);

	/**
	 * Sets the text contents of a child from this element.
	 * 
	 * This is a shortcut for getting/creating a child and setting its contents.
	 * 
	 * @param name the name of the child
	 * @param text the text contents of the child
	 */
	void setChildText(String name, @Nullable String text);

	/**
	 * Sets the text contents of a child from this element.
	 * 
	 * This is a shortcut for getting/creating a child and setting its contents.
	 * 
	 * @param name the name of the child
	 * @param namespace the namespace of the child
	 * @param text the text contents of the child
	 */
	void setChildText(String name, @Nullable String namespace, @Nullable String text);

}
