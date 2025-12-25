package org.lab.repository;

import org.lab.model.BugReport;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BugReportRepository {
    BugReport save(BugReport bugReport);
    
    Optional<BugReport> findById(UUID id);
    
    List<BugReport> findAll();
    
    void deleteById(UUID id);
}

