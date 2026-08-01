package com.softserve.academy.model;

import com.softserve.academy.exception.InvalidNotificationException;
import com.softserve.academy.exception.NotDeliverableException;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Notification implements Comparable<Notification> {
    protected String recipient;
    protected String message;
    protected int priority;
    protected NotificationStatus status;

    public Notification(String recipient, String message, int priority) {
        if (recipient == null || recipient.isBlank()) {
            throw new InvalidNotificationException("Recipient cannot be empty!");
        }
        if (message == null) {
            throw new InvalidNotificationException("Message cannot be null!");
        }
        if (priority < 1 || priority > 5) {
            throw new InvalidNotificationException("Priority cannot be less then 1 or more then 5!");
        }
        this.recipient = recipient;
        this.message = message;
        this.priority = priority;
        this.status = NotificationStatus.PENDING;
    }

    public abstract boolean isDeliverable() throws NotDeliverableException;

    public abstract String getFormattedMessage();

    public abstract int estimateDeliverySeconds();

    public boolean isHighPriority() {
        return getPriority() >= 4;
    }

    public void send() throws NotDeliverableException {
        boolean result = isDeliverable();
        if (!result) {
            setStatus(NotificationStatus.FAILED);
            throw new NotDeliverableException("Message should be deliverable!");
        }
        performSend();
        setStatus(NotificationStatus.SENT);
    }

    protected abstract void performSend() throws NotDeliverableException;

    @Override
    public int compareTo(Notification other) {
        // TODO: Сортування за priority descending
        return Integer.compare(other.priority, this.priority);
    }
}