package org.lab.repository.inmemory;

import org.lab.model.User;
import org.lab.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepository implements UserRepository {
    private final Map<UUID, User> storage = new ConcurrentHashMap<>();
    
    @Override
    public User save(User user) {
        storage.put(user.id(), user);
        return user;
    }
    
    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }
    
    @Override
    public List<User> findAll() {
        return new ArrayList<>(storage.values());
    }
    
    @Override
    public void deleteById(UUID id) {
        storage.remove(id);
    }
}

