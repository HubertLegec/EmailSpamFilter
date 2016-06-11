package com.legec.tkom.core.model;

import java.util.List;

import static com.legec.tkom.core.model.HeaderKey.CONTENT_DISPOSITION;

public class BodyPart {
    private EmailHeader header = new EmailHeader();
    private String body;

    public void addHeaderRow(HeaderKey key, List<String> values) {
        header.addHeaderPart(key, values);
    }

    public void setBody(String body) {
        this.body = body;
    }

    public EmailHeader getHeader() {
        return header;
    }

    public String getBody() {
        return body;
    }

    public boolean isAttachment() {
        List<String> contentDispositionValues = getHeader().getFieldValues(CONTENT_DISPOSITION);
        return contentDispositionValues != null && contentDispositionValues.contains("attachment");
    }
}
