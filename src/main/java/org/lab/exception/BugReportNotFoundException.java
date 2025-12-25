package org.lab.exception;

import java.util.UUID;

public class BugReportNotFoundException extends RuntimeException {
    public BugReportNotFoundException(UUID bugReportId) {
        super("Bug report not found: " + bugReportId);
    }
}

