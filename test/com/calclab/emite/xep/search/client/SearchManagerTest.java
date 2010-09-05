package com.calclab.emite.xep.search.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;

import com.calclab.emite.core.client.xmpp.session.ResultListener;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xep.dataforms.client.Field;
import com.calclab.emite.xep.dataforms.client.FieldType;
import com.calclab.emite.xep.dataforms.client.Form;
import com.calclab.emite.xtesting.XmppSessionTester;

public class SearchManagerTest {
    private static final String XEP_0055_2_1_SAMPLE_2 = "<iq type='result' from='characters.shakespeare.lit' to='romeo@montague.net/home' id='search1' xml:lang='en'><query xmlns='jabber:iq:search'> <instructions>Fill in one or more fields to search for any matching Jabber users.</instructions> <first/> <last/> <nick/> <email/></query></iq>";
    private static final String XEP_0055_3_SAMPLE_7 = "<iq type='result' from='characters.shakespeare.lit' to='juliet@capulet.com/balcony' id='search3' xml:lang='en'> <query xmlns='jabber:iq:search'> <instructions>Use the enclosed form to search. If your Jabber client does not support Data Forms, visit http://shakespeare.lit/ </instructions> <x xmlns='jabber:x:data' type='form'> <title>User Directory Search</title> <instructions>Please provide the following information to search for Shakespearean characters.</instructions> <field type='hidden' var='FORM_TYPE'> <value>jabber:iq:search</value> </field> <field type='text-single' label='Given Name' var='first'/> <field type='text-single' label='Family Name' var='last'/> <field type='list-single' label='Gender' var='x-gender'> <option label='Male'><value>male</value></option> <option label='Female'><value>female</value></option> </field> </x> </query> </iq>";

    private XmppSessionTester session;
    private SearchManager manager;

    @Before
    public void setUp() {
	session = new XmppSessionTester();
	manager = new SearchManagerImpl(session);
	manager.setHost(XmppURI.uri("search.service"));
	session.setLoggedIn(XmppURI.uri("romeo@montague.net/home"));
	session.setReady();
    }

