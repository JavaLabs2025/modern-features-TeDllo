package org.lab.exception;

import java.util.UUID;
import java.util.function.Supplier;

public class BugReportNotFoundException extends RuntimeException {
    public BugReportNotFoundException(UUID bugReportId) {
        super("Bug report not found: " + bugReportId);
    }
    
    public static Supplier<BugReportNotFoundException> supplier(UUID bugReportId) {
        return () -> new BugReportNotFoundException(bugReportId);
    }
}

