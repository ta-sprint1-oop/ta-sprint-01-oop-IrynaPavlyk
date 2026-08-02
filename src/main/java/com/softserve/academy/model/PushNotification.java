package com.softserve.academy.model;

import com.softserve.academy.exception.NotDeliverableException;
import lombok.Getter;

@Getter
public class PushNotification extends Notification {
    private final String deviceToken;
    private final String iconUrl;

    public PushNotification(String recipient, String message, int priority, String deviceToken, String iconUrl) {
        super(recipient, message, priority);
        this.deviceToken = deviceToken;
        this.iconUrl = iconUrl;
    }

    @Override
    public boolean isDeliverable() {
        return !(getDeviceToken() == null) && !getDeviceToken().isBlank() && getDeviceToken().length() > 10;
    }

    public boolean isSilent() {
        return getMessage() != null && getMessage().isBlank();
    }

    @Override
    public String getFormattedMessage() {
        if (isSilent()) {
            return "🔔 (silent)";
        } else {
            return "🔔 " + getMessage();
        }
    }

    @Override
    public int estimateDeliverySeconds() {
        return 1;
    }

    @Override
    protected void performSend() throws NotDeliverableException {
        if (isDeliverable()) {
            System.out.println("To: " + getRecipient());
            System.out.println(getFormattedMessage());
            System.out.println("With icon: " + getIconUrl());
            System.out.println("With token: " + maskSensitiveInfo(getDeviceToken()));
            System.out.println("Priority: " + getPriority() + " " + (isHighPriority() ? "is high." : "is low"));
            System.out.printf("Message will be sent in %d seconds.\n", estimateDeliverySeconds());
            System.out.println("Status: " + getStatus() + "\n");
        } else {
        throw new NotDeliverableException("Push notification is not deliverable!");
    }
    }

    private String maskSensitiveInfo(String token) {
        return token.toLowerCase().contains("token") ? "***" : token;
    }
}