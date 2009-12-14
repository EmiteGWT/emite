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

import com.calclab.emite.core.client.bosh.Connection;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.ioc.decorator.Singleton;
import com.calclab.suco.client.ioc.module.AbstractModule;
import com.calclab.suco.client.ioc.module.Factory;
import com.google.gwt.core.client.EntryPoint;

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
 * &lt;meta id=&quot;emite.httpBase&quot; content=&quot;proxy&quot; /&gt;
 * &lt;meta id=&quot;emite.host&quot; content=&quot;localhost&quot; /&gt;
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
 * <h4>2.2 Page close behaviour</h4>
 *
 * You can configure what is the behaviour os <b>emite</b> when the user enter
 * or leaves the page.
 *
 * The default behavour is close the session (<code>session.logout();</code>)
 * but you can change to pause the session with the following line:
 *
 * <pre>
 * &lt;meta id=&quot;emite.onBeforeUnload&quot; content=&quot;pause&quot; /&gt;
 * </pre>
 *
 * <h4>2.3 Page open behaviour</h4>
 *
 * With the BrowserModule installed, <b>emite</b> will automatically try to
 * resume the session if 1) onBeforeUnload == pause and 2) if founds a session persisted in a cookie (so the previous session was paused). Also it will try
 * to login automatically if this meta tags are found:
 *
 * <pre>
 * &lt;meta id=&quot;emite.user&quot; content=&quot;test2@localhost&quot; /&gt;
 * &lt;meta id=&quot;emite.password&quot; content=&quot;test2&quot; /&gt;
 * </pre>
 *
 * <b>WARNING: this is work in progress and currently COMPLETLY UNSECURE. We
 * will implement the ability to encode the passwords... but is NOT currently
 * implemented. But...</b>
 *
 * You can force an ANONYMOUS login (much more secure, but usually not enough)
 * with the following line:
 *
 * <pre>
 * &lt;meta id=&quot;emite.user&quot; content=&quot;anonymous&quot; /&gt;
 * </pre>
 *
 * Remember that <b>emite</b> won't autologin if a session was previously
 * paused.
 */
public class BrowserModule extends AbstractModule implements EntryPoint {

    public BrowserModule() {
        super();
    }

    public void onModuleLoad() {
        Suco.install(this);
        Suco.get(AutoConfig.class);
    }

    @Override
    protected void onInstall() {
        register(Singleton.class, new Factory<DomAssist>(DomAssist.class) {
            @Override
            public DomAssist create() {
                return new DomAssist();
            }
        }, new Factory<AutoConfig>(AutoConfig.class) {
            @Override
            public AutoConfig create() {
                return new AutoConfig($(Connection.class), $(Session.class), $(DomAssist.class));
            }
        });
    }

}
