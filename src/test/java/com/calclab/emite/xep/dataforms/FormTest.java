/*
 * ((e)) emite: A pure Google Web Toolkit XMPP library
 * Copyright (c) 2008-2011 The Emite development team
 * 
 * This file is part of Emite.
 *
 * Emite is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * Emite is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with Emite.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.calclab.emite.xep.dataforms;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.base.xml.XMLBuilder;
import com.calclab.emite.core.XmppURI;
import com.calclab.emite.xep.dataforms.Field;
import com.calclab.emite.xep.dataforms.FieldType;
import com.calclab.emite.xep.dataforms.Form;
import com.calclab.emite.xep.dataforms.Item;
import com.calclab.emite.xep.dataforms.Reported;
import com.calclab.emite.xtesting.XmppSessionTester;

public class FormTest {

	private static final String XEP_0004_5_1_SAMPLE_2 = "<iq from='joogle@botster.shakespeare.lit' to='romeo@montague.net/home' type='result' xml:lang='en' id='create1'> <command xmlns='http://jabber.org/protocol/commands' node='create' sessionid='create:20040408T0128Z' status='executing'> <x xmlns='jabber:x:data' type='form'> <title>Bot Configuration</title> <instructions>Fill out this form to configure your new bot!</instructions> <field type='hidden' var='FORM_TYPE'> <value>jabber:bot</value> </field> <field type='fixed'><value>Section 1: Bot Info</value></field> <field type='text-single' label='The name of your bot' var='botname'/> <field type='text-multi' label='Helpful description of your bot' var='description'/> <field type='boolean' label='Public bot?' var='public'> <required/> </field> <field type='text-private' label='Password for special access' var='password'/> <field type='fixed'><value>Section 2: Features</value></field> <field type='list-multi' label='What features will the bot support?' var='features'> <option label='Contests'><value>contests</value></option> <option label='News'><value>news</value></option> <option label='Polls'><value>polls</value></option> <option label='Reminders'><value>reminders</value></option> <option label='Search'><value>search</value></option> <value>news</value> <value>search</value> </field> <field type='fixed'><value>Section 3: Subscriber List</value></field> <field type='list-single' label='Maximum number of subscribers' var='maxsubs'> <value>20</value> <option label='10'><value>10</value></option> <option label='20'><value>20</value></option> <option label='30'><value>30</value></option> <option label='50'><value>50</value></option> <option label='100'><value>100</value></option> <option label='None'><value>none</value></option> </field> <field type='fixed'><value>Section 4: Invitations</value></field> <field type='jid-multi' label='People to invite' var='invitelist'> <desc>Tell all your friends about your new bot!</desc> </field> </x> </command> </iq>";
	private static final String XEP_0004_5_1_SAMPLE_3 = "<iq from='romeo@montague.net/home' to='joogle@botster.shakespeare.lit' type='set' xml:lang='en' id='create2'> <command xmlns='http://jabber.org/protocol/commands' node='create' sessionid='create:20040408T0128Z'> <x xmlns='jabber:x:data' type='submit'> <field type='hidden' var='FORM_TYPE'> <value>jabber:bot</value> </field> <field type='text-single' var='botname'> <value>The Jabber Google Bot</value> </field> <field type='text-multi' var='description'> <value>This bot enables you to send requests to</value> <value>Google and receive the search results right</value> <value>in your Jabber client. It&apos; really cool!</value> <value>It even supports Google News!</value> </field> <field type='boolean' var='public'> <value>0</value> </field> <field type='text-private' var='password'> <value>v3r0na</value> </field> <field type='list-multi' var='features'> <value>news</value> <value>search</value> </field> <field type='list-single' var='maxsubs'> <value>50</value> </field> <field type='jid-multi' var='invitelist'> <value>juliet@capulet.com</value> <value>benvolio@montague.net</value> </field> </x> </command> </iq>";
	private static final String XEP_0004_5_1_SAMPLE_4 = "<iq from='joogle@botster.shakespeare.lit' to='romeo@montague.net/home' type='result' xml:lang='en' id='create2'> <command xmlns='http://jabber.org/protocol/commands' node='create' sessionid='create:20040408T0128Z' status='completed'> <x xmlns='jabber:x:data' type='result'> <field type='hidden' var='FORM_TYPE'> <value>jabber:bot</value> </field> <field type='text-single' var='botname'> <value>The Jabber Google Bot</value> </field> <field type='boolean' var='public'> <value>0</value> </field> <field type='text-private' var='password'> <value>v3r0na</value> </field> <field type='list-multi' var='features'> <value>news</value> <value>search</value> </field> <field type='list-single' var='maxsubs'> <value>50</value> </field> <field type='jid-multi' var='invitelist'> <value>juliet@capulet.com</value> <value>benvolio@montague.net</value> </field> </x> </command> </iq>";
	private static final String XEP_0004_5_2_SAMPLE_6 = "<iq from='joogle@botster.shakespeare.lit' to='juliet@capulet.com/chamber' type='result' xml:lang='en' id='search1'> <command xmlns='http://jabber.org/protocol/commands' node='search' status='executing'> <x xmlns='jabber:x:data' type='form'> <title>Joogle Search</title> <instructions>Fill out this form to search for information!</instructions> <field type='text-single' var='search_request'> <required/> </field> </x> </command> </iq>";
	private static final String XEP_0004_5_2_SAMPLE_7 = "<iq from='juliet@capulet.com/chamber' to='joogle@botster.shakespeare.lit' type='get' xml:lang='en' id='search2'> <command xmlns='http://jabber.org/protocol/commands' node='search'> <x xmlns='jabber:x:data' type='submit'> <field type='text-single' var='search_request'> <value>verona</value> </field> </x> </command> </iq>";
	private static final String XEP_0004_5_2_SAMPLE_8 = "<iq from='joogle@botster.shakespeare.lit' to='juliet@capulet.com/chamber' type='result' xml:lang='en' id='search2'> <command xmlns='http://jabber.org/protocol/commands' node='search' status='completed'> <x xmlns='jabber:x:data' type='result'> <title>Joogle Search: verona</title> <reported> <field var='name'/> <field var='url'/> </reported> <item> <field var='name'> <value>Comune di Verona - Benvenuti nel sito ufficiale</value> </field> <field var='url'> <value>http://www.comune.verona.it/</value> </field> </item> <item> <field var='name'> <value>benvenuto!</value> </field> <field var='url'> <value>http://www.hellasverona.it/</value> </field> </item> <item> <field var='name'> <value>Universita degli Studi di Verona - Home Page</value> </field> <field var='url'> <value>http://www.univr.it/</value> </field> </item> <item> <field var='name'> <value>Aeroporti del Garda</value> </field> <field var='url'> <value>http://www.aeroportoverona.it/</value> </field> </item> <item> <field var='name'> <value>Veronafiere - fiera di Verona</value> </field> <field var='url'> <value>http://www.veronafiere.it/</value> </field> </item> </x> </command> </iq>";
	private static final String XEP_0154_5_3_SAMPLE_13 = "<message to='francisco@denmark.lit' from='hamlet@denmark.lit/elsinore' type='headline' id='foo'> <event xmlns='http://jabber.org/protocol/pubsub#event'> <items node='urn:xmpp:tmp:profile'> <item> <profile xmlns='urn:xmpp:tmp:profile'> <x xmlns='jabber:x:data' type='result'> <field var='weblog'> <value>http://www.denmark.lit/blogs/princely_musings</value> </field> </x> </profile> </item> </items> </event> </message>";
	private static final String SEVERAL_INSTRUCTIONS = "<iq from='joogle@botster.shakespeare.lit' to='juliet@capulet.com/chamber' type='result' xml:lang='en' id='search2'> <command xmlns='http://jabber.org/protocol/commands' node='search' status='completed'> <x xmlns='jabber:x:data'type='form'> <title/> <instructions>First</instructions> <instructions>Second</instructions> <field var='test'type='boelean'label='description'> <desc/> <required/> <value>somevalue</value> <option label='option-label'><value>some-option-value</value></option> <option label='option-label'><value>some-option-value</value></option> </field> </x></command> </iq>";
	private static final String XEP_0055_2_1_SAMPLE_1 = "<iq type='get' from='romeo@montague.net/home' to='characters.shakespeare.lit' id='search1' xml:lang='en'>  <query xmlns='jabber:iq:search'/></iq>";

	private XmppSessionTester session;

	@Test
	public void parseFieldsInIQ() {
		final Form result = parse(XEP_0004_5_1_SAMPLE_2);
		final List<Field> fields = result.getFields();
		assertEquals(12, fields.size());
		assertEquals("hidden", fields.get(0).getType());
		assertEquals("FORM_TYPE", fields.get(0).getVar());
		assertEquals("jabber:bot", fields.get(0).getValues().get(0));
		assertEquals("Tell all your friends about your new bot!", fields.get(11).getDesc());
	}

	@Test
	public void parseFieldsInMessage() {
		final Form result = parse(XEP_0154_5_3_SAMPLE_13);
		final List<Field> fields = result.getFields();
		assertEquals(1, fields.size());
		assertEquals(null, fields.get(0).getType());
		assertEquals("weblog", fields.get(0).getVar());
		assertEquals("http://www.denmark.lit/blogs/princely_musings", fields.get(0).getValues().get(0));
	}

	@Test
	public void parseInstructions() {
		final Form result1 = parse(XEP_0004_5_1_SAMPLE_2);
		final Form result2 = parse(XEP_0004_5_1_SAMPLE_3);
		final Form result4 = parse(XEP_0004_5_2_SAMPLE_6);
		assertEquals("Fill out this form to configure your new bot!", result1.getInstructions().get(0));
		assertEquals(0, result2.getInstructions().size());
		assertEquals("Fill out this form to search for information!", result4.getInstructions().get(0));
	}

	@Test
	public void parseReported() {
		final Form result = parse(XEP_0004_5_2_SAMPLE_8);
		final Reported reported = result.getReported();
		final List<Field> fields = reported.getFields();
		assertEquals(2, fields.size());
		assertEquals("name", fields.get(0).getVar());
		assertEquals("url", fields.get(1).getVar());
	}

	@Test
	public void parseSearchResults() {
		final Form result = parse(XEP_0004_5_2_SAMPLE_8);
		final List<Item> items = result.getItems();
		assertEquals(5, items.size());
		final List<Field> fields1 = items.get(0).getFields();
		assertEquals(2, fields1.size());
		assertEquals("name", fields1.get(0).getVar());
		assertEquals("url", fields1.get(1).getVar());
		assertEquals("Comune di Verona - Benvenuti nel sito ufficiale", fields1.get(0).getValues().get(0));
		assertEquals("http://www.comune.verona.it/", fields1.get(1).getValues().get(0));
		final List<Field> fields4 = items.get(4).getFields();
		assertEquals("Veronafiere - fiera di Verona", fields4.get(0).getValues().get(0));
		assertEquals("http://www.veronafiere.it/", fields4.get(1).getValues().get(0));
	}

	@Test
	public void parseSearchWithoutForm() {
		final Form form = parse(XEP_0055_2_1_SAMPLE_1);
		assertEquals(form.x(), NoPacket.INSTANCE);
	}

	@Test
	public void parseTitle() {
		final Form result1 = parse(XEP_0004_5_1_SAMPLE_2);
		final Form result2 = parse(XEP_0004_5_1_SAMPLE_3);
		final Form result3 = parse(XEP_0004_5_1_SAMPLE_4);
		final Form result4 = parse(XEP_0004_5_2_SAMPLE_6);
		final Form result5 = parse(XEP_0004_5_2_SAMPLE_7);
		final Form result6 = parse(XEP_0004_5_2_SAMPLE_8);
		assertEquals("Bot Configuration", result1.getTitle());
		assertEquals(null, result2.getTitle());
		assertEquals(null, result3.getTitle());
		assertEquals("Joogle Search", result4.getTitle());
		assertEquals(null, result5.getTitle());
		assertEquals("Joogle Search: verona", result6.getTitle());
	}

	@Test
	public void parseType() {
		final Form result1 = parse(XEP_0004_5_1_SAMPLE_2);
		final Form result2 = parse(XEP_0004_5_1_SAMPLE_3);
		final Form result3 = parse(XEP_0004_5_1_SAMPLE_4);
		final Form result4 = parse(XEP_0004_5_2_SAMPLE_6);
		final Form result5 = parse(XEP_0004_5_2_SAMPLE_7);
		final Form result6 = parse(XEP_0004_5_2_SAMPLE_8);
		final Form result7 = parse(XEP_0154_5_3_SAMPLE_13);
		assertEquals(Form.Type.form, result1.getType());
		assertEquals(Form.Type.submit, result2.getType());
		assertEquals(Form.Type.result, result3.getType());
		assertEquals(Form.Type.form, result4.getType());
		assertEquals(Form.Type.submit, result5.getType());
		assertEquals(Form.Type.result, result6.getType());
		assertEquals(Form.Type.result, result7.getType());
	}

	@Before
	public void setUp() {
		session = new XmppSessionTester();
		session.setLoggedIn(XmppURI.uri("romeo@montague.net/home"));
		session.setReady();
	}

	@Test
	public void testFormReportedAndItems() {
		final Form form = new Form(Form.Type.result);
		form.setTitle("Joogle Search: verona");
		form.addToReported(new Field().Var("name"));
		form.addToReported(new Field().Var("url"));
		form.addItem(new Item().WithField(new Field().Var("name").Value("Comune di Verona - Benvenuti nel sito ufficiale")).WithField(
				new Field().Var("url").Value("http://www.comune.verona.it/")));
		form.addItem(new Item().WithField(new Field().Var("name").Value("benvenuto!")).WithField(new Field().Var("url").Value("http://www.hellasverona.it/")));
		form.addItem(new Item().WithField(new Field().Var("name").Value("Universita degli Studi di Verona - Home Page")).WithField(
				new Field().Var("url").Value("http://www.univr.it/")));
		form.addItem(new Item().WithField(new Field().Var("name").Value("Aeroporti del Garda")).WithField(
				new Field().Var("url").Value("http://www.aeroportoverona.it/")));
		form.addItem(new Item().WithField(new Field().Var("name").Value("Veronafiere - fiera di Verona")).WithField(
				new Field().Var("url").Value("http://www.veronafiere.it/")));
		session.send(form);
		session.verifySent(parse(XEP_0004_5_2_SAMPLE_8).x());
	}

	@Test
	public void testFormResult() {
		final Form form = new Form(Form.Type.result);
		form.WithField(new Field(FieldType.HIDDEN).Var("FORM_TYPE").Value("jabber:bot"));
		form.WithField(new Field(FieldType.TEXT_SINGLE).Var("botname").Value("The Jabber Google Bot"));
		form.WithField(new Field(FieldType.BOOLEAN).Var("public").Value("0"));
		form.WithField(new Field(FieldType.TEXT_PRIVATE).Var("password").Value("v3r0na"));
		form.WithField(new Field(FieldType.LIST_MULTI).Var("features").Value("news").Value("search"));
		form.WithField(new Field(FieldType.LIST_SINGLE).Var("maxsubs").Value("50"));
		form.WithField(new Field(FieldType.JID_MULTI).Var("invitelist").Value("juliet@capulet.com").Value("benvolio@montague.net"));
		session.send(form);
		session.verifySent(parse(XEP_0004_5_1_SAMPLE_4).x());
	}

	@Test
	public void testFormSearch() {
		final Form form = new Form(Form.Type.submit).WithField(new Field(FieldType.TEXT_SINGLE).Var("search_request").Value("verona"));
		session.send(form);
		session.verifySent(parse(XEP_0004_5_2_SAMPLE_7).x());
	}

	/**
	 * The OPTIONAL <title/> and <instructions/> elements enable the
	 * form-processing entity to label the form as a whole and specify
	 * natural-language instructions to be followed by the form-submitting
	 * entity. The XML character data for these elements SHOULD NOT contain
	 * newlines (the \n and \r characters), and any handling of newlines (e.g.,
	 * presentation in a user interface) is unspecified herein; however,
	 * multiple instances of the <instructions/> element MAY be included.
	 */
	@Test
	public void testMultipleInstructions() {
		final Form result = parse(SEVERAL_INSTRUCTIONS);
		assertEquals("First", result.getInstructions().get(0));
		assertEquals("Second", result.getInstructions().get(1));
		assertEquals(2, result.getInstructions().size());
	}

	@Test
	public void testTypeForm() {
		final Form form = new Form(Form.Type.form).WithField(new Field(FieldType.TEXT_SINGLE).Var("search_request").Required(true));
		form.setTitle("Joogle Search");
		form.addInstruction("Fill out this form to search for information!");
		session.send(form);
		session.verifySent(parse(XEP_0004_5_2_SAMPLE_6).x());
	}

	private Form parse(final String xml) {
		return new Form(XMLBuilder.fromXML(xml));
	}

}
