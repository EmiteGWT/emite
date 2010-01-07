package com.calclab.emite.xfunctional.client;

import java.util.HashMap;

public interface FunctionalTestSuite {
    public HashMap<String, FunctionalTest> getTests();

    void afterLogin(Context ctx);

    void beforeLogin(Context ctx);

}
