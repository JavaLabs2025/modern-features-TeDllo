package org.lab.model;

import lombok.With;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public record Project(
    UUID id,
    String title,
    String description,
    @With List<UUID> developers,
    @With List<UUID> testers,
    UUID manager,
    @With @Nullable UUID teamLead,
    List<UUID> milestoneIds,
    List<UUID> bugReportIds
) {
}

// AuthService (checkPermission(user, permission, projectId))
// Enum Role ROLE1(name, list<String> permissions), ...
// Enum Permission PERM1(name, description), ...
// AccessBinding (userId, projectId, role)
// AccessBindingService (create(userId, projectId, role), delete(userId, projectId, role))

