package com.calclab.emite.xtesting.handlers;

import com.calclab.emite.xep.muc.client.subject.RoomSubjectChangedEvent;
import com.calclab.emite.xep.muc.client.subject.RoomSubjectChangedHandler;

public class RoomSubjectChangedTestHandler extends TestHandler<RoomSubjectChangedEvent> implements
	RoomSubjectChangedHandler {

    @Override
    public void onSubjectChanged(RoomSubjectChangedEvent event) {
	addEvent(event);
    }

}
