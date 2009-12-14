/**
 *
 */
package com.calclab.emite.testing;

import java.util.Collection;

import org.mockito.ArgumentMatcher;

@SuppressWarnings("unchecked")
class IsCollectionLike<T extends Collection> extends ArgumentMatcher<T> {
    private final T expected;

    public IsCollectionLike(final T list) {
	this.expected = list;
    }

    @Override
    public boolean matches(final Object argument) {
	final T actual = (T) argument;
	final Object[] actualArray = actual.toArray();
	final Object[] expectedArray = expected.toArray();
	if (actualArray.length != expectedArray.length) {
	    return false;
	}

	for (int index = 0; index < expectedArray.length; index++) {
	    if (!expectedArray[index].equals(actualArray[index])) {
		return false;
	    }
	}
	return true;
    }

}
