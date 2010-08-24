/**
 * 
 */
package com.calclab.emite.xep.disco.client;

import java.util.List;

public class DiscoveryManagerResponse {
    private final List<Identity> identities;

    private final List<Feature> features;

    private final List<Item> items;

    public DiscoveryManagerResponse(List<Identity> identities, List<Feature> features) {
        this(identities, features, null);
    }

    public DiscoveryManagerResponse(List<Identity> identities, List<Feature> features, List<Item> items) {
        this.identities = identities;
        this.features = features;
        this.items = items;
    }

    public DiscoveryManagerResponse(List<Item> items) {
        this(null, null, items);
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public List<Identity> getIdentities() {
        return identities;
    }

    public List<Item> getItems() {
        return items;
    }
}