package com.legec.emailspamfilter.core.model;

public enum HeaderFieldType {
    DATE("Date:"),
    FROM("From:"),
    TO("To:"),
    CC("Cc:"),
    BCC("Bcc:"),
    SUBJECT("Subject:"),
    DELIVERED_TO("Delivered-To:"),
    RECEIVED("Received:"),
    RETURN_PATH("Return-Path:"),
    MIME_VERSION("MIME-Version:"),
    CONTENT_TYPE("Content-Type:"),
    CONTENT_TRANSFER_ENCODING("Content-Transfer-Encoding:"),
    MESSAGE_ID("Message-ID:"),
    REPLY_TO("Reply-To:"),
    SENDER("Sender:");


    private String name;

    HeaderFieldType(String name){

    }

    public String getName(){
        return name;
    }
}
