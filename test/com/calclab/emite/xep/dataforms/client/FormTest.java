package com.calclab.emite.xep.dataforms.client;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.calclab.emite.xtesting.services.TigaseXMLService;

public class FormTest {

    private static final String XEP_0004_5_1_SAMPLE_2 = "<iq from='joogle@botster.shakespeare.lit' to='romeo@montague.net/home' type='result' xml:lang='en' id='create1'> <command xmlns='http://jabber.org/protocol/commands' node='create' sessionid='create:20040408T0128Z' status='executing'> <x xmlns='jabber:x:data' type='form'> <title>Bot Configuration</title> <instructions>Fill out this form to configure your new bot!</instructions> <field type='hidden' var='FORM_TYPE'> <value>jabber:bot</value> </field> <field type='fixed'><value>Section 1: Bot Info</value></field> <field type='text-single' label='The name of your bot' var='botname'/> <field type='text-multi' label='Helpful description of your bot' var='description'/> <field type='boolean' label='Public bot?' var='public'> <required/> </field> <field type='text-private' label='Password for special access' var='password'/> <field type='fixed'><value>Section 2: Features</value></field> <field type='list-multi' label='What features will the bot support?' var='features'> <option label='Contests'><value>contests</value></option> <option label='News'><value>news</value></option> <option label='Polls'><value>polls</value></option> <option label='Reminders'><value>reminders</value></option> <option label='Search'><value>search</value></option> <value>news</value> <value>search</value> </field> <field type='fixed'><value>Section 3: Subscriber List</value></field> <field type='list-single' label='Maximum number of subscribers' var='maxsubs'> <value>20</value> <option label='10'><value>10</value></option> <option label='20'><value>20</value></option> <option label='30'><value>30</value></option> <option label='50'><value>50</value></option> <option label='100'><value>100</value></option> <option label='None'><value>none</value></option> </field> <field type='fixed'><value>Section 4: Invitations</value></field> <field type='jid-multi' label='People to invite' var='invitelist'> <desc>Tell all your friends about your new bot!</desc> </field> </x> </command> </iq>";
    private static final String XEP_0004_5_1_SAMPLE_3 = "<iq from='romeo@montague.net/home' to='joogle@botster.shakespeare.lit' type='set' xml:lang='en' id='create2'> <command xmlns='http://jabber.org/protocol/commands' node='create' sessionid='create:20040408T0128Z'> <x xmlns='jabber:x:data' type='submit'> <field type='hidden' var='FORM_TYPE'> <value>jabber:bot</value> </field> <field type='text-single' var='botname'> <value>The Jabber Google Bot</value> </field> <field type='text-multi' var='description'> <value>This bot enables you to send requests to</value> <value>Google and receive the search results right</value> <value>in your Jabber client. It&apos; really cool!</value> <value>It even supports Google News!</value> </field> <field type='boolean' var='public'> <value>0</value> </field> <field type='text-private' var='password'> <value>v3r0na</value> </field> <field type='list-multi' var='features'> <value>news</value> <value>search</value> </field> <field type='list-single' var='maxsubs'> <value>50</value> </field> <field type='jid-multi' var='invitelist'> <value>juliet@capulet.com</value> <value>benvolio@montague.net</value> </field> </x> </command> </iq>";
    private static final String XEP_0004_5_1_SAMPLE_4 = "<iq from='joogle@botster.shakespeare.lit' to='romeo@montague.net/home' type='result' xml:lang='en' id='create2'> <command xmlns='http://jabber.org/protocol/commands' node='create' sessionid='create:20040408T0128Z' status='completed'> <x xmlns='jabber:x:data' type='result'> <field type='hidden' var='FORM_TYPE'> <value>jabber:bot</value> </field> <field type='text-single' var='botname'> <value>The Jabber Google Bot</value> </field> <field type='boolean' var='public'> <value>0</value> </field> <field type='text-private' var='password'> <value>v3r0na</value> </field> <field type='list-multi' var='features'> <value>news</value> <value>search</value> </field> <field type='list-single' var='maxsubs'> <value>50</value> </field> <field type='jid-multi' var='invitelist'> <value>juliet@capulet.com</value> <value>benvolio@montague.net</value> </field> </x> </command> </iq>";
    private static final String XEP_0004_5_2_SAMPLE_6 = "<iq from='joogle@botster.shakespeare.lit' to='juliet@capulet.com/chamber' type='result' xml:lang='en' id='search1'> <command xmlns='http://jabber.org/protocol/commands' node='search' status='executing'> <x xmlns='jabber:x:data' type='form'> <title>Joogle Search</title> <instructions>Fill out this form to search for information!</instructions> <field type='text-single' var='search_request'> <required/> </field> </x> </command> </iq>";
    private static final String XEP_0004_5_2_SAMPLE_7 = "<iq from='juliet@capulet.com/chamber' to='joogle@botster.shakespeare.lit' type='get' xml:lang='en' id='search2'> <command xmlns='http://jabber.org/protocol/commands' node='search'> <x xmlns='jabber:x:data' type='submit'> <field type='text-single' var='search_request'> <value>verona</value> </field> </x> </command> </iq>";
    private static final String XEP_0004_5_2_SAMPLE_8 = "<iq from='joogle@botster.shakespeare.lit' to='juliet@capulet.com/chamber' type='result' xml:lang='en' id='search2'> <command xmlns='http://jabber.org/protocol/commands' node='search' status='completed'> <x xmlns='jabber:x:data' type='result'> <title>Joogle Search: verona</title> <reported> <field var='name'/> <field var='url'/> </reported> <item> <field var='name'> <value>Comune di Verona - Benvenuti nel sito ufficiale</value> </field> <field var='url'> <value>http://www.comune.verona.it/</value> </field> </item> <item> <field var='name'> <value>benvenuto!</value> </field> <field var='url'> <value>http://www.hellasverona.it/</value> </field> </item> <item> <field var='name'> <value>Universita degli Studi di Verona - Home Page</value> </field> <field var='url'> <value>http://www.univr.it/</value> </field> </item> <item> <field var='name'> <value>Aeroporti del Garda</value> </field> <field var='url'> <value>http://www.aeroportoverona.it/</value> </field> </item> <item> <field var='name'> <value>Veronafiere - fiera di Verona</value> </field> <field var='url'> <value>http://www.veronafiere.it/</value> </field> </item> </x> </command> </iq>";
    private static final String XEP_0154_5_3_SAMPLE_13 = "<message to='francisco@denmark.lit' from='hamlet@denmark.lit/elsinore' type='headline' id='foo'> <event xmlns='http://jabber.org/protocol/pubsub#event'> <items node='urn:xmpp:tmp:profile'> <item> <profile xmlns='urn:xmpp:tmp:profile'> <x xmlns='jabber:x:data' type='result'> <field var='weblog'> <value>http://www.denmark.lit/blogs/princely_musings</value> </field> </x> </profile> </item> </items> </event> </message>";
    private static final String SEVERAL_INSTRUCTIONS = "<iq from='joogle@botster.shakespeare.lit' to='juliet@capulet.com/chamber' type='result' xml:lang='en' id='search2'> <command xmlns='http://jabber.org/protocol/commands' node='search' status='completed'> <x xmlns='jabber:x:data'type='form'> <title/> <instructions>First</instructions> <instructions>Second</instructions> <field var='test'type='boelean'label='description'> <desc/> <required/> <value>somevalue</value> <option label='option-label'><value>some-option-value</value></option> <option label='option-label'><value>some-option-value</value></option> </field> </x></command> </iq>";

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

    private Form parse(String xml) {
	return Form.parse(TigaseXMLService.toPacket(xml));
    }

}
