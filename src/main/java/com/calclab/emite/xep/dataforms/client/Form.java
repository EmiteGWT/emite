package com.calclab.emite.xep.dataforms.client;

import java.util.ArrayList;
import java.util.List;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.MatcherFactory;
import com.calclab.emite.core.client.packet.NoPacket;
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
    private static final String X = "x";
    private static final String TYPE = "type";
    private static final String TITLE = "title";
    private static final String INSTRUCTIONS = "instructions";

    List<String> instructions;
    private IPacket x;
    List<Field> fields;
    List<Item> items;
    Reported reported;

    public Form(final IPacket stanza) {
        super(stanza);
    }

    public Form(final Type type) {
        super(X, DATA_XMLS);
        setType(type);
    }

    public void addField(final Field field) {
        parseFields();
        fields.add(field);
        super.addChild(field);
    }

    public void addInstruction(final String instruction) {
        parseInstructions();
        instructions.add(instruction);
        super.addChild(INSTRUCTIONS).setText(instruction);
    }

    public void addItem(final Item item) {
        parseItems();
        items.add(item);
        super.addChild(item);
    }

    public void addToReported(final Field field) {
        parseReported();
        IPacket reportedPacket = getReportedPacket();
        if (reportedPacket == NoPacket.INSTANCE) {
            reportedPacket = super.addChild(Reported.REPORTED);
        }
        reportedPacket.addChild(field);
        reported.addChild(field);
    }

    public List<Field> getFields() {
        parseFields();
        return fields;
    }

    public List<String> getInstructions() {
        parseInstructions();
        return instructions;
    }

    public List<Item> getItems() {
        parseItems();
        return items;
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
        parseReported();
        return reported;
    }

    public String getTitle() {
        return x().getFirstChild(TITLE).getText();
    }

    public Type getType() {
        return Type.valueOf(x().getAttribute(TYPE));
    }

    public void setTitle(final String title) {
        setTextToChild(TITLE, title);
    }

    public void setType(final Type type) {
        super.setAttribute(TYPE, type.toString());
    }

    public Form WithField(final Field field) {
        addField(field);
        return this;
    }

    public IPacket x() {
        if (x == null) {
            x = super.getFirstChildInDeep(MatcherFactory.byNameAndXMLNS(X, DATA_XMLS));
        }
        return x;
    }

    private IPacket getReportedPacket() {
        return x().getFirstChild(MatcherFactory.byName(Reported.REPORTED));
    }

    private void parseFields() {
        fields = Field.parseFields(fields, x());
    }

    private void parseInstructions() {
        if (instructions == null) {
            instructions = new ArrayList<String>();
            for (final IPacket instruction : x().getChildren(MatcherFactory.byName(INSTRUCTIONS))) {
                instructions.add(instruction.getText());
            }
        }
    }

    private void parseItems() {
        if (items == null) {
            items = new ArrayList<Item>();
            for (final IPacket itemPacket : x().getChildren(MatcherFactory.byName(Item.ITEM))) {
                items.add(new Item(itemPacket));
            }
        }
    }

    private void parseReported() {
        if (reported == null) {
            reported = new Reported(getReportedPacket());
        }
    }
}
