/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008-2009 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emite.browser.client;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;

/**
 * In order to remove the (bosh) configuration from the source code and to
 * reduce the boiler plate code, some of the parameters and behaviours of the
 * Emite library can be specified using <meta> tags inside <html> files.
 * 
 * <h3>1. Install</h3>
 * 
 * Simply add this line:
 * 
 * <pre>
 * &lt;inherits name=&quot;com.calclab.emite.browser.EmiteBrowser&quot; /&gt;
 * </pre>
 * 
 * To your module's .gwt.xml file
 * 
 * <h3>2. Usage<h3>
 * <h4>2.1 Bosh configuration</h4>
 * You can configure your bosh configuration settings adding this lines inside
 * the head tag of your html file:
 * 
 * <pre>
 * &lt;meta name=&quot;emite.httpBase&quot; content=&quot;proxy&quot; /&gt;
 * &lt;meta name=&quot;emite.host&quot; content=&quot;localhost&quot; /&gt;
 * </pre>
 * 
 * This is completly equivalent to write this configuration code:
 * 
 * <pre>
 * Suco.get(Connection.class).setSettings(new BoshSettings(&quot;proxy&quot;, &quot;localhost&quot;));
 * </pre>
 * 
 * So, if you install this module and add those meta tags inside the html file
 * you don't longer need to writa that configuration code anymore.
 * 
 * <h4>2.2 Login and logout</h4>
 * 
 * To make BrowserModule to login and logout when enter and exits the page
 * respectively this meta tag should be placed in the html file:
 * 
 * <pre>
 * &lt;meta name=&quot;emite.session&quot; content=&quot;login&quot; /&gt;
 * </pre>
 * 
 * In order this feature to work you have to specify the user's jid and password
 * (plain text at this moment, more to come):
 * 
 * <pre>
 * &lt;meta name=&quot;emite.user&quot; content=&quot;test2@localhost&quot; /&gt;
 * &lt;meta name=&quot;emite.password&quot; content=&quot;test2&quot; /&gt;
 * </pre>
 * 
 * Automatic logout only (when page closed) is possible using the the "logout"
 * content value in this meta tag. If this option is choosen, there's no need to
 * specify user or password.
 * 
 * <h4>2.3 Pause and resume</h4>
 * 
 * To make BrowserModule pause and resume the session when enter and exit the
 * page respectively, this meta tag should be use: *
 * 
 * <pre>
 * &lt;meta name=&quot;emite.session&quot; content=&quot;resume&quot; /&gt;
 * </pre>
 * 
 * BrowserModule will pause the session and serialize it on a browser's cookie.
 * It will try to resume the session if the cookie is present.
 * 
 * <h4>2.4 Resume or login</h4>
 * 
 * To make BrowserModule to pause the session when the user leaves the page and
 * to resume the session or login if resume fails, this meta tag should be
 * placed in your html file:
 * 
 * <pre>
 * &lt;meta name=&quot;emite.session&quot; content=&quot;resumeOrLogin&quot; /&gt;
 * </pre>
 * 
 * 
 * <b>WARNING: this is work in progress and currently COMPLETLY UNSECURE. We
 * will implement the ability to encode the passwords... but is NOT currently
 * implemented. But...</b>
 * 
 * You can force an ANONYMOUS login (much more secure, but usually not enough)
 * with the following line:
 * 
 * <pre>
 * &lt;meta name=&quot;emite.user&quot; content=&quot;anonymous&quot; /&gt;
 * </pre>
 * 
 * Remember that <b>emite</b> won't autologin if a session was previously
 * paused.
 */
public class BrowserModule extends AbstractGinModule {

    @Override
    protected void configure() {
	bind(AutoConfig.class).in(Singleton.class);
	bind(AutoConfigBoot.class).asEagerSingleton();
    }
}
