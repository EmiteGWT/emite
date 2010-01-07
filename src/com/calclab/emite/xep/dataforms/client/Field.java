package com.calclab.emite.xep.dataforms.client;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * XEP-0004 Field element : A data form of type "form", "submit", or "result"
 * SHOULD contain at least one <field/> element; a data form of type "cancel"
 * SHOULD NOT contain any <field/> elements.
 * 
 */
public class Field {

    private String desc;

    private boolean required;

    /**
     * The <field/> element MAY possess a 'label' attribute that defines a
     * human-readable name for the field.
     */
    private String label;

    /**
     * element SHOULD possess a 'type' attribute that defines the data "type" of
     * the field data (if no 'type' is specified, the default is "text-single")
     */
    private String type;

    /**
     * Fields of type list-multi, jid-multi, text-multi, and hidden MAY contain
     * more than one <value/> and this field is only for the other types
     */
    private String value;

    private final List<Option> options = new ArrayList<Option>();

    public String getDesc() {
        return desc;
    }

    public String getLabel() {
        return label;
    }

    public List<Option> getOptions() {
        return options;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public boolean isRequired() {
        return required;
    }

    public void setDesc(final String desc) {
        this.desc = desc;
    }

    public void setLabel(final String label) {
        this.label = label;
    }

    public void setRequired(final boolean required) {
        this.required = required;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public void setValue(final String value) {
        this.value = value;
    }

}
