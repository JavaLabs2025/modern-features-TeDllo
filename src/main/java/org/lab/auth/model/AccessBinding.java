package org.lab.auth.model;

import java.util.UUID;

public record AccessBinding(
    UUID userId,
    UUID projectId,
    Role role
) {
}

