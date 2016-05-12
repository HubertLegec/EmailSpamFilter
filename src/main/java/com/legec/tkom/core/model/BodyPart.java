package com.legec.tkom.core.model;

public class BodyPart {
    private EmailHeader header = new EmailHeader();
    private String body;

    public EmailHeader getHeader() {
        return header;
    }

    public String getBody() {
        return body;
    }
}
