package com.legec.tkom.core.model;

public enum HeaderKey {
    DATE("Date"),
    FROM("From"),
    TO("To"),
    CC("Cc"),
    BCC("Bcc"),
    SUBJECT("Subject"),
    DELIVERED_TO("Delivered-To"),
    RECEIVED("Received"),
    X_RECEIVED("X-Received"),
    RETURN_PATH("Return-Path"),
    MIME_VERSION("MIME-Version"),
    CONTENT_TYPE("Content-Type"),
    CONTENT_TRANSFER_ENCODING("Content-Transfer-Encoding"),
    CONTENT_DISPOSITION("Content-Disposition"),
    MESSAGE_ID("Message-ID"),
    REPLY_TO("Reply-To"),
    SENDER("Sender");

    private String name;

    HeaderKey(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
