package com.legec.tkom.core;

import com.legec.tkom.core.configuration.GlobalConfig;
import com.legec.tkom.core.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Filter {
    private EmailModel model;
    private List<String> suspiciousElements = new ArrayList<>();
    private EmailType type;

    Filter(EmailModel model) {
        this.model = model;
    }

    EmailType processEmail() {
        checkTitle();
        checkSuspiciousExtensions();
        return type;
    }

    List<String> getSuspiciousElements() {
        return suspiciousElements;
    }

    private void checkSuspiciousExtensions() {
        List<String> suspiciousExtensions = GlobalConfig.getConfiguration().getDangerousExtensions();
        if (suspiciousExtensions.isEmpty() || !model.isMultipart()) {
            return;
        }
        List<String> attachmentNames = model.getBodyParts().stream()
                .filter(part -> isSuspiciousAttachment(part, suspiciousExtensions))
                .map( part -> getAttachmentFileName(part))
                .collect(Collectors.toList());
        if(!attachmentNames.isEmpty()){
            suspiciousElements.addAll(attachmentNames);
            type = EmailType.SPAM;
        }
    }

    private void checkTitle(){
        List<String> suspiciousTitleWords = GlobalConfig.getConfiguration().getSuspiciousTitleWords();
        if(suspiciousTitleWords.isEmpty()){
            return;
        }

    }

    private boolean isSuspiciousAttachment(BodyPart part, List<String> suspiciousExtensions) {
        EmailHeader header = part.getHeader();
        List<String> contentDispositionValues = header.getFieldValues(HeaderKey.CONTENT_DISPOSITION);
        if (contentDispositionValues != null && contentDispositionValues.contains("attachment")) {
            return contentDispositionValues.stream()
                    .filter(val -> val.startsWith("filename"))
                    .map(val -> Utils.getStringBetweenQuotationMarks(val))
                    .map(val -> val.substring(val.lastIndexOf('.') + 1))
                    .anyMatch(suspiciousExtensions::contains);
        }
        return false;
    }

    private String getAttachmentFileName(BodyPart part){
        return part.getHeader()
                .getFieldValues(HeaderKey.CONTENT_DISPOSITION).stream()
                .filter( val -> val.startsWith("filename"))
                .map(Utils::getStringBetweenQuotationMarks)
                .findFirst()
                .get();
    }
}
