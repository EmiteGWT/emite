package com.calclab.emite.xep.dataforms.client;

/**
 * A XEP-0004 field option
 * 
 */
public class Option {
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
