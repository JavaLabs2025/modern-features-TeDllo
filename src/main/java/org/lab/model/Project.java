package org.lab.model;

import java.util.List;
import java.util.UUID;

public record Project(
    UUID id,
    String title,
    String description,
    List<UUID> milestoneIds,
    List<UUID> bugReportIds
) {
}
