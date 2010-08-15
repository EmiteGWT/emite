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

    public static final class ChangeEventTypes {
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
	return ChangeEventTypes.added.equals(changeType);
    }

    public boolean isClosed() {
	return ChangeEventTypes.closed.equals(changeType);
    }

    public boolean isCreated() {
	return ChangeEventTypes.created.equals(changeType);
    }

    public boolean isModified() {
	return ChangeEventTypes.modified.equals(changeType);
    }

    public boolean isOpened() {
	return ChangeEventTypes.opened.equals(changeType);
    }

    public boolean isRemoved() {
	return ChangeEventTypes.removed.equals(changeType);
    }

    @Override
    public String toDebugString() {
	return super.toDebugString() + changeType + ": ";
    }

}
