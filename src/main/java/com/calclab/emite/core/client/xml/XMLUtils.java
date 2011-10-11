package com.calclab.emite.core.client.xml;

import com.google.gwt.xml.client.XMLParser;
import com.google.gwt.xml.client.impl.DOMParseException;

public class XMLUtils {
	
	public static final XMLPacket createPacket(String name) {
		return new XMLPacketImplGWT(name);
	}
	
	public static final XMLPacket createPacket(String name, String namespace) {
		return new XMLPacketImplGWT(name, namespace);
	}
	
	public static final XMLPacket fromXML(String xml) {
		try {
			return new XMLPacketImplGWT(XMLParser.parse(xml).getDocumentElement());
		} catch (DOMParseException e) {
			return null;
		}
	}
	
}
