package com.calclab.emite.xep.disco.client;

/**
 * A callback to know if one or more features are supported by an entity
 * 
 * @see DiscoveryManager
 */
public interface FeatureSupportedCallback {

    /**
     * This method is called when the DiscoveryManager knows if some features
     * are supported for a given entity
     * 
     * @param areFeaturedSupported
     */
    void onFeaturesSupported(boolean areFeaturedSupported);

}
