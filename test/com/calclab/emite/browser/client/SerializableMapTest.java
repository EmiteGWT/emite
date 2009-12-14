package com.calclab.emite.browser.client;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SerializableMapTest {

    @Test
    public void shouldSerialize() {
	final SerializableMap original = new SerializableMap();
	original.put("name1", "value1");
	original.put("name2", "value2");
	final String serialized = original.serialize();
	final SerializableMap copy = SerializableMap.restore(serialized);
	assertEquals(original, copy);
    }
}
