package com.calclab.emite.testing;

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.mockito.Mockito;

import com.calclab.emite.core.client.bosh.Connection;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.testing.services.TigaseXMLService;
import com.calclab.suco.client.events.Listener;

public class ConnectionTestHelper {

    public static class EventTester<A> extends BaseMatcher<Listener<A>> {
	private Listener<A> listener;

	public EventTester() {
	}

	public void describeTo(final Description description) {
	    description.appendText("event");
	}

	public Listener<A> fire(final A parameter) {
	    listener.onEvent(parameter);
	    return getListener();
	}

	public Listener<A> getListener() {
	    return Mockito.argThat(this);
	}

	@SuppressWarnings("unchecked")
	public boolean matches(final Object arg0) {
	    try {
		this.listener = (Listener<A>) arg0;
	    } catch (final ClassCastException e) {
		return false;
	    }
	    return true;
	}

	public <M> M mock(final Class<M> classToMock) {
	    return mock(Mockito.mock(classToMock));
	}

	public <M> M mock(final M mock) {
	    return Mockito.verify(mock);
	}
    }
    public final Connection connection;
    private EventTester<IPacket> onStanzaEvent;

    private final TigaseXMLService xmler;

    public ConnectionTestHelper() {
	xmler = new TigaseXMLService();
	connection = new ConnectionTester();
    }

    public Connection getConnection() {
	return connection;
    }

    public void simulateReception(final IPacket packet) {
	if (onStanzaEvent == null) {
	    replaceOnStanzaEvent();
	}
	onStanzaEvent.fire(packet);
    }

    public void simulateReception(final String received) {
	final IPacket packet = xmler.toXML(received);
	simulateReception(packet);
    }

    public void verifySentLike(final IPacket packet) {
	verify(connection).send(argThat(new IsPacketLike(packet)));
    }

    private void replaceOnStanzaEvent() {
	onStanzaEvent = new EventTester<IPacket>();
	verify(connection).onStanzaReceived(argThat(onStanzaEvent));
    }
}
