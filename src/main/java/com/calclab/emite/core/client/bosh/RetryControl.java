package com.calclab.emite.core.client.bosh;

public class RetryControl {

    public int maxRetries = 8;
    
    public int retry(int nbErrors){
	return 500 + (nbErrors - 1) * nbErrors * 550;
    }
    
}
