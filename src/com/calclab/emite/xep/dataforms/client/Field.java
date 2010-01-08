package com.calclab.emite.xep.dataforms.client;

import java.util.ArrayList;
import java.util.List;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.MatcherFactory;
import com.calclab.emite.core.client.packet.NoPacket;

/**
 * 
 * XEP-0004 Field element : A data form of type "form", "submit", or "result"
 * SHOULD contain at least one <field/> element; a data form of type "cancel"
 * SHOULD NOT contain any <field/> elements.
 * 
 */
public class Field {

    public static List<Field> parseList(final IPacket packet) {
        final List<Field> fields = new ArrayList<Field>();
        for (final IPacket fieldPacket : packet.getChildren(MatcherFactory.byName("field"))) {
            fields.add(singleParse(fieldPacket));
        }
        return fields;
    }

    public static Field singleParse(final IPacket packet) {
        final Field field = new Field();
        field.setType(packet.getAttribute("type"));
        field.setLabel(packet.getAttribute("label"));
        field.setVar(packet.getAttribute("var"));
        field.setDesc(packet.getFirstChild("desc").getText());
        field.setRequired(packet.getFirstChild("required") != NoPacket.INSTANCE);
        final List<String> valuesList = new ArrayList<String>();
        for (final IPacket valuePacket : packet.getChildren(MatcherFactory.byName("value"))) {
            valuesList.add(valuePacket.getText());
        }
        field.setValues(valuesList);
        final List<Option> optionsList = new ArrayList<Option>();
        for (final IPacket optionPacket : packet.getChildren(MatcherFactory.byName("option"))) {
            optionsList.add(Option.parse(optionPacket));
        }
        field.setOptions(optionsList);
        return field;
    }

    /**
     * element SHOULD possess a 'type' attribute that defines the data "type" of
     * the field data (if no 'type' is specified, the default is "text-single")
     */
    private String type;

    /**
     * The <field/> element MAY possess a 'label' attribute that defines a
     * human-readable name for the field.
     */
    private String label;
    private String desc;
    private String var;
    private boolean required;

    /**
     * Fields of type list-multi, jid-multi, text-multi, and hidden MAY contain
     * more than one <value/> and this field is only for the other types
     */
    private List<String> values;
    private List<Option> options;

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

    public List<String> getValues() {
        return values;
    }

    public String getVar() {
        return var;
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

    public void setOptions(final List<Option> options) {
        this.options = options;
    }

    public void setRequired(final boolean required) {
        this.required = required;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public void setValues(final List<String> values) {
        this.values = values;
    }

    public void setVar(final String var) {
        this.var = var;
    }

}
