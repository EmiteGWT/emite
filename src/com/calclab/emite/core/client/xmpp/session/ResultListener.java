package com.calclab.emite.core.client.xmpp.session;

public interface ResultListener<T> {
    public void onFailure(String message);

    public void onSuccess(T response);
}
