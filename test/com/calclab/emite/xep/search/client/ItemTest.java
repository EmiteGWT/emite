package com.calclab.emite.xep.search.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.calclab.emite.testing.MockitoEmiteHelper;

public class ItemTest {

    @Test
    public void onlyJid() {
        final Item result = Item.parse(MockitoEmiteHelper.toXML("<item jid='juliet@capulet.com'>" + "</item>"));
        assertEquals("juliet@capulet.com", result.getJid().toString());
        assertNull(result.getEmail());
        assertNull(result.getFirst());
        assertNull(result.getLast());
        assertNull(result.getNick());
    }

    @Test
    public void onlyJidAndFirst() {
        final Item result = Item.parse(MockitoEmiteHelper.toXML("<item jid='juliet@capulet.com'>"
                + "<first>Juliet</first></item>"));
        assertEquals("juliet@capulet.com", result.getJid().toString());
        assertNull(result.getEmail());
        assertEquals("Juliet", result.getFirst());
        assertNull(result.getLast());
        assertNull(result.getNick());
    }

    @Test
    public void onlyJidAndLast() {
        final Item result = Item.parse(MockitoEmiteHelper.toXML("<item jid='juliet@capulet.com'>"
                + "<last>Capulet</last></item>"));
        assertEquals("juliet@capulet.com", result.getJid().toString());
        assertNull(result.getEmail());
        assertNull(result.getFirst());
        assertEquals("Capulet", result.getLast());
        assertNull(result.getNick());
    }
}
