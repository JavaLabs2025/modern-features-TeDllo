package org.lab.repository.inmemory;

import org.lab.model.Project;
import org.lab.repository.ProjectRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryProjectRepository implements ProjectRepository {
    private final Map<UUID, Project> storage = new ConcurrentHashMap<>();
    
    @Override
    public Project save(Project project) {
        storage.put(project.id(), project);
        return project;
    }
    
    @Override
    public Optional<Project> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }
    
    @Override
    public List<Project> findAll() {
        return new ArrayList<>(storage.values());
    }
    
    @Override
    public void deleteById(UUID id) {
        storage.remove(id);
    }
}

