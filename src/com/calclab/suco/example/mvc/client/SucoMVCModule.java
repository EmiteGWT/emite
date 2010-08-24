package com.calclab.suco.example.mvc.client;

import com.calclab.suco.client.ioc.decorator.Singleton;
import com.calclab.suco.client.ioc.module.AbstractModule;
import com.calclab.suco.client.ioc.module.Factory;

public class SucoMVCModule extends AbstractModule  {

    @Override
    protected void onInstall() {
	// only one view
	register(Singleton.class, new Factory<View>(View.class) {

	    @Override
	    public View create() {
		return new ViewGWT();
	    }
	    
	    // all views have an associated controller and model
	    @Override
	    public void onAfterCreated(View view) {
		new Controller(new Model(), view);
	    }
	});
    }


}
