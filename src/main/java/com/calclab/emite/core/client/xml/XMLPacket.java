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

package com.calclab.emite.core.client.xml;

import java.util.List;
import java.util.Map;

public interface XMLPacket extends HasXML {

	String getTagName();

	String getNamespace();

	XMLPacket getParent();
	
	XMLPacket getFirstParent();

	Map<String, String> getAttributes();

	String getAttribute(String name);

	void setAttribute(String name, String value);

	boolean hasChild(String name);

	boolean hasChild(String name, String namespace);

	XMLPacket addChild(String name);

	XMLPacket addChild(String name, String namespace);

	XMLPacket addChild(HasXML child);

	XMLPacket getFirstChild(String name);

	XMLPacket getFirstChild(String name, String namespace);

	XMLPacket getFirstChild(XMLMatcher matcher);

	List<XMLPacket> getChildren();

	List<XMLPacket> getChildren(String name);

	List<XMLPacket> getChildren(String name, String namespace);

	List<XMLPacket> getChildren(XMLMatcher matcher);

	void removeChild(HasXML child);

	String getText();

	String getChildText(String name);

	String getChildText(String name, String namespace);

	void setText(String text);

	void setChildText(String name, String text);

	void setChildText(String name, String namespace, String text);

}
