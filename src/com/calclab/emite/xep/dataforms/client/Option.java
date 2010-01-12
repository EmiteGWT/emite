package com.calclab.emite.xep.dataforms.client;

import com.calclab.emite.core.client.packet.DelegatedPacket;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.Packet;

/**
 * A XEP-0004 field option
 * 
 */
public class Option extends DelegatedPacket {

    public static final String OPTION = "option";
    private static final String LABEL = "label";
    private static final String VALUE = "value";

    public Option() {
        super((new Packet(OPTION)));
    }

    public Option(final IPacket stanza) {
        super(stanza);
    }

    public String getLabel() {
        return super.getAttribute(LABEL);
    }

    public String getValue() {
        return super.getFirstChild(VALUE).getText();
    }

    public void setLabel(final String label) {
        super.setAttribute(LABEL, label);
    }

    public void setValue(final String value) {
        setTextToChild(VALUE, value);
    }
}
