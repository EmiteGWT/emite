package com.calclab.emite.xep.dataforms.client;

/**
 * XEP-0004 Field type constants (a enum it's not a good idea because the
 * boolean type, and the hyphen (-) in the types)
 * 
 */
public final class FieldType {

    /**
     * The field enables an entity to gather or provide an either-or choice
     * between two options. The default value is "false".
     */
    public static final String TYPE_BOOLEAN = "boolean";

    /**
     * The field is intended for data description (e.g., human-readable text
     * such as "section" headers) rather than data gathering or provision. The
     * <value/> child SHOULD NOT contain newlines (the \n and \r characters);
     * instead an application SHOULD generate multiple fixed fields, each with
     * one <value/> child.
     */
    public static final String TYPE_FIXED = "fixed";

    /**
     * The field is not shown to the form-submitting entity, but instead is
     * returned with the form. The form-submitting entity SHOULD NOT modify the
     * value of a hidden field, but MAY do so if such behavior is defined for
     * the "using protocol".
     */
    public static final String TYPE_HIDDEN = "hidden";

    /**
     * The field enables an entity to gather or provide multiple Jabber IDs.
     * Each provided JID SHOULD be unique (as determined by comparison that
     * includes application of the Nodeprep, Nameprep, and Resourceprep profiles
     * of Stringprep as specified in XMPP Core), and duplicate JIDs MUST be
     * ignored.
     */
    public static final String TYPE_JID_MULTI = "jid-multi";

    /** The field enables an entity to gather or provide a single Jabber ID. */
    public static final String TYPE_JID_SINGLE = "jid-single";

    /**
     * The field enables an entity to gather or provide one or more options from
     * among many. A form-submitting entity chooses one or more items from among
     * the options presented by the form-processing entity and MUST NOT insert
     * new options. The form-submitting entity MUST NOT modify the order of
     * items as received from the form-processing entity, since the order of
     * items MAY be significant.
     */
    public static final String TYPE_LIST_MULTI = "list-multi";

    /**
     * The field enables an entity to gather or provide one option from among
     * many. A form-submitting entity chooses one item from among the options
     * presented by the form-processing entity and MUST NOT insert new options.
     */
    public static final String TYPE_LIST_SINGLE = "list-single";

    /**
     * The field enables an entity to gather or provide multiple lines of text.
     */
    public static final String TYPE_TEXT_MULTI = "text-multi";

    /**
     * The field enables an entity to gather or provide a single line or word of
     * text, which shall be obscured in an interface (e.g., with multiple
     * instances of the asterisk character).
     */
    public static final String TYPE_TEXT_PRIVATE = "text-private";

    /**
     * The field enables an entity to gather or provide a single line or word of
     * text, which may be shown in an interface. This field type is the default
     * and MUST be assumed if a form-submitting entity receives a field type it
     * does not understand.
     */
    public static final String TYPE_TEXT_SINGLE = "text-single";

    FieldType() {
        // Final...
    }
}
