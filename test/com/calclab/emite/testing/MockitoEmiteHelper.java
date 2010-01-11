package com.calclab.emite.testing;

import static org.mockito.Matchers.argThat;

import java.util.Collection;
import java.util.List;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.testing.services.TigaseXMLService;

@SuppressWarnings("unchecked")
public class MockitoEmiteHelper {

    public static final TigaseXMLService xmler = new TigaseXMLService();

    public static List hasSame(final List list) {
	return argThat(new IsCollectionLike<List>(list));
    }

    public static Collection isCollectionOfSize(final int size) {
	return argThat(new IsCollectionOfSize<Collection>(size));
    }

    public static List isListOfSize(final int size) {
	return argThat(new IsCollectionOfSize<List>(size));
    }

    public static IPacket packetLike(final IPacket packet) {
	return argThat(new IsPacketLike(packet));
    }

    public static IPacket packetLike(final String xml) {
	return packetLike(toXML(xml));
    }

    public static IPacket toXML(final String xml) {
	return xmler.toXML(xml);
    }

}
