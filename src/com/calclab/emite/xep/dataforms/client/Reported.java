package com.calclab.emite.xep.dataforms.client;

import com.calclab.emite.core.client.packet.IPacket;

/**
 * 
 * XEP-0004 Reported element for "3.2 Multiple Items in Form Results", which can
 * be understood as a "table header" describing the data to follow. The
 * <reported/> element defines the data format for the result items by
 * specifying the fields to be expected for each item; for this reason, the
 * <field/> elements SHOULD possess a 'type' attribute and 'label' attribute in
 * addition to the 'var' attribute, and SHOULD NOT contain a <value/> element.
 * 
 */
public class Reported extends AbstractItem {

    static final String REPORTED = "reported";

    public Reported() {
        super(REPORTED);
    }

    public Reported(final IPacket packet) {
        super(packet);
    }

}
