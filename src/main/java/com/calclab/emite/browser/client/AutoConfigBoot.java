package com.calclab.emite.browser.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class AutoConfigBoot {

    @Inject
    public AutoConfigBoot(final Provider<AutoConfig> provider) {
	GWT.log("Booting auto config...");
	Scheduler.get().scheduleDeferred(new ScheduledCommand() {
	    @Override
	    public void execute() {
		GWT.log("Checking auto config");
		if (!PageAssist.isMetaFalse("emite.autoConfig")) {
		    provider.get();
		}
	    }
	});
    }
}
