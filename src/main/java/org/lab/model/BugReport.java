package org.lab.model;

import lombok.With;
import java.util.UUID;

public record BugReport(
    UUID id,
    UUID projectId,
    String description,
    @With BugReportStatus status
) {
}

