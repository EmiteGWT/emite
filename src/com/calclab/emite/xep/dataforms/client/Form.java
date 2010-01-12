package com.calclab.emite.xep.dataforms.client;

import java.util.ArrayList;
import java.util.List;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.MatcherFactory;
import com.calclab.emite.core.client.xmpp.stanzas.BasicStanza;

/**
 * 
 * XEP-0004 Form
 * 
 */
public class Form extends BasicStanza {

    public enum Type {
        /**
         * The form-processing entity is asking the form-submitting entity to
         * complete a form.
         */
        form,
        /**
         * The form-submitting entity is submitting data to the form-processing
         * entity. The submission MAY include fields that were not provided in
         * the empty form, but the form-processing entity MUST ignore any fields
         * that it does not understand.
         */
        submit,
        /**
         * The form-submitting entity has cancelled submission of data to the
         * form-processing entity.
         */
        cancel,
        /**
         * The form-processing entity is returning data (e.g., search results)
         * to the form-submitting entity, or the data is a generic data set.
         */
        result
    }

    private static final String DATA_XMLS = "jabber:x:data";

    public static List<Field> parseList(final IPacket packet) {
        final List<Field> fields = new ArrayList<Field>();
        for (final IPacket fieldPacket : packet.getChildren(MatcherFactory.byName("field"))) {
            fields.add(new Field(fieldPacket));
        }
        return fields;
    }

    private IPacket x;

    public Form(final IPacket stanza) {
        super(stanza);
    }

    public List<Field> getFields() {
        return parseList(x());
    }

    public List<String> getInstructions() {
        final List<String> instructions = new ArrayList<String>();
        for (final IPacket instruction : x().getChildren(MatcherFactory.byName("instructions"))) {
            instructions.add(instruction.getText());
        }
        return instructions;
    }

    public List<Item> getItems() {
        return Item.parse(x().getChildren(MatcherFactory.byName("item")));
    }

    /**
     *In some contexts (e.g., the results of a search request), it may be
     * necessary to communicate multiple items. Therefore, a data form of type
     * "result" MAY contain two child elements not described in the basic syntax
     * above: 1. One and only <reported/> element, which can be understood as a
     * "table header" describing the data to follow. 2. Zero or more <item/>
     * elements, which can be understood as "table cells" containing data (if
     * any) that matches the request.
     */
    public Reported getReported() {
        return Reported.parse(x().getFirstChild(MatcherFactory.byName("reported")));
    }

    public String getTitle() {
        return x().getFirstChild("title").getText();
    }

    public Type getType() {
        return Type.valueOf(x().getAttribute("type"));
    }

    public void setFields(final List<Field> fields) {
        // FIXME
    }

    public void setInstructions(final List<String> instructions) {
        // FIXME
    }

    public void setItems(final List<Item> items) {
        // FIXME
    }

    public void setReported(final Reported reported) {
        // FIXME
    }

    public void setTitle(final String title) {
        super.setAttribute("type", title);
    }

    public void setType(final Type type) {
        super.setAttribute("type", type.toString());
    }

    public IPacket x() {
        if (x == null) {
            x = super.getFirstChildInDeep(MatcherFactory.byNameAndXMLNS("x", DATA_XMLS));
        }
        return x;
    }
}
