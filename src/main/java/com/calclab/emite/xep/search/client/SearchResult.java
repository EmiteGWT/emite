package com.calclab.emite.xep.search.client;

public class SearchResult<T> {

    public static enum Status {
	success, fail
    }

    public T data;
    public Status status;
    public String errorMessage;
}
