package com.calclab.emite.xfunctional.client.tests;

import java.util.HashMap;

import com.calclab.emite.xfunctional.client.Context;
import com.calclab.emite.xfunctional.client.FunctionalTest;
import com.calclab.emite.xfunctional.client.FunctionalTestSuite;

public abstract class BasicTestSuite implements FunctionalTestSuite {
    private HashMap<String, FunctionalTest> tests;

    public BasicTestSuite() {
    }

    public void add(String name, FunctionalTest test) {
	assert test != null;
	tests.put(name, test);
    }

    @Override
    public void afterLogin(Context ctx) {
    }

    @Override
    public void beforeLogin(Context ctx) {
    }

    @Override
    public HashMap<String, FunctionalTest> getTests() {
	tests = new HashMap<String, FunctionalTest>();
	registerTests();
	return tests;
    }

    public abstract void registerTests();
}
