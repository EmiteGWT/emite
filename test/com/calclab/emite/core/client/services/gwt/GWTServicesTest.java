package com.calclab.emite.core.client.services.gwt;

import java.util.List;

import org.junit.Test;

import com.calclab.emite.core.client.packet.IPacket;
import com.google.gwt.junit.client.GWTTestCase;

public class GWTServicesTest extends GWTTestCase {

    @Override
    public String getModuleName() {
        return "com.calclab.emite.core.EmiteCore";
    }

    @Test
    public void testToXML() {
        final String textXml = "<test attr=\"&quot;&lt;&amp;&gt;&#39;\"><child /><childWithText>&quot;&lt;&amp;&gt;&#39;</childWithText></test>";

        final GWTXMLService service = new GWTXMLService();

        IPacket result = service.toXML(textXml);

        assertEquals("Root has wrong number of attributes", 1, result.getAttributes().size());
        assertEquals("Attribute not correct", "\"<&>'", result.getAttribute("attr"));

        final List<? extends IPacket> children = result.getChildren();

        assertEquals("First child not found", "child", children.get(0).getName());
        assertEquals("First child has wrong number of children", 0, children.get(0).getChildrenCount());
        assertEquals("First child has wrong number of attributes", 0, children.get(0).getAttributes().size());

        assertEquals("Second child not found", "childWithText", children.get(1).getName());
        assertEquals("Second child has wrong number of children", 1, children.get(1).getChildrenCount());
        assertEquals("Second child has wrong number of attributes", 0, children.get(1).getAttributes().size());

        assertEquals("Second child has wrong text node", "\"<&>'", children.get(1).getText());
    }

}
