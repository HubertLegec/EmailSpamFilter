package com.legec.tkom.core.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmailHeader {
    private Map<HeaderKey, List<String>> headerParts = new HashMap<>();

    public void addHeader(HeaderKey key, String value) {
        List<String> elements = headerParts.get(key);
        if (elements != null) {
            elements.add(value);
        } else {
            elements = new ArrayList<>();
            elements.add(value);
            headerParts.put(key, elements);
        }
    }

    public List<String> getFieldValues(HeaderKey key) {
        return headerParts.get(key);
    }

    public boolean isEmpty() {
        return headerParts.isEmpty();
    }
}
