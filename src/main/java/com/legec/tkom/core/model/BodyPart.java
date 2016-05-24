package com.legec.tkom.core.model;

import java.util.List;

public class BodyPart {
    private EmailHeader header = new EmailHeader();
    private String body;


    public void addHeaderRow(HeaderKey key, List<String> values){
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
}