    /**
     * @see http://xmpp.org/extensions/xep-0055.html#usecases-search
     */
    @SuppressWarnings("unchecked")
    @Test
    public void shouldRequestAndReceiveSearchFields() {
	final ResultListener<SearchFields> result = Mockito.mock(ResultListener.class);
	manager.requestSearchFields(result);
	session.verifyIQSent("<iq type='get' from='romeo@montague.net/home' to='search.service'"
		+ "xml:lang='en'> <query xmlns='jabber:iq:search'/> </iq>");
	session.answer(XEP_0055_2_1_SAMPLE_2);
	Mockito.verify(result, Mockito.never()).onFailure(Mockito.anyString());
	Mockito.verify(result).onSuccess(Mockito.argThat(new ArgumentMatcher<SearchFields>() {
	    @Override
	    public boolean matches(final Object arg0) {
		final SearchFields response = (SearchFields) arg0;
		final List<String> fields = response.getFieldNames();
		assertTrue(fields.contains("first"));
		assertTrue(fields.contains("last"));
		assertTrue(fields.contains("nick"));
		assertTrue(fields.contains("email"));
		assertFalse(fields.contains("instructions"));
		return true;
	    }
	}));

    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldRequestAndReceiveSearchForm() {
	final ResultListener<Form> result = Mockito.mock(ResultListener.class);
	manager.requestSearchForm(result);
	session.answer(XEP_0055_3_SAMPLE_7);
	Mockito.verify(result, Mockito.never()).onFailure(Mockito.anyString());
	Mockito.verify(result).onSuccess(Mockito.argThat(new ArgumentMatcher<Form>() {
	    @Override
	    public boolean matches(final Object arg0) {
		final Form response = (Form) arg0;
		assertEquals("User Directory Search", response.getTitle());
		assertEquals("Please provide the following information to search for Shakespearean characters.",
			response.getInstructions().get(0));
		return true;
	    }
	}));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldRequestAndReceiveSearchFormWhenNoFormReturned() {
	final ResultListener<Form> result = Mockito.mock(ResultListener.class);
	manager.requestSearchForm(result);
	session.answer(XEP_0055_2_1_SAMPLE_2);
	Mockito.verify(result, Mockito.never()).onFailure(Mockito.anyString());
	Mockito.verify(result).onSuccess(Mockito.argThat(new ArgumentMatcher<Form>() {
	    @Override
	    public boolean matches(final Object arg0) {
		final Form response = (Form) arg0;
		assertEquals(4, response.getFields().size());
		assertEquals("Fill in one or more fields to search for any matching Jabber users.", response
			.getInstructions().get(0));
		return true;
	    }
	}));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldReturnAnEmptyListIfNotResultFounded() {
	final ResultListener<List<SearchResultItem>> result = Mockito.mock(ResultListener.class);
	manager.search(new HashMap<String, String>(), result);
	session
		.answer("<iq type='result' from='characters.shakespeare.lit' to='romeo@montague.net/home' id='search2' xml:lang='en'>"
			+ "<query xmlns='jabber:iq:search'/></iq>");
	Mockito.verify(result, Mockito.never()).onFailure(Mockito.anyString());
	Mockito.verify(result).onSuccess(Mockito.argThat(new ArgumentMatcher<List<SearchResultItem>>() {
	    @Override
	    public boolean matches(final Object arg0) {
		final List<SearchResultItem> list = (List<SearchResultItem>) arg0;
		assertTrue(list.isEmpty());
		return true;
	    }
	}));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSearch() {
	final HashMap<String, String> query = new HashMap<String, String>();
	query.put("last", "Capulet");
	final ResultListener<List<SearchResultItem>> result = Mockito.mock(ResultListener.class);
	manager.search(query, result);

	session.verifyIQSent("<iq type='set' from='romeo@montague.net/home' to='search.service' xml:lang='en'>"
		+ "<query xmlns='jabber:iq:search'> <last>Capulet</last> </query></iq>");
	session
		.answer("<iq type='result' from='characters.shakespeare.lit' to='romeo@montague.net/home' id='search2' xml:lang='en'>"
			+ "<query xmlns='jabber:iq:search'><item jid='juliet@capulet.com'>"
			+ "<first>Juliet</first><last>Capulet</last><nick>JuliC</nick>"
			+ "<email>juliet@shakespeare.lit</email></item>"
			+ "<item jid='tybalt@shakespeare.lit'><first>Tybalt</first>"
			+ "<last>Capulet</last><nick>ty</nick>"
			+ "<email>tybalt@shakespeare.lit</email></item></query></iq>");
	Mockito.verify(result, Mockito.never()).onFailure(Mockito.anyString());
	Mockito.verify(result).onSuccess(Mockito.argThat(new ArgumentMatcher<List<SearchResultItem>>() {
	    @Override
	    public boolean matches(final Object arg0) {
		final List<SearchResultItem> list = (List<SearchResultItem>) arg0;
		final SearchResultItem searchResultItem1 = list.get(0);
		final SearchResultItem searchResultItem2 = list.get(1);
		assertEquals(2, list.size());
		assertEquals("Juliet", searchResultItem1.getFirst());
		assertEquals("Capulet", searchResultItem1.getLast());
		assertEquals("JuliC", searchResultItem1.getNick());
		assertEquals("juliet@shakespeare.lit", searchResultItem1.getEmail());
		assertEquals("juliet@capulet.com", searchResultItem1.getJid().toString());
		assertEquals("tybalt@shakespeare.lit", searchResultItem2.getJid().toString());
		return true;
	    }
	}));
    }

    @SuppressWarnings("unchecked")
    @Test
    /**
     * Example 8 and 9 of XEP-0055
     */
    public void testSearchUsingForms() {
	final Form form = new Form(Form.Type.submit);
	form.addField(new Field(FieldType.HIDDEN).Var("FORM_TYPE").Value(SearchManagerImpl.IQ_SEARCH));
	form.addField(new Field().Var("x-gender").Value("male"));
	final ResultListener<Form> result = Mockito.mock(ResultListener.class);
	manager.search(form, result);

	session.verifyIQSent("<iq type='set' from='romeo@montague.net/home' to='search.service' xml:lang='en'>"
		+ "<query xmlns='jabber:iq:search'><x xmlns='jabber:x:data' type='submit'>"
		+ "<field type='hidden' var='FORM_TYPE'><value>jabber:iq:search</value>"
		+ "</field><field var='x-gender'><value>male</value></field></x></query></iq>");
	session
		.answer("<iq type='result'     from='characters.shakespeare.lit'    to='juliet@capulet.com/balcony'    id='search4'    xml:lang='en'>  <query xmlns='jabber:iq:search'>    <x xmlns='jabber:x:data' type='result'>      <field type='hidden' var='FORM_TYPE'>        <value>jabber:iq:search</value>      </field>      <reported>        <field var='first' label='Given Name' type='text-single'/>        <field var='last' label='Family Name' type='text-single'/>        <field var='jid' label='Jabber ID' type='jid-single'/>        <field var='x-gender' label='Gender' type='list-single'/>      </reported>      <item>        <field var='first'><value>Benvolio</value></field>        <field var='last'><value>Montague</value></field>        <field var='jid'><value>benvolio@montague.net</value></field>        <field var='x-gender'><value>male</value></field>      </item>      <item>        <field var='first'><value>Romeo</value></field>        <field var='last'><value>Montague</value></field>        <field var='jid'><value>romeo@montague.net</value></field>        <field var='x-gender'><value>male</value></field>      </item>    </x>  </query></iq>");
	Mockito.verify(result, Mockito.never()).onFailure(Mockito.anyString());
	Mockito.verify(result).onSuccess(Mockito.argThat(new ArgumentMatcher<Form>() {
	    @Override
	    public boolean matches(final Object arg0) {
		final Form response = (Form) arg0;
		assertEquals(2, response.getItems().size());
		assertEquals(4, response.getReported().getFields().size());
		return true;
	    }
	}));
    }

}
