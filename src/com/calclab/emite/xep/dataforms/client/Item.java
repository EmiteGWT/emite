package com.calclab.emite.xep.dataforms.client;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * XEP-0004 Item for "3.2 Multiple Items in Form Results"
 * 
 */
public class Item {

    /**
     * Each of these elements MUST contain one or more <field/> children.
     */
    private final List<Field> fields = new ArrayList<Field>();
}
