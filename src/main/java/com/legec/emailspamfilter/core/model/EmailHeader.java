package com.legec.emailspamfilter.core.model;

import java.util.HashMap;
import java.util.Map;

public class EmailHeader {
    private Map<HeaderFieldType, String> headerParts = new HashMap<>();

    public EmailHeader() {
    }

    public void addHeader(HeaderFieldType key, String value){
        headerParts.put(key, value);
    }

    public boolean containsField(HeaderFieldType name){
        return headerParts.containsKey(name);
    }
}
