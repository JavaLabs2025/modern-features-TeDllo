package org.lab.auth;

import org.lab.auth.model.Permission;

import java.util.UUID;

public class PermissionDeniedException extends RuntimeException {
    public PermissionDeniedException(UUID userId, UUID projectId, Permission permission) {
        super("Permission denied: userId=" + userId + ", projectId=" + projectId + ", permission=" + permission.getName());
    }
}

