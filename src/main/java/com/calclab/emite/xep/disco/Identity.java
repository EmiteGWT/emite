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

package com.calclab.emite.xep.disco;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.calclab.emite.base.xml.HasXML;
import com.calclab.emite.base.xml.XMLBuilder;
import com.calclab.emite.base.xml.XMLPacket;
import com.google.common.base.Objects;

/**
 * Each <identity/> element MUST possess 'category' and 'type' attributes
 * specifying the category and type for the entity, and MAY possess a 'name'
 * attribute specifying a natural-language name for the entity
 */
@Immutable
public final class Identity implements HasXML {
	
	private final String category;
	private final String type;
	@Nullable private final String name;

	protected Identity(final String category, final String type, @Nullable final String name) {
		this.category = checkNotNull(category);
		this.type = checkNotNull(type);
		this.name = name;
	}
	
	public final String getCategory() {
		return category;
	}
	
	public final String getType() {
		return type;
	}
	
	@Nullable
	public final String getName() {
		return name;
	}
	
	@Override
	public final int hashCode() {
		return Objects.hashCode(category, type, name);
	}
	
	@Override
	public final boolean equals(Object obj) {
		if (obj instanceof Identity) {
			final Identity other = (Identity) obj;
			
			return category.equals(other.category) && type.equals(other.type) && Objects.equal(name, other.name);
		}
		
		return super.equals(obj);
	}
	
	@Override
	public final XMLPacket getXML() {
		return XMLBuilder.create("identity").attribute("category", category).attribute("type", type).attribute("name", name).getXML();
	}
	
}
