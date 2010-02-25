package com.calclab.emite.core.client.xmpp.stanzas;

public class XmppUriParser {
    private static final String PREFIX = "xmpp:";
    private static final int PREFIX_LENGTH = PREFIX.length();

    public static final String REGEXP_NODE = "^[a-z0-9\\.\\-_\\+]+$";

    // http://www.shauninman.com/archive/2006/05/08/validating_domain_names
    private static final String REGEXP_DOMAIN = "^([a-z0-9]([-a-z0-9]*[a-z0-9])?\\.)+((a[cdefgilmnoqrstuwxz]|aero|arpa)|(b[abdefghijmnorstvwyz]|biz)|(c[acdfghiklmnorsuvxyz]|cat|com|coop)|d[ejkmoz]|(e[ceghrstu]|edu)|f[ijkmor]|(g[abdefghilmnpqrstuwy]|gov)|h[kmnrtu]|(i[delmnoqrst]|info|int)|(j[emop]|jobs)|k[eghimnprwyz]|l[abcikrstuvy]|(m[acdghklmnopqrstuvwxyz]|mil|mobi|museum)|(n[acefgilopruz]|name|net)|(om|org)|(p[aefghklmnrstwy]|pro)|qa|r[eouw]|s[abcdeghijklmnortvyz]|(t[cdfghjklmnoprtvwz]|travel)|u[agkmsyz]|v[aceginu]|w[fs]|y[etu]|z[amw])$";

    public static String getDomain(final String uri) {
	final int atIndex = uri.indexOf('@') + 1;
	final int barIndex = uri.indexOf('/', atIndex);
	if (atIndex == barIndex) {
	    return null;
	}
	return barIndex > 0 ? uri.substring(atIndex, barIndex) : uri.substring(atIndex);
    }

    public static String getNode(final String uri) {
	final int begin = uri.startsWith(PREFIX) ? PREFIX_LENGTH : 0;
	final int atIndex = uri.indexOf('@');
	if (atIndex >= 0) {
	    return uri.substring(begin, atIndex);
	}
	return null;
    }

    public static boolean isValidDomain(final String domain) {
	return domain != null && domain.matches(REGEXP_DOMAIN);
    }

    public static boolean isValidJid(final String uri) {
	final String node = getNode(uri);
	if (!isValidNode(node)) {
	    return false;
	}
	final String domain = getDomain(uri);
	if (!isValidDomain(domain)) {
	    return false;
	}
	return getResource(uri) == null;
    }

    public static boolean isValidNode(final String node) {
	return node != null && node.matches(REGEXP_NODE);
    }

    public static String removePrefix(final String xmppUri) {
	return xmppUri.startsWith(PREFIX) ? xmppUri.substring(PREFIX_LENGTH) : xmppUri;
    }

    static String getResource(final String uri) {
	final int atIndex = uri.indexOf('@') + 1;
	final int barIndex = uri.indexOf('/', atIndex);
	return barIndex > 0 ? uri.substring(barIndex + 1) : null;
    }
}
