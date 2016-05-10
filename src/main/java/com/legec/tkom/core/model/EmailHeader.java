package com.legec.tkom.core.model;

import java.util.HashMap;
import java.util.Map;

public class EmailHeader {
    private Map<String, String> headerParts = new HashMap<>();

    public EmailHeader() {
    }

    public void addHeader(String key, String value){
        headerParts.put(key, value);
    }

    public boolean containsField(String name){
        return headerParts.containsKey(name);
    }
}
