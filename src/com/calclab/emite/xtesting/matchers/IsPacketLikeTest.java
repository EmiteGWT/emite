package com.calclab.emite.xtesting.matchers;

import static com.calclab.emite.xtesting.matchers.EmiteAsserts.assertNotPacketLike;
import static com.calclab.emite.xtesting.matchers.EmiteAsserts.assertPacketLike;

import org.junit.Test;

public class IsPacketLikeTest {

    @Test
    public void testAttributes() {
	assertPacketLike("<iq type='result' />", "<iq type='result' />");
	assertNotPacketLike("<iq type='result' />", "<iq type='result2' />");
	assertPacketLike("<iq type='result' />", "<iq type='result' value='something here' />");
    }

    @Test
    public void testChildren() {
	assertNotPacketLike("<iq><child att='value' /></iq>", "<iq></iq>");
	assertNotPacketLike("<iq><child att='value' /></iq>", "<iq><child/></iq>");
	assertPacketLike("<iq><child att='value' /></iq>", "<iq><child att='value' /><child2 /></iq>");
    }

    @Test
    public void testChildrenText() {
	assertNotPacketLike("<presence><show>chat</show></presence>", "<presence><show>dnd</show></presence>");
    }

    @Test
    public void testMultipleChildren() {
	assertPacketLike("<root><child2/></root>", "<root><child1/><child2/></root>");
	assertPacketLike("<root><child1/></root>", "<root><child1/><child2/></root>");
	assertNotPacketLike("<root><child3/></root>", "<root><child1/><child2/></root>");
    }

    @Test
    public void testNestedChildren() {
	assertPacketLike("<iq><child/></iq>", "<iq><child><subchild/></child></iq>");
    }

}
