package org.lab.repository;

import org.lab.model.Milestone;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MilestoneRepository {
    Milestone save(Milestone milestone);
    
    Optional<Milestone> findById(UUID id);
    
    List<Milestone> findAll();
    
    void deleteById(UUID id);
}

