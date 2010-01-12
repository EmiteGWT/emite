package com.calclab.emite.xep.dataforms.client;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.calclab.emite.xtesting.services.TigaseXMLService;

public class FieldTest {

    @Test
    public void testBasicFieldsParsing() {
	final Field field = parse("<field type='jid-multi' label='People to invite'"
		+ "var='invitelist'><desc>Tell all your friends about your new bot!</desc></field>");
	assertEquals(FieldType.JID_MULTI, field.getType());
	assertEquals("People to invite", field.getLabel());
	assertEquals("invitelist", field.getVar());
	assertEquals("Tell all your friends about your new bot!", field.getDesc());
    }

    @Test
    public void testOptionsParsing() {
	final Field field = parse("<field type='list-single'" + " label='Maximum number of subscribers' var='maxsubs'>"
		+ " <value>20</value> <option label='10'><value>10</value></option>"
		+ " <option label='20'><value>20</value></option> <option label='30'><value>30</value></option>"
		+ " <option label='50'><value>50</value></option> <option label='100'><value>100</value></option>"
		+ " <option label='None'><value>none</value></option></field>");
	assertEquals(FieldType.LIST_SINGLE, field.getType());
	assertEquals("Maximum number of subscribers", field.getLabel());
	assertEquals("maxsubs", field.getVar());
	assertEquals(null, field.getDesc());
	final List<String> values = field.getValues();
	assertEquals(1, values.size());
	assertEquals("20", values.get(0));
	final List<Option> options = field.getOptions();
	assertEquals(6, options.size());
	assertEquals("None", options.get(5).getLabel());
	assertEquals("none", options.get(5).getValue());
    }

    private Field parse(String stanza) {
	return Field.singleParse(TigaseXMLService.toPacket(stanza));
    }
}
