package org.lab.model;

import lombok.With;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record Milestone(
    UUID id,
    UUID projectId,
    LocalDate startDate,
    LocalDate endDate,
    List<UUID> ticketIds,
    @With MilestoneStatus status
) {
}

