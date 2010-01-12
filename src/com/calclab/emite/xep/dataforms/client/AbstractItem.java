package com.calclab.emite.xep.dataforms.client;

import java.util.List;

import com.calclab.emite.core.client.packet.DelegatedPacket;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.Packet;

/**
 * 
 * XEP-0004 Item and Results abstract class for
 * "3.2 Multiple Items in Form Results"
 * 
 */
public class AbstractItem extends DelegatedPacket {

    /**
     * Each of these elements MUST contain one or more <field/> children.
     */
    private List<Field> fields;

    public AbstractItem(final IPacket delegate) {
        super(delegate);
    }

    public AbstractItem(final String name) {
        this(new Packet(name));
    }

    public void addField(final Field field) {
        parseFields();
        super.addChild(field);
        fields.add(field);
    }

    public List<Field> getFields() {
        parseFields();
        return fields;
    }

    private void parseFields() {
        fields = Field.parseFields(fields, this);
    }
}
