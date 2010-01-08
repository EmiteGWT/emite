package com.calclab.emite.xep.dataforms.client;

import com.calclab.emite.core.client.packet.IPacket;

/**
 * A XEP-0004 field option
 * 
 */
public class Option {
    public static Option parse(final IPacket packet) {
        final Option option = new Option();
        option.setLabel(packet.getAttribute("label"));
        option.setValue(packet.getFirstChild("value").getText());
        return option;
    }
    private String label;

    private String value;

    public Option() {
    }

    public Option(final String label, final String value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

    public void setLabel(final String label) {
        this.label = label;
    }

    public void setValue(final String value) {
        this.value = value;
    }

}
