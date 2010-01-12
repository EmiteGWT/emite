package com.calclab.emite.xep.dataforms.client;

import java.util.ArrayList;
import java.util.List;

import com.calclab.emite.core.client.packet.IPacket;

/**
 * 
 * XEP-0004 Item for "3.2 Multiple Items in Form Results"
 * 
 */
public class Item {

    public static Item parse(final IPacket packet) {
        final Item item = new Item();
        item.setFields(Field.parseList(packet));
        return item;
    }

    public static List<Item> parse(final List<? extends IPacket> children) {
        final List<Item> list = new ArrayList<Item>();
        for (final IPacket child : children) {
            list.add(parse(child));
        }
        return list;
    }

    /**
     * Each of these elements MUST contain one or more <field/> children.
     */
    @Deprecated
    // ?
    private List<Field> fields;

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(final List<Field> fields) {
        this.fields = fields;
    }

}
