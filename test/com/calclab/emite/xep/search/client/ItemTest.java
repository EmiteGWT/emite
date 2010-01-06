package com.calclab.emite.xep.search.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.calclab.emite.testing.MockitoEmiteHelper;

public class ItemTest {

    @Test
    public void itemsShouldHaveJIDIfPresent() {
        final Item result = Item.parse(MockitoEmiteHelper.toXML("<item jid='juliet@capulet.com'>" + "</item>"));
        assertEquals("juliet@capulet.com", result.getJid().toString());
        assertNull(result.getEmail());
        assertNull(result.getFirst());
        assertNull(result.getLast());
        assertNull(result.getNick());
    }

    @Test
    public void itemsShouldHaveFirstIfPresent() {
        final Item result = Item.parse(MockitoEmiteHelper.toXML("<item jid='juliet@capulet.com'>"
                + "<first>Juliet</first></item>"));
        assertEquals("juliet@capulet.com", result.getJid().toString());
        assertNull(result.getEmail());
        assertEquals("Juliet", result.getFirst());
        assertNull(result.getLast());
        assertNull(result.getNick());
    }

    @Test
    public void itemsShouldHaveLastIfPresent() {
        final Item result = Item.parse(MockitoEmiteHelper.toXML("<item jid='juliet@capulet.com'>"
                + "<last>Capulet</last></item>"));
        assertEquals("juliet@capulet.com", result.getJid().toString());
        assertNull(result.getEmail());
        assertNull(result.getFirst());
        assertEquals("Capulet", result.getLast());
        assertNull(result.getNick());
    }
}
