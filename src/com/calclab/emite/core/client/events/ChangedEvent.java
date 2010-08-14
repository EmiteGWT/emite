package com.calclab.emite.core.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public abstract class ChangedEvent<T extends EventHandler> extends GwtEvent<T> {

    public static final class ChangeAction {
	public static final String REMOVED = "removed";
	public static final String ADDED = "added";
	public static final String MODIFIED = "modified";
	public static final String CLOSED = "closed";
	public static final String CREATED = "created";
	public static final String OPENED = "opened";
    }

    private final String changeType;
    private final Type<T> associatedType;

    public ChangedEvent(final Type<T> type, final String changeType) {
	this.associatedType = type;
	this.changeType = changeType;
	assert changeType != null : "Change type can't be null in RoomOccupantsChangedEvent";
    }

    @Override
    public Type<T> getAssociatedType() {
	return associatedType;
    }

    public String getChangeType() {
	return changeType;
    }

    public boolean is(final String changeType) {
	return changeType.equals(changeType);
    }

    public boolean isAdded() {
	return ChangeAction.ADDED.equals(changeType);
    }

    public boolean isClosed() {
	return ChangeAction.CLOSED.equals(changeType);
    }

    public boolean isCreated() {
	return ChangeAction.CREATED.equals(changeType);
    }

    public boolean isModified() {
	return ChangeAction.MODIFIED.equals(changeType);
    }

    public boolean isOpened() {
	return ChangeAction.OPENED.equals(changeType);
    }

    public boolean isRemoved() {
	return ChangeAction.REMOVED.equals(changeType);
    }

    @Override
    public String toDebugString() {
	return super.toDebugString() + changeType + ": ";
    }

}
