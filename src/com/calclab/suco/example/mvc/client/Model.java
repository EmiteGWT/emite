package com.calclab.suco.example.mvc.client;

import com.calclab.suco.client.events.Event;
import com.calclab.suco.client.events.Listener;


public class Model {
    private int counter;
    private Event<Integer> changed;

    public Model() {
	this.counter = 0;
	this.changed = new Event<Integer>("changed");
    }
    
    public void increment() {
	counter++;
	changed.fire(counter);
    }
    
    public void onchanged(Listener<Integer> listener) {
	changed.add(listener);
    }
}
