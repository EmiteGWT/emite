package com.calclab.emite.xfunctional.client;

import com.calclab.emite.xfunctional.client.TestOutput.Level;
import com.google.gwt.user.client.Timer;

public abstract class FunctionalTest {

    private TestOutput output;
    private long beginTime;
    private int assertions;
    private int failures;
    private int aciertos;
    private long endTime;
    private long delayedTime;

    public FunctionalTest() {
	stop();
    }

    public void debug(String message) {
	output.print(Level.debug, message);
    }

    public void delay(int milliseconds, final DelayedCode delayedCode) {
	delayedTime += milliseconds;
	debug("Waiting for " + ((float) (milliseconds / 1000)) + " seconds.");
	Timer timer = new Timer() {
	    @Override
	    public void run() {
		delayedCode.run();
	    }
	};
	timer.schedule(milliseconds);
    }

    public void equal(String message, Object expected, Object actual) {
	checkAssertion(message, expected.equals(actual));
    }

    public abstract String getName();

    public void info(String message) {
	output.print(Level.info, message);
    }

    public abstract void run();

    public void run(TestOutput output) {
	this.output = output;
	run();
    }

    public void testBegins() {
	this.beginTime = System.currentTimeMillis();
	info("BEGIN: " + getName());
    }

    public void testEnds() {
	this.endTime = System.currentTimeMillis();
	String message = "END: " + getName();
	message += " [ Assertions: " + assertions;
	message += " , Ok: " + aciertos;
	message += " , Failed: " + failures + " ] ";
	message += "It took: " + calcTime() + " milliseconds.";
	Level level = failures > 0 ? Level.fail : Level.success;
	output.print(level, message);
	stop();
    }

    private long calcTime() {
	return endTime - beginTime - delayedTime;
    }

    private void checkAssertion(String message, boolean isValid) {
	assertions++;
	if (isValid) {
	    aciertos++;
	} else {
	    failures++;
	}
	Level level = isValid ? Level.success : Level.fail;
	String prefix = isValid ? "OK: " : "FAIL :";
	output.print(level, prefix + message);
    }

    private void stop() {
	this.beginTime = 0;
	this.endTime = 0;
	this.delayedTime = 0;
	this.assertions = 0;
	this.failures = 0;
	this.aciertos = 0;
    }

}
