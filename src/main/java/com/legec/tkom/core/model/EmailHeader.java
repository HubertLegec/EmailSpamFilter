package com.legec.tkom.core.model;

import com.legec.tkom.core.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.legec.tkom.core.Utils.getStringBetweenQuotationMarks;
import static com.legec.tkom.core.model.HeaderKey.*;

public class EmailHeader {
    private Map<HeaderKey, List<String>> headerParts = new HashMap<>();

    void addHeaderPart(HeaderKey key, List<String> values) {
        List<String> elements = headerParts.get(key);
        if (elements != null) {
            elements.addAll(values);
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

    public boolean containsListUnsubscribe() {
        return getHeaderParts()
                .entrySet().stream()
                .anyMatch(entry -> entry.getKey() == LIST_UNSUBSCRIBE);
    }

    public String getAttachmentFileName() {
        return getFieldValues(CONTENT_DISPOSITION).stream()
                .filter(val -> val.startsWith("filename"))
                .map(Utils::getStringBetweenQuotationMarks)
                .findFirst()
                .orElse(null);
    }

    public String getSenderAddress() {
        return getHeaderParts()
                .entrySet().stream()
                .filter(row -> row.getKey() == FROM)
                .map(Map.Entry::getValue)
                .findFirst().orElse(new ArrayList<>())
                .stream().findFirst().orElse(null);
    }

    public String getContentType() {
        return getHeaderRowValue(TokenType.CONTENT_TYPE_VAL, CONTENT_TYPE);
    }

    public String getContentEncoding() {
        return getHeaderRowValue(TokenType.ENCODING_VAL, CONTENT_TRANSFER_ENCODING);
    }

    public String getCharset() {
        String charset = getHeaderRowValue(TokenType.CHARSET, CONTENT_TYPE);
        return charset != null ? getStringBetweenQuotationMarks(charset) : null;
    }

    private String getHeaderRowValue(TokenType tokenType, HeaderKey key) {
        Pattern pattern = Pattern.compile(tokenType.getPattern());
        return getHeaderParts()
                .entrySet().stream()
                .filter(row -> row.getKey() == key)
                .map(Map.Entry::getValue)
                .findFirst().orElse(new ArrayList<>())
                .stream()
                .filter(val -> pattern.matcher(val).find())
                .findFirst().orElse(null);
    }
}
