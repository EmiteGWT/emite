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
package com.calclab.emite.core.client.packet;

import java.util.HashMap;
import java.util.List;

/**
 * Helper class to build a nice packet representation
 */
public class PacketRenderer {
    public static String toString(final IPacket packet) {
        final StringBuffer buffer = new StringBuffer();
        toString(packet, buffer);
        return buffer.toString();
    }

    public static void toString(final IPacket root, final StringBuffer buffer) {
        final String name = root.getName();
        buffer.append("<").append(name);
        final HashMap<String, String> attributes = root.getAttributes();
        for (final String key : attributes.keySet()) {
            final String value = attributes.get(key);
            if (value != null) {
                buffer.append(" ").append(key).append("=\"");
                buffer.append(TextUtils.escape(value)).append("\"");
            }
        }

        final String rootText = root.getText();
        if (rootText != null) {
            buffer.append(">");
            buffer.append(TextUtils.escape(rootText));
            buffer.append("</").append(name).append(">");
        } else {
            final List<? extends IPacket> children = root.getChildren();
            if (children.size() > 0) {
                buffer.append(">");
                for (final IPacket child : children) {
                    toString(child, buffer);
                }
                buffer.append("</").append(name).append(">");
            } else {
                buffer.append(" />");
            }
        }
    }

}
