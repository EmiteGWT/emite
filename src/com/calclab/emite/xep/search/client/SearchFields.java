package com.calclab.emite.xep.search.client;

import java.util.ArrayList;
import java.util.List;

public class SearchFields {
    private final List<String> searchFields;
    private String instructions;

    public SearchFields() {
	searchFields = new ArrayList<String>();
    }

    public void add(String name) {
	searchFields.add(name);
    }

    public List<String> getFieldNames() {
	return searchFields;
    }

    public String getInstructions() {
	return instructions;
    }

    public void setInstructions(String instructions) {
	this.instructions = instructions;
    }
}
