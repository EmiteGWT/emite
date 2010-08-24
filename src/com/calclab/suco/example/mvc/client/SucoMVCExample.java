package com.calclab.suco.example.mvc.client;

import com.calclab.suco.client.Suco;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class SucoMVCExample implements EntryPoint {
    public void onModuleLoad() {
	Suco.install(new SucoMVCModule());
	RootPanel.get("app").add((Widget) Suco.get(View.class));
    }
}
