package org.lab.model;

import java.util.List;
import java.util.UUID;

// Can be value object
public record Project(
    UUID id,
    String title,
    String description,
    List<UUID> milestoneIds,
    List<UUID> bugReportIds
) implements Entity {
}
