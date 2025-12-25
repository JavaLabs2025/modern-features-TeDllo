package org.lab.exception;

import java.util.UUID;

public class NotAllTicketsCompletedException extends RuntimeException {
    public NotAllTicketsCompletedException(UUID milestoneId) {
        super("Cannot close milestone: not all tickets are completed: " + milestoneId);
    }
}

