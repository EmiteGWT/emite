/**
 * 
 */
package com.calclab.emite.core.client.packet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author Ash
 * 
 */
public class PacketRendererTest {

    /**
     * Test method for
     * {@link com.calclab.emite.core.client.packet.PacketRenderer#toString(com.calclab.emite.core.client.packet.IPacket)}
     * .
     */
    @Test
    public void testToStringIPacket() {
        final Packet testPacket = new Packet("test");
        final Packet testChild = new Packet("child");
        final Packet testChildWithText = new Packet("childWithText");

        testChildWithText.setText("\"<&>'");

        testPacket.setAttribute("attr", "\"<&>'");

        testPacket.addChild(testChild);
        testPacket.addChild(testChildWithText);

        String result = PacketRenderer.toString(testPacket);

        // We should probably do something cleverer here really as the xml may
        // not necessarily always have to come out the same to still be correct
        assertEquals("XML has not been rendered as expected", "<test attr=\"&quot;&lt;&amp;&gt;&#39;\"><child /><childWithText>&quot;&lt;&amp;&gt;&#39;</childWithText></test>", result);
    }

}
