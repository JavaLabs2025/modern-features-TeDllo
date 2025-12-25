package org.lab.repository.inmemory;

import org.lab.model.BugReport;
import org.lab.repository.BugReportRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryBugReportRepository implements BugReportRepository {
    private final Map<UUID, BugReport> storage = new ConcurrentHashMap<>();
    
    @Override
    public BugReport save(BugReport bugReport) {
        storage.put(bugReport.id(), bugReport);
        return bugReport;
    }
    
    @Override
    public Optional<BugReport> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }
    
    @Override
    public List<BugReport> findAll() {
        return new ArrayList<>(storage.values());
    }
    
    @Override
    public void deleteById(UUID id) {
        storage.remove(id);
    }
}

