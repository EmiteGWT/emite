package com.calclab.emite.xep.storage.client;

public interface PrivateStorageManager {

	void retrieve(SimpleStorageData data, PrivateStorageResponseEvent.Handler handler);
	
	void store(SimpleStorageData data, PrivateStorageResponseEvent.Handler handler);
	
}
