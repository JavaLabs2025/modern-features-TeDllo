package org.lab.exception;

import java.util.UUID;

public class ActiveMilestoneExistsException extends RuntimeException {
    public ActiveMilestoneExistsException(UUID projectId) {
        super("Project already has an active milestone: " + projectId);
    }
}

