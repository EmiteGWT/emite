package com.calclab.emite.xep.dataforms.client;

import java.util.ArrayList;
import java.util.List;

import com.calclab.emite.core.client.packet.DelegatedPacket;
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

public class Field extends DelegatedPacket {

    public static List<Field> parseList(final IPacket packet) {
        final List<Field> fields = new ArrayList<Field>();
        for (final IPacket fieldPacket : packet.getChildren(MatcherFactory.byName("field"))) {
            fields.add(new Field(fieldPacket));
        }
        return fields;
    }

    public Field(final IPacket stanza) {
        super(stanza);
    }

    public String getDesc() {
        return super.getFirstChild("desc").getText();
    }

    public String getLabel() {
        return super.getAttribute("label");
    }

    public List<Option> getOptions() {
        final List<Option> optionsList = new ArrayList<Option>();
        for (final IPacket optionPacket : super.getChildren(MatcherFactory.byName("option"))) {
            optionsList.add(new Option(optionPacket.getAttribute("label"),
                    optionPacket.getFirstChild("value").getText()));
        }
        return optionsList;
    }

    public String getType() {
        return super.getAttribute("type");
    }

    public List<String> getValues() {
        final List<String> valuesList = new ArrayList<String>();
        for (final IPacket valuePacket : super.getChildren(MatcherFactory.byName("value"))) {
            valuesList.add(valuePacket.getText());
        }
        return valuesList;
    }

    public String getVar() {
        return super.getAttribute("var");
    }

    public boolean isRequired() {
        return super.getFirstChild("required") != NoPacket.INSTANCE;
    }

    public void setDesc(final String desc) {
        IPacket child = super.getFirstChild("desc");
        if (child == NoPacket.INSTANCE) {
            child = super.addChild("desc", "");
        }
        child.setText(desc);
    }

    /**
     * The <field/> element MAY possess a 'label' attribute that defines a
     * human-readable name for the field.
     */

    public void setLabel(final String label) {
        super.setAttribute("label", label);
    }

    public void setOptions(final List<Option> options) {
        for (final IPacket optionPacket : super.getChildren(MatcherFactory.byName("option"))) {
            super.removeChild(optionPacket);
        }
        for (final Option option : options) {
            super.addChild("option", null).With("label", option.getLabel()).addChild("value", null).setText(
                    option.getValue());
        }
    }

    public void setRequired(final boolean required) {
        final IPacket requiredP = super.getFirstChild("required");
        if (required && requiredP == NoPacket.INSTANCE) {
            super.addChild("required", null);
        }
        if (!required && requiredP != NoPacket.INSTANCE) {
            super.removeChild(requiredP);
        }
    }

    /**
     * element SHOULD possess a 'type' attribute that defines the data "type" of
     * the field data (if no 'type' is specified, the default is "text-single")
     */

    public void setType(final String type) {
        super.setAttribute("type", type);
    }

    /**
     * Fields of type list-multi, jid-multi, text-multi, and hidden MAY contain
     * more than one <value/> and this field is only for the other types
     */

    public void setValues(final List<String> values) {
        for (final IPacket valuePacket : super.getChildren(MatcherFactory.byName("value"))) {
            super.removeChild(valuePacket);
        }
        for (final String value : values) {
            super.addChild("value", value);
        }
    }

    public void setVar(final String var) {
        super.setAttribute("var", var);
    }

}
