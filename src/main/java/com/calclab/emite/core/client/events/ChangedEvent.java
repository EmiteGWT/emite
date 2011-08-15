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

package com.calclab.emite.core.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * An abstract change event. The event is parametrized with the thing
 * susceptible to be changed.
 * 
 * The ChangeAction provide some common glossary of changes. Any other can be
 * used to be open to new extensions.
 * 
 * @param <T>
 *            The object type that changed
 */
public abstract class ChangedEvent<T extends EventHandler> extends GwtEvent<T> {

	public static final class ChangeTypes {
		public static final String removed = "removed";
		public static final String added = "added";
		public static final String modified = "modified";
		public static final String closed = "closed";
		public static final String created = "created";
		public static final String opened = "opened";
	}

	private final String changeType;
	private final Type<T> associatedType;

	public ChangedEvent(final Type<T> type, final String changeType) {
		assert changeType != null : "Change type can't be null in ChangedEvent";
		this.associatedType = type;
		this.changeType = changeType;
	}

	@Override
	public Type<T> getAssociatedType() {
		return associatedType;
	}

	public String getChangeType() {
		return changeType;
	}

	public boolean is(final String changeType) {
		return this.changeType.equals(changeType);
	}

	public boolean isAdded() {
		return ChangeTypes.added.equals(changeType);
	}

	public boolean isClosed() {
		return ChangeTypes.closed.equals(changeType);
	}

	public boolean isCreated() {
		return ChangeTypes.created.equals(changeType);
	}

	public boolean isModified() {
		return ChangeTypes.modified.equals(changeType);
	}

	public boolean isOpened() {
		return ChangeTypes.opened.equals(changeType);
	}

	public boolean isRemoved() {
		return ChangeTypes.removed.equals(changeType);
	}

	@Override
	public String toDebugString() {
		return super.toDebugString() + changeType + ": ";
	}

}
