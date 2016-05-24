package com.legec.tkom.core.model;

import java.util.ArrayList;
import java.util.List;

public class EmailModel {
    private EmailHeader emailHeader = new EmailHeader();
    private List<BodyPart> bodyParts = new ArrayList<>();
    private boolean multipart = false;
    private String boundary;

    public void addMainHeaderRow(HeaderKey key, List<String> values){
        if(key == HeaderKey.CONTENT_TYPE){
            values.stream()
                    .filter( val -> val.contains("boundary"))
                    .forEach( val -> {
                        multipart = true;
                        boundary = val.substring(val.indexOf('"') + 1, val.lastIndexOf('"'));
                    });
        }
        emailHeader.addHeaderPart(key, values);
    }

    public boolean isMultipart() {
        return multipart;
    }

    public EmailHeader getEmailHeader() {
        return emailHeader;
    }

    public List<BodyPart> getBodyParts() {
        return bodyParts;
    }
}
