package org.lab.model;

import lombok.With;
import java.util.List;
import java.util.UUID;

public record Ticket(
    UUID id,
    UUID projectId,
    UUID milestoneId,
    String description,
    @With List<UUID> assignedDevelopers,
    @With TicketStatus status
) implements Entity {
    public boolean isCompleted() {
        return status == TicketStatus.COMPLETED;
    }
}

