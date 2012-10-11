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

import javax.annotation.concurrent.Immutable;

import com.calclab.emite.base.xml.HasXML;
import com.calclab.emite.base.xml.XMLBuilder;
import com.calclab.emite.base.xml.XMLPacket;
import com.google.common.base.Objects;

@Immutable
public final class Feature implements HasXML {

	private final String var;

	protected Feature(final String var) {
		this.var = checkNotNull(var);
	}
	
	public final String getVar() {
		return var;
	}
	
	@Override
	public final int hashCode() {
		return Objects.hashCode(var);
	}
	
	@Override
	public final boolean equals(Object obj) {
		if (obj instanceof Feature) {
			final Feature other = (Feature) obj;
			
			return var.equals(other.var);
		}
		
		return super.equals(obj);
	}
	
	@Override
	public final XMLPacket getXML() {
		return XMLBuilder.create("feature").attribute("var", var).getXML();
	}
	
}
