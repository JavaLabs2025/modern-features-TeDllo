package org.lab.exception;

import java.util.UUID;
import java.util.function.Supplier;

public class MilestoneNotFoundException extends RuntimeException {
    public MilestoneNotFoundException(UUID milestoneId) {
        super("Milestone not found: " + milestoneId);
    }
    
    public static Supplier<MilestoneNotFoundException> supplier(UUID milestoneId) {
        return () -> new MilestoneNotFoundException(milestoneId);
    }
}

