package com.calclab.emite.browser.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class AutoConfigBoot {

    @Inject
    public AutoConfigBoot(final Provider<AutoConfig> provider) {
	GWT.log("Booting auto config...");
	DeferredCommand.addCommand(new Command() {
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
