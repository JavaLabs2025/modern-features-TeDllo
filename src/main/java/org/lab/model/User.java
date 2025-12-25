package org.lab.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record User(
    UUID id,
    String name,
    LocalDateTime createdAt
) implements Entity {
}

