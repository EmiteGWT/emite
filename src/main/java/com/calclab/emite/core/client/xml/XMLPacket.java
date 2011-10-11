package com.calclab.emite.core.client.xml;

import java.util.List;
import java.util.Map;

public interface XMLPacket extends HasXML {
	
	String getTagName();
	String getNamespace();
	XMLPacket getParent();
	
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
