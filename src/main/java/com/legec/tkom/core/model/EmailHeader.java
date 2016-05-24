package com.legec.tkom.core.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmailHeader {
    private Map<HeaderKey, List<String>> headerParts = new HashMap<>();

    public void addHeaderPart(HeaderKey key, List<String> values) {
        List<String> elements = headerParts.get(key);
        if (elements != null) {
            for(String value : values) {
                elements.add(value);
            }
        } else {
            elements = values;
            headerParts.put(key, elements);
        }
    }

    public List<String> getFieldValues(HeaderKey key) {
        return headerParts.get(key);
    }

    public Map<HeaderKey, List<String>> getHeaderParts() {
        return headerParts;
    }

    public boolean isEmpty() {
        return headerParts.isEmpty();
    }
}
