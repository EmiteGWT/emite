package com.calclab.suco.example.mvc.client;

import com.calclab.suco.client.events.Listener;
import com.calclab.suco.client.events.Listener0;

public class Controller {
    public Controller(final Model model, final View view) {
	model.onchanged(new Listener<Integer>() {
	    @Override
	    public void onEvent(Integer count) {
		view.print("Clicked " + count + " times.");
	    }
	});
		
	view.onclicked(new Listener0() {
	    @Override
	    public void onEvent() {
		model.increment();
	    }
	});
    }
}
