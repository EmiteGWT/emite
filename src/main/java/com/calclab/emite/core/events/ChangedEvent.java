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

package com.calclab.emite.core.events;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.web.bindery.event.shared.Event;

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
public abstract class ChangedEvent<H> extends Event<H> {

	public static enum ChangeType {
		removed, added, modified, closed, created, opened;
	}

	private final ChangeType changeType;

	protected ChangedEvent(final ChangeType changeType) {
		this.changeType = checkNotNull(changeType);
	}

	public ChangeType getChangeType() {
		return changeType;
	}

	public boolean is(final ChangeType changeType) {
		return this.changeType.equals(changeType);
	}

	public boolean isAdded() {
		return ChangeType.added.equals(changeType);
	}

	public boolean isClosed() {
		return ChangeType.closed.equals(changeType);
	}

	public boolean isCreated() {
		return ChangeType.created.equals(changeType);
	}

	public boolean isModified() {
		return ChangeType.modified.equals(changeType);
	}

	public boolean isOpened() {
		return ChangeType.opened.equals(changeType);
	}

	public boolean isRemoved() {
		return ChangeType.removed.equals(changeType);
	}

	@Override
	public String toDebugString() {
		return super.toDebugString() + changeType + ": ";
	}

}
