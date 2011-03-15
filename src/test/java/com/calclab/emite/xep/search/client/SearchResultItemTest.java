package com.calclab.emite.xep.search.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.calclab.emite.xtesting.services.TigaseXMLService;

public class SearchResultItemTest {

    @Test
    public void shouldHaveFirstIfPresent() {
	final SearchResultItem result = SearchResultItem.parse(TigaseXMLService
		.toPacket("<item jid='juliet@capulet.com'>" + "<first>Juliet</first></item>"));
	assertEquals("juliet@capulet.com", result.getJid().toString());
	assertNull(result.getEmail());
	assertEquals("Juliet", result.getFirst());
	assertNull(result.getLast());
	assertNull(result.getNick());
    }

    @Test
    public void shouldHaveJIDIfPresent() {
	final SearchResultItem result = SearchResultItem.parse(TigaseXMLService
		.toPacket("<item jid='juliet@capulet.com'>" + "</item>"));
	assertEquals("juliet@capulet.com", result.getJid().toString());
	assertNull(result.getEmail());
	assertNull(result.getFirst());
	assertNull(result.getLast());
	assertNull(result.getNick());
    }

    @Test
    public void shouldHaveLastIfPresent() {
	final SearchResultItem result = SearchResultItem.parse(TigaseXMLService
		.toPacket("<item jid='juliet@capulet.com'>" + "<last>Capulet</last></item>"));
	assertEquals("juliet@capulet.com", result.getJid().toString());
	assertNull(result.getEmail());
	assertNull(result.getFirst());
	assertEquals("Capulet", result.getLast());
	assertNull(result.getNick());
    }
}
