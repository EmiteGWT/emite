package com.calclab.emite.xep.storage.client;

import com.calclab.emite.core.client.xml.HasXML;

public interface PrivateStorageManager {

	void retrieve(HasXML data, PrivateStorageResponseEvent.Handler handler);
	
	void store(HasXML data, PrivateStorageResponseEvent.Handler handler);
	
}
