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
package com.calclab.emite.xtesting.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tigase.xml.Element;

import com.calclab.emite.core.client.packet.AbstractPacket;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.PacketRenderer;
import com.calclab.emite.core.client.packet.TextUtils;

public class TigasePacket extends AbstractPacket {

    private final Element delegate;

    public TigasePacket(final Element element) {
        this.delegate = element;
    }

    public TigasePacket(final String name) {
        this(new Element(name));
    }

    public IPacket addChild(final IPacket child) {
        final TigasePacket tigaseChild = (TigasePacket) child;
        delegate.addChild(tigaseChild.delegate);
        return child;
    }

    public IPacket addChild(final String nodeName) {
        return addChild(nodeName, null);
    }

    public IPacket addChild(final String nodeName, final String xmlns) {
        final TigasePacket child = new TigasePacket(nodeName);
        child.setAttribute("xmlns", xmlns);
        addChild(child);
        return child;
    }

    public String getAttribute(final String name) {
        return delegate.getAttribute(name);
    }

    public HashMap<String, String> getAttributes() {
        final HashMap<String, String> atts = new HashMap<String, String>();
        final Map<String, String> src = delegate.getAttributes();
        if (src != null) {
            atts.putAll(src);
        }
        return atts;
    }

    public List<? extends IPacket> getChildren() {
        final List<Element> children = delegate.getChildren();
        return wrap(children);
    }

    public int getChildrenCount() {
        final List<Element> children = delegate.getChildren();
        return children != null ? children.size() : 0;
    }

    public String getName() {
        return delegate.getName();
    }

    public String getText() {
        return TextUtils.unescape(delegate.getCData());
    }

    public boolean removeChild(final IPacket child) {
        return delegate.removeChild(((TigasePacket) child).delegate);
    }

    public void render(final StringBuffer buffer) {
        buffer.append(delegate.toString());
    }

    public void setAttribute(final String name, final String value) {
        delegate.setAttribute(name, value);
    }

    public void setText(final String text) {
        delegate.setCData(text);
    }

    @Override
    public String toString() {
        return PacketRenderer.toString(this);
    }

    private List<IPacket> wrap(final List<Element> children) {
        final ArrayList<IPacket> result = new ArrayList<IPacket>();
        if (children != null) {
            for (final Element e : children) {
                result.add(new TigasePacket(e));
            }
        }
        return result;
    }
}
