package com.calclab.suco.example.mvc.client;

import com.calclab.suco.client.events.Listener0;

public interface View {
    void print(String body);
    void onclicked(Listener0 listener);
}
