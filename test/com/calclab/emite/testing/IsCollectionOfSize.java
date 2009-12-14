/**
 *
 */
package com.calclab.emite.testing;

import java.util.Collection;

import org.mockito.ArgumentMatcher;

class IsCollectionOfSize<T> extends ArgumentMatcher<T> {
    private final int size;

    public IsCollectionOfSize(final int size) {
	this.size = size;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean matches(final Object list) {
	return ((Collection) list).size() == size;
    }
}
