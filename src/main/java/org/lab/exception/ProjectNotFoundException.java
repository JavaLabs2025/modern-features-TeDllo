package org.lab.exception;

import java.util.UUID;
import java.util.function.Supplier;

public class ProjectNotFoundException extends RuntimeException {
    public ProjectNotFoundException(UUID projectId) {
        super("Project not found: " + projectId);
    }
    
    public static Supplier<ProjectNotFoundException> supplier(UUID projectId) {
        return () -> new ProjectNotFoundException(projectId);
    }
}

