package com.legec.tkom.core;

import com.legec.tkom.core.configuration.GlobalConfig;
import com.legec.tkom.core.model.*;

import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.legec.tkom.core.Utils.*;
import static com.legec.tkom.core.model.EmailType.*;
import static com.legec.tkom.core.model.HeaderKey.*;

class Filter {
    private static final Pattern MULTIPLE_EXCLAMATION_OR_QUESTION_MARK = Pattern.compile(".*(!{2,})|(\\?{2,}).*");
    private EmailModel model;
    private List<String> suspiciousElements = new ArrayList<>();
    private EmailType type = EmailType.OK;

    Filter(EmailModel model) {
        this.model = model;
    }

    EmailType processEmail() {
        containsListUnsubscribe();
        checkTitle();
        checkAttachmentsExtensions();
        checkServers();
        checkBodyParts();
        return type;
    }

    List<String> getSuspiciousElements() {
        return suspiciousElements;
    }

    private void checkAttachmentsExtensions() {
        List<String> suspiciousExtensions = GlobalConfig.getConfiguration().getDangerousExtensions();
        if (suspiciousExtensions.isEmpty() || !model.isMultipart()) {
            return;
        }
        List<String> attachmentNames = model.getBodyParts().stream()
                .filter(part -> isSuspiciousAttachment(part, suspiciousExtensions))
                .map(part -> "Dangerous attachment: " +  getAttachmentFileName(part))
                .collect(Collectors.toList());
        if (!attachmentNames.isEmpty()) {
            suspiciousElements.addAll(attachmentNames);
            setEmailType(SPAM);
        }
    }

    private void checkTitle() {
        List<String> suspiciousTitleWords = GlobalConfig.getConfiguration().getSuspiciousTitleWords();
        try {
            String decodedTitle = MimeUtility.decodeText(model.getEmailHeader().getFieldValues(SUBJECT).get(0));
            List<String> foundWords = suspiciousTitleWords.stream()
                    .map(String::toLowerCase)
                    .filter(word -> decodedTitle.toLowerCase().contains(word))
                    .map(word -> "Title contains word: " + word)
                    .collect(Collectors.toList());
            if (!foundWords.isEmpty()) {
                suspiciousElements.addAll(foundWords);
                if (foundWords.size() > 2) {
                    setEmailType(SPAM);
                } else {
                    setEmailType(SUSPICIOUS);
                }
            }
            if(MULTIPLE_EXCLAMATION_OR_QUESTION_MARK.matcher(decodedTitle).find()){
                suspiciousElements.add("Multiple question or exclamation marks in subject");
                if(foundWords.size() < 2){
                    setEmailType(SUSPICIOUS);
                } else {
                    setEmailType(SPAM);
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void checkServers() {
        List<String> suspiciousServers = GlobalConfig.getConfiguration().getDangerousServers();
        List<String> cumulativeRoute = new ArrayList<>();
        model.getEmailHeader().getHeaderParts().entrySet().stream()
                .filter( entry -> entry.getKey() == RECEIVED || entry.getKey() == X_RECEIVED)
                .forEach( entry -> cumulativeRoute.addAll(entry.getValue()));
        List<String> foundServers = suspiciousServers.stream()
                .filter( server ->
                        cumulativeRoute.stream().anyMatch( value -> value.contains(server))
                )
                .map( server -> "Server on email route: " + server)
                .collect(Collectors.toList());
        if(!foundServers.isEmpty()){
            suspiciousElements.addAll(foundServers);
            setEmailType(SPAM);
        }
    }

    private void checkBodyParts(){
        if(model.isMultipart()) {
            model.getBodyParts().stream()
                    .filter(part -> !isAttachment(part))
                    .forEach(part -> analyzeBodyPart(part));
        } else {
            analyzeBodyPart(model.getBodyParts().get(0));
        }
    }

    private void analyzeBodyPart(BodyPart part){
        List<String> suspiciousWords = GlobalConfig.getConfiguration().getSuspiciousWords();
        //TODO
    }

    private void containsListUnsubscribe(){
        boolean hasHeader = model.getEmailHeader().getHeaderParts()
                .entrySet().stream()
                .anyMatch( entry -> entry.getKey() == HeaderKey.LIST_UNSUBSCRIBE);
        if(hasHeader){
            setEmailType(EmailType.SPAM);
            suspiciousElements.add("Contains header: List-Unsubscribe");
        }
    }

    private boolean isAttachment(BodyPart part){
        List<String> contentDispositionValues = part.getHeader().getFieldValues(CONTENT_DISPOSITION);
        return contentDispositionValues != null && contentDispositionValues.contains("attachment");
    }

    private boolean isSuspiciousAttachment(BodyPart part, List<String> suspiciousExtensions) {
        List<String> contentDispositionValues = part.getHeader().getFieldValues(CONTENT_DISPOSITION);
        if (contentDispositionValues != null && contentDispositionValues.contains("attachment")) {
            return contentDispositionValues.stream()
                    .filter(val -> val.startsWith("filename"))
                    .map(val -> getStringBetweenQuotationMarks(val))
                    .map(val -> val.substring(val.lastIndexOf('.') + 1))
                    .anyMatch(suspiciousExtensions::contains);
        }
        return false;
    }

    private String getAttachmentFileName(BodyPart part) {
        return part.getHeader()
                .getFieldValues(CONTENT_DISPOSITION).stream()
                .filter(val -> val.startsWith("filename"))
                .map(Utils::getStringBetweenQuotationMarks)
                .findFirst()
                .get();
    }

    private void setEmailType(EmailType type){
        if(this.type == null ||
                this.type == OK ||
                (this.type == SUSPICIOUS && type == SPAM)){
            this.type = type;
        }
    }
}
