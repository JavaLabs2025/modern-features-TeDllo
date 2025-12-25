package org.lab.exception;

import java.util.UUID;

public class MilestoneNotFoundException extends RuntimeException {
    public MilestoneNotFoundException(UUID milestoneId) {
        super("Milestone not found: " + milestoneId);
    }
}

