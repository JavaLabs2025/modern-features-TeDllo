package org.lab.auth;

import org.lab.auth.model.AccessBinding;
import org.lab.auth.model.Permission;
import org.lab.auth.model.Role;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public interface AuthService {
    void checkPermission(UUID projectId, Permission permission);
    
    void addBinding(UUID userId, UUID projectId, Role role);
    
    void removeBinding(UUID userId, UUID projectId, Role role);
    
    List<AccessBinding> findAllByUserId(UUID userId);
    
    void removeAllByProjectIdAndRole(UUID projectId, Role role);
}
