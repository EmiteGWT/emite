package com.calclab.emite.core.client.util;

import java.util.LinkedList;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.user.client.Command;

/**
 * Very simple class to implement a queue of commands which will be run as an
 * incremental command.
 * 
 * This is similar to using the normal GWT scheduler with deferred commands,
 * except that in a deferred command the execution of the command will be
 * deferred, but all the deferred commands will be executed in one pass. This
 * class, on the other hand, will execute each command separately as part of an
 * incremental command, allowing the browser's event loop to run between
 * commands.
 * 
 * Note that there is no consideration of synchronisation here as JS is single
 * threaded.
 */
public class NonBlockingCommandScheduler implements RepeatingCommand {
    private final LinkedList<Command> commandQueue;

    public NonBlockingCommandScheduler() {
	commandQueue = new LinkedList<Command>();
    }

    /**
     * Adds a command to be queued
     * 
     * @param command
     */
    public void addCommand(final Command command) {
	commandQueue.addLast(command);

	if (commandQueue.size() == 1) {
	    Scheduler.get().scheduleIncremental(this);
	}
    }

    @Override
    public boolean execute() {
	if (commandQueue.isEmpty()) {
	    // Shouldn't happen, but just in case
	    return false;
	}

	Command command = commandQueue.remove();

	command.execute();

	return (commandQueue.size() > 0);
    }
}
