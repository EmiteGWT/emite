package com.calclab.emite.xep.vcard.client;

import static com.calclab.emite.core.client.packet.MatcherFactory.byName;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;

public class VCardResponse {
    private final IPacket result;
    private VCard vcard;

    public VCardResponse(final IPacket result) {
	this.result = result;
    }

    public IQ.Type getType() {
	return IQ.getType(result);
    }

    public VCard getVCard() {
	if (vcard == null && isSuccess()) {
	    final IPacket vcardChild = result.getFirstChild(byName(VCard.VCARD));
	    if (vcardChild.getChildrenCount() > 0) {
		vcard = new VCard(vcardChild);
	    }
	}
	return vcard;
    }

    public boolean hasVCard() {
	return isSuccess() && getVCard() != null;
    }

    public boolean isError() {
	return IQ.isType(IQ.Type.error, result);
    }

    public boolean isSuccess() {
	return IQ.isSuccess(result);
    }
}
