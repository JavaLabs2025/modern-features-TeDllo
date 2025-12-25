package org.lab.auth;

import org.lab.auth.model.AccessBinding;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthRepository {
    AccessBinding save(AccessBinding accessBinding);
    
    Optional<AccessBinding> findByUserIdAndProjectId(UUID userId, UUID projectId);
    
    List<AccessBinding> findAll();
    
    void deleteByUserIdAndProjectId(UUID userId, UUID projectId);
    
    default void delete(AccessBinding binding) {
        deleteByUserIdAndProjectId(binding.userId(), binding.projectId());
    }
}

