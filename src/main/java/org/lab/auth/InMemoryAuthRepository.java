package org.lab.auth;

import org.lab.auth.model.AccessBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryAuthRepository implements AuthRepository {
    private final Map<String, AccessBinding> storage = new ConcurrentHashMap<>();
    
    private String key(UUID userId, UUID projectId) {
        return userId + ":" + projectId;
    }
    
    @Override
    public AccessBinding save(AccessBinding accessBinding) {
        storage.put(key(accessBinding.userId(), accessBinding.projectId()), accessBinding);
        return accessBinding;
    }
    
    @Override
    public Optional<AccessBinding> findByUserIdAndProjectId(UUID userId, UUID projectId) {
        return Optional.ofNullable(storage.get(key(userId, projectId)));
    }
    
    @Override
    public List<AccessBinding> findAll() {
        return new ArrayList<>(storage.values());
    }
    
    @Override
    public void deleteByUserIdAndProjectId(UUID userId, UUID projectId) {
        storage.remove(key(userId, projectId));
    }
}

