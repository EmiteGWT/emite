package com.calclab.emite.suco.client;

import com.calclab.emite.browser.client.BrowserGinjector;
import com.calclab.emite.core.client.CoreGinjector;
import com.calclab.emite.im.client.ImGinjector;
import com.calclab.emite.reconnect.client.ReconnectGinjector;
import com.calclab.emite.xep.avatar.client.AvatarGinjector;
import com.calclab.emite.xep.chatstate.client.ChatStateGinjector;
import com.calclab.emite.xep.disco.client.DiscoveryGinjector;
import com.calclab.emite.xep.muc.client.MucGinjector;
import com.calclab.emite.xep.mucchatstate.client.MucChatStateGinjector;
import com.calclab.emite.xep.mucdisco.client.MucDiscoveryGinjector;
import com.calclab.emite.xep.search.client.SearchGinjector;
import com.calclab.emite.xep.storage.client.PrivateStorageGinjector;
import com.calclab.emite.xep.vcard.client.VCardGinjector;

public interface EmiteSucoGinjector extends CoreGinjector, ChatStateGinjector, ImGinjector, ReconnectGinjector,
	AvatarGinjector, DiscoveryGinjector, MucGinjector, MucChatStateGinjector, MucDiscoveryGinjector,
	SearchGinjector, PrivateStorageGinjector, VCardGinjector, BrowserGinjector {
}
