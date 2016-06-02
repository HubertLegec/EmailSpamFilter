package com.legec.tkom.core;

import com.legec.tkom.core.configuration.GlobalConfig;
import com.legec.tkom.core.model.BodyPart;
import com.legec.tkom.core.model.EmailHeader;
import com.legec.tkom.core.model.EmailModel;
import com.legec.tkom.core.model.EmailType;

import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.legec.tkom.core.Utils.getStringBetweenQuotationMarks;
import static com.legec.tkom.core.model.EmailType.*;
import static com.legec.tkom.core.model.HeaderKey.*;

class Filter {
    private static final Pattern MULTIPLE_EXCLAMATION_OR_QUESTION_MARK = Pattern.compile(".*(!{2,})|(\\?{2,}).*");
    private EmailModel model;
    private List<String> suspiciousElements = new ArrayList<>();
    private EmailType type = EmailType.OK;
    private EmailRouteChecker routeChecker;

    private int suspiciousInTitle = 0;
    private int suspiciousInBody = 0;

    Filter(EmailModel model) {
        this.model = model;
        this.routeChecker = new EmailRouteChecker(this, model.getEmailHeader());
    }

    EmailType processEmail() {
        containsListUnsubscribe();
        senderAddressContainsNoReplay();
        checkTitle();
        checkAttachmentsExtensions();
        checkServers();
        checkBodyParts();
        if (GlobalConfig.getConfiguration().isCheckRoute()) {
            routeChecker.check();
        }
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
                .map(part -> "Dangerous attachment: " + part.getHeader().getAttachmentFileName())
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
            if (MULTIPLE_EXCLAMATION_OR_QUESTION_MARK.matcher(decodedTitle).find()) {
                suspiciousElements.add("Multiple question or exclamation marks in subject");
                suspiciousInTitle = foundWords.size();
                if (foundWords.size() < 2) {
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
                .filter(entry -> entry.getKey() == RECEIVED || entry.getKey() == X_RECEIVED)
                .forEach(entry -> cumulativeRoute.addAll(entry.getValue()));
        List<String> foundServers = suspiciousServers.stream()
                .filter(server ->
                        cumulativeRoute.stream().anyMatch(value -> value.contains(server))
                )
                .map(server -> "Server on email route: " + server)
                .collect(Collectors.toList());
        if (!foundServers.isEmpty()) {
            suspiciousElements.addAll(foundServers);
            setEmailType(SPAM);
        }
    }

    private void checkBodyParts() {
        if (model.isMultipart()) {
            model.getBodyParts().stream()
                    .filter(part -> !part.isAttachment())
                    .forEach(part -> analyzeBodyPart(part, part.getHeader()));
        } else {
            analyzeBodyPart(model.getBodyParts().get(0), model.getEmailHeader());
        }
    }

    private void analyzeBodyPart(BodyPart part, EmailHeader header) {
        String contentType = header.getContentType();
        String contentEncoding = header.getContentEncoding();
        String charset = header.getCharset();
        if (contentEncoding != null && charset != null) {
            String decodedBody = Utils.decodeString(part.getBody(), contentEncoding, charset);
            analyzeBodyPartCriteria(decodedBody, contentType);
        } else {
            analyzeBodyPartCriteria(part.getBody(), contentType);
        }
    }

    private void analyzeBodyPartCriteria(String bodyContent, String contentType) {
        List<String> suspiciousWords = GlobalConfig.getConfiguration().getSuspiciousWords();
        Map<String, Integer> wordsOccurrencesCounter = new HashMap<>();
        suspiciousWords.forEach(word -> {
            int counter = 0;
            Matcher m = Pattern.compile(word).matcher(bodyContent);
            while (m.find()) {
                counter++;
            }
            if (counter > 0) {
                wordsOccurrencesCounter.put(word, counter);
            }
        });
        for (Map.Entry<String, Integer> e : wordsOccurrencesCounter.entrySet()) {
            suspiciousInBody += e.getValue();
            suspiciousElements.add("Word '" + e.getKey() + "' " + e.getValue() + " times in body");
        }
        if (suspiciousInBody > 5) {
            setEmailType(EmailType.SPAM);
        } else if (suspiciousInBody > 0) {
            setEmailType(EmailType.SUSPICIOUS);
        }
        List<String> hyperlinksList = Utils.getAllPatternOccurencesFromText(bodyContent, Utils.URL_PATTERN);
        if (hyperlinksList.size() > 0) {
            hyperlinksList.forEach(url -> suspiciousElements.add(url + " url in email body"));
            if (contentType.contains("html") && (suspiciousInBody > 0 || suspiciousInTitle > 0)) {
                setEmailType(EmailType.SPAM);
            } else {
                setEmailType(EmailType.SUSPICIOUS);
            }
        }
        if (contentType.contains("html")) {
            List<String> imgHyperlinks = Utils.getAllPatternOccurencesFromText(bodyContent, Utils.HTML_IMG_URL);
            if (imgHyperlinks.size() > 0) {
                suspiciousElements.add("Email contains " + imgHyperlinks.size() + " images with hyperlinks");
                if (suspiciousInBody > 0 || suspiciousInTitle > 0) {
                    setEmailType(EmailType.SPAM);
                } else {
                    setEmailType(EmailType.SUSPICIOUS);
                }
            }
        }
    }

    private void senderAddressContainsNoReplay() {
        String senderAddress = model.getEmailHeader().getSenderAddress();
        if (senderAddress.toLowerCase().contains("noreplay")) {
            setEmailType(EmailType.SUSPICIOUS);
        }
    }

    private void containsListUnsubscribe() {
        boolean hasHeader = model.getEmailHeader().containsListUnsubscribe();
        if (hasHeader) {
            setEmailType(EmailType.SPAM);
            suspiciousElements.add("Contains header: List-Unsubscribe");
        }
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

    void addSuspiciousElement(String val) {
        suspiciousElements.add(val);
    }

    void setEmailType(EmailType type) {
        if (this.type == null ||
                this.type == OK ||
                (this.type == SUSPICIOUS && type == SPAM)) {
            this.type = type;
        }
    }
}
