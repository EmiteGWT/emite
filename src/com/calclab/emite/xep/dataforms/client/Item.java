package com.calclab.emite.xep.dataforms.client;

import com.calclab.emite.core.client.packet.IPacket;

/**
 * 
 * XEP-0004 Item for "3.2 Multiple Items in Form Results"
 * 
 */
public class Item extends AbstractItem {

    static final String ITEM = "item";

    /**
     * Each of these elements MUST contain one or more <field/> children.
     */

    public Item() {
        super(ITEM);
    }

    public Item(final IPacket packet) {
        super(packet);
    }

    public Item WithField(final Field field) {
        addField(field);
        return this;
    }
}
