package org.lab.auth;

import org.lab.auth.model.AccessBinding;
import org.lab.auth.model.Permission;
import org.lab.auth.model.Role;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class AuthServiceImpl implements AuthService {
    private final AuthRepository authRepository;
    
    public AuthServiceImpl(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public void checkPermission(UUID projectId, Permission permission) {
        var userId = AuthenticationContext.get();
        if (!hasPermission(userId, projectId, permission)) {
            throw new PermissionDeniedException(userId, projectId, permission);
        }
    }

    private boolean hasPermission(UUID userId, UUID projectId, Permission permission) {
        var binding = authRepository.findByUserIdAndProjectId(userId, projectId)
            .orElse(null);

        return binding != null && binding.role().getPermissions().contains(permission.getName());
    }
    
    @Override
    public void addBinding(UUID userId, UUID projectId, Role role) {
        authRepository.save(new AccessBinding(userId, projectId, role));
    }
    
    @Override
    public void removeBinding(UUID userId, UUID projectId, Role role) {
        var binding = authRepository.findByUserIdAndProjectId(userId, projectId)
                .orElse(null);
        
        if (binding != null && binding.role() == role) {
            authRepository.deleteByUserIdAndProjectId(userId, projectId);
        }
    }
    
    @Override
    public List<AccessBinding> findAllByUserId(UUID userId) {
        return authRepository.findAll().stream()
                .filter(binding -> binding.userId().equals(userId))
                .toList();
    }
    
    @Override
    public void removeAllByProjectIdAndRole(UUID projectId, Role role) {
        authRepository.findAll().stream()
                .filter(binding -> binding.projectId().equals(projectId) && binding.role() == role)
                .forEach(authRepository::delete);
    }
}

