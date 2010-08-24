package com.calclab.suco.testing.events;

import static com.calclab.suco.testing.events.Eventito.anyListener;
import static com.calclab.suco.testing.events.Eventito.anyListener0;
import static com.calclab.suco.testing.events.Eventito.anyListener2;
import static com.calclab.suco.testing.events.Eventito.fire;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mockito.Mockito;

import com.calclab.suco.client.events.Listener;
import com.calclab.suco.client.events.Listener0;
import com.calclab.suco.client.events.Listener2;

public class EventitoTests {

    public static interface Publisher {
	public void onEvent(final Listener<String> listener);
    }

    public static interface Publisher0 {
	public void onEvent(final Listener0 listener);
    }

    public static interface Publisher2 {
	public void onEvent(final Listener2<String, String> listener);
    }

    @Test
    public void shouldFireEvent() {
	final Publisher publisher = Mockito.mock(Publisher.class);
	final MockedListener<String> listener = new MockedListener<String>();
	publisher.onEvent(listener);
	fire("message").when(publisher).onEvent(anyListener(String.class));
	assertEquals(1, listener.getCalledTimes());
	assertEquals("message", listener.getValue(0));
    }

    @Test
    public void shouldFireEvent0() {
	final Publisher0 publisher = Mockito.mock(Publisher0.class);
	final MockedListener0 listener = new MockedListener0();
	publisher.onEvent(listener);

	fire().when(publisher).onEvent(anyListener0());
	assertEquals(1, listener.getCalledTimes());
    }

    @Test
    public void shouldFireEvent2() {
	final Publisher2 publisher = Mockito.mock(Publisher2.class);
	final MockedListener2<String, String> listener = new MockedListener2<String, String>();
	publisher.onEvent(listener);

	fire("something", "here").when(publisher).onEvent(anyListener2(String.class, String.class));
	assertEquals(1, listener.getCalledTimes());
	assertTrue(listener.isCalledWithEquals("something", "here"));
    }
}
