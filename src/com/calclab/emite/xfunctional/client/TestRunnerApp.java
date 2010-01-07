package com.calclab.emite.xfunctional.client;

import com.calclab.emite.xfunctional.client.tests.ConnectionTests;
import com.calclab.emite.xfunctional.client.tests.SearchTests;
import com.calclab.emite.xfunctional.client.tests.TestDiscovery;
import com.calclab.emite.xfunctional.client.ui.TestRunnerPanel;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class TestRunnerApp implements EntryPoint {

    @Override
    public void onModuleLoad() {
	GWT.log("TestRunnerApp loaded!", null);
	final TestRunnerPanel runner = new TestRunnerPanel();

	// add tests here
	runner.addTestSuite("Connection", new ConnectionTests());
	runner.addTestSuite("Search", new SearchTests());
	runner.addTestSuite("Disco", new TestDiscovery());

	RootLayoutPanel.get().add(runner);
    }

}
