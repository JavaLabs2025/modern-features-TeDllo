package org.lab.repository.inmemory;

import org.lab.model.Milestone;
import org.lab.repository.MilestoneRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryMilestoneRepository implements MilestoneRepository {
    private final Map<UUID, Milestone> storage = new ConcurrentHashMap<>();
    
    @Override
    public Milestone save(Milestone milestone) {
        storage.put(milestone.id(), milestone);
        return milestone;
    }
    
    @Override
    public Optional<Milestone> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }
    
    @Override
    public List<Milestone> findAll() {
        return new ArrayList<>(storage.values());
    }
    
    @Override
    public void deleteById(UUID id) {
        storage.remove(id);
    }
}

