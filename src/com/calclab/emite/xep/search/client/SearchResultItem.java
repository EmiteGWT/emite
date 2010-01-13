package com.calclab.emite.xep.search.client;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;

/**
 * Item search result
 * 
 * See: http://xmpp.org/extensions/xep-0055.html#schema
 */
public class SearchResultItem {
    public static SearchResultItem parse(final IPacket child) {
	final XmppURI jid = XmppURI.jid(child.getAttribute("jid"));
	assert jid != null;
	final IPacket firstChild = child.getFirstChild("first");
	final IPacket lastChild = child.getFirstChild("last");
	final IPacket nickChild = child.getFirstChild("nick");
	final IPacket emailChild = child.getFirstChild("email");
	return new SearchResultItem(jid, firstChild.getText(), lastChild.getText(), nickChild.getText(), emailChild
		.getText());
    }

    private final XmppURI jid;
    private String first;
    private String last;
    private String nick;
    private String email;

    /**
     * Created a search result items. All parameters can be null except jid
     * 
     * @param jid
     *            jid of the result (is a required field)
     * @param email
     * @param nick
     * @param last
     * @param first
     * 
     */
    public SearchResultItem(final XmppURI jid, String nick, String first, String last, String email) {
	assert jid != null : "SearchResultItem requires a JID";
	this.jid = jid;
	this.first = first;
	this.last = last;
	this.nick = nick;
	this.email = email;
    }

    public String getEmail() {
	return email;
    }

    public String getFirst() {
	return first;
    }

    public XmppURI getJid() {
	return jid;
    }

    public String getLast() {
	return last;
    }

    public String getNick() {
	return nick;
    }

    public void setEmail(final String email) {
	this.email = email;
    }

    public void setFirst(final String first) {
	this.first = first;
    }

    public void setLast(final String last) {
	this.last = last;
    }

    public void setNick(final String nick) {
	this.nick = nick;
    }
}
