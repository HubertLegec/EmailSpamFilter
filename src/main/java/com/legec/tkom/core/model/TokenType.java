package com.legec.tkom.core.model;

public enum TokenType {
    DATE("Date:", true, false),
    FROM("From:", true, false),
    TO("To:", true, false),
    CC("Cc:", true, false),
    BCC("Bcc:", true, false),
    SUBJECT("Subject:", true, false),
    DELIVERED_TO("Delivered-To:", true, false),
    RECEIVED("Received:", true, false),
    RETURN_PATH("Return-Path:", true, false),
    MIME_VERSION("MIME-Version:", true, false),
    CONTENT_TYPE("Content-Type:", true, false),
    CONTENT_TRANSFER_ENCODING("Content-Transfer-Encoding:", true, false),
    MESSAGE_ID("Message-ID:", true, false),
    REPLY_TO("Reply-To:", true, false),
    SENDER("Sender:", true, false),
    VALUE("*", false, false);


    private String name;
    private boolean isHeaderKey;
    private boolean isBodyKey;

    TokenType(String name, boolean isHeaderKey, boolean isBodyKey){
        this.name = name;
        this.isHeaderKey = isHeaderKey;
        this.isBodyKey = isBodyKey;
    }

    public String getName(){
        return name;
    }

    public boolean isHeaderKey() {
        return isHeaderKey;
    }

    public boolean isBodyKey() {
        return isBodyKey;
    }

    public boolean isValue(){
        return !isHeaderKey && !isBodyKey;
    }
}
