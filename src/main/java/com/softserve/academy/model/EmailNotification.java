package com.softserve.academy.model;

import com.softserve.academy.exception.NotDeliverableException;

import java.util.List;
import java.util.regex.Pattern;

import lombok.Getter;

@Getter
public class EmailNotification extends Notification {
    private final String senderEmail;
    private final String subject;
    private final boolean hasAttachment;

    public EmailNotification(String recipient, String message, int priority, String senderEmail,
                             String subject, boolean hasAttachment) {
        super(recipient, message, priority);
        this.senderEmail = senderEmail;
        this.subject = subject;
        this.hasAttachment = hasAttachment;
    }

    @Override
    public boolean isDeliverable() throws NotDeliverableException {
        if (getRecipient() == null) {
            throw new NotDeliverableException("Email is not deliverable!");
        }
        String regex = "^[^@]+@[^@]+\\.[^@]+$";
        return Pattern.matches(regex, getRecipient());
    }

    public boolean isSpam() {
        String lowerCaseSubject = getSubject().toLowerCase();
        List<String> spamWords = List.of("free", "win", "click");

        return spamWords.stream().anyMatch(lowerCaseSubject::contains);
    }

    @Override
    public String getFormattedMessage() {
        return String.format("Subject: %s\n%s", getSubject(), getMessage());
    }

    @Override
    public int estimateDeliverySeconds() {
        return 30;
    }

    @Override
    protected void performSend() throws NotDeliverableException {
        if (isSpam()) {
            throw new NotDeliverableException("This massage is a spam!");
        }
        if (isDeliverable()) {
            System.out.println("Send from: " + getSenderEmail());
            System.out.println("To: " + getRecipient());
            System.out.println("Priority: " + getPriority() + " " + (isHighPriority() ? "is high." : "is low"));
            System.out.println(getFormattedMessage());
            String attachment = hasAttachment ? "Yes" : "No";
            System.out.println("Is there any attachment? " + attachment);
            System.out.printf("Message will be delivered in %d seconds.", estimateDeliverySeconds());
            System.out.println("Status: " + getStatus() + "\n");
        }
    }
}