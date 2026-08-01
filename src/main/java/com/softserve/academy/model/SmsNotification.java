package com.softserve.academy.model;

import com.softserve.academy.exception.NotDeliverableException;
import lombok.Getter;

@Getter
public class SmsNotification extends Notification {
    private final String phoneNumber;
    private final boolean isFlash;

    public SmsNotification(String recipient, String message, int priority, String phoneNumber, boolean isFlash) {
        super(recipient, message, priority);
        this.phoneNumber = phoneNumber;
        this.isFlash = isFlash;
    }

    @Override
    public boolean isDeliverable() {
        if (getPhoneNumber() == null) {
            return false;
        } else {
            return getPhoneNumber().matches("^\\+\\d{9,14}$");
        }
    }

    public boolean isOverLimit() {
        return getMessage().length() > 160;
    }

    @Override
    public String getFormattedMessage() {
        if (isOverLimit()) {
            setMessage(getMessage().substring(0, 160));
        }
        return getMessage();
    }

    @Override
    public int estimateDeliverySeconds() {
        return 5;
    }

    @Override
    protected void performSend() throws NotDeliverableException {
        if (isDeliverable()) {
            System.out.println("To: " + getRecipient());
            System.out.println("Phone number: " + getPhoneNumber());
            System.out.println(getFormattedMessage());
            System.out.println("Has flash? " + isFlash());
            System.out.println("Priority: " + getPriority() + " " + (isHighPriority() ? "is high." : "is low"));
            System.out.printf("Message will be sent in %d seconds.", estimateDeliverySeconds());
            System.out.println("Status: " + getStatus() + "\n");
        }
    }
}
