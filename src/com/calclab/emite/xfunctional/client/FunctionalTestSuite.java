package com.calclab.emite.xfunctional.client;

public interface FunctionalTestSuite {
    void afterLogin(Context ctx);

    void beforeLogin(Context ctx);

    void duringLogin(Context ctx);

    String getName();

}
