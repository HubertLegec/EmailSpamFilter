package com.legec.tkom.core.model;

import java.util.ArrayList;
import java.util.List;

public class EmailModel {
    private EmailHeader emailHeader = new EmailHeader();
    private List<BodyPart> bodyParts = new ArrayList<>();
    private boolean multipart = false;

    public void addMainHeaderRow(HeaderKey key, List<String> values){
        if(key == HeaderKey.CONTENT_TYPE){
            values.stream()
                    .filter( val -> val.contains("boundary"))
                    .forEach( val -> multipart = true );
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
