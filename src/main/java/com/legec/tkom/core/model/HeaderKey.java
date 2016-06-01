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
    SENDER("Sender"),
    LIST_UNSUBSCRIBE("List-Unsubscribe");

    private String name;

    HeaderKey(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static HeaderKey getFromTokenType(TokenType type){
        return fromString(type.getPattern());
    }

    private static HeaderKey fromString(String text) {
        if (text != null) {
            for (HeaderKey k : HeaderKey.values()) {
                if (text.equalsIgnoreCase(k.name)) {
                    return k;
                }
            }
        }
        return null;
    }
}
