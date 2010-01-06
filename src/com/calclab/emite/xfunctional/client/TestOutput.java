package com.calclab.emite.xfunctional.client;

public interface TestOutput {

    enum Level { info, debug, fail, success }
    
    void print(Level level, String message);

}
