package org.lab.service;

import org.lab.auth.AuthService;
import org.lab.auth.model.AccessBinding;
import org.lab.auth.model.Permission;
import org.lab.exception.BugReportNotFoundException;
import org.lab.model.BugReport;
import org.lab.model.BugReportStatus;
import org.lab.repository.BugReportRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class BugReportService {
    private final BugReportRepository bugReportRepository;
    private final AuthService authService;

    public BugReportService(BugReportRepository bugReportRepository, AuthService authService) {
        this.bugReportRepository = bugReportRepository;
        this.authService = authService;
    }

    public BugReport create(UUID projectId, String description) {
        authService.checkPermission(projectId, Permission.BUG_REPORT_CREATE);
        
        BugReport bugReport = new BugReport(
            UUID.randomUUID(),
            projectId,
            description,
            BugReportStatus.NEW
        );
        return bugReportRepository.save(bugReport);
    }

    public List<BugReport> listByUser(UUID userId) {
        Set<UUID> userProjectIds = authService.findAllByUserId(userId).stream()
                .map(AccessBinding::projectId)
                .collect(Collectors.toSet());
        
        return bugReportRepository.findAll().stream()
                .filter(bugReport -> userProjectIds.contains(bugReport.projectId()))
                .toList();
    }

    public void fix(UUID bugReportId) {
        BugReport bugReport = bugReportRepository.findById(bugReportId)
            .orElseThrow(() -> new BugReportNotFoundException(bugReportId));
        
        authService.checkPermission(bugReport.projectId(), Permission.BUG_REPORT_FIX);
        
        bugReportRepository.save(bugReport.withStatus(BugReportStatus.FIXED));
    }

    public void test(UUID bugReportId) {
        BugReport bugReport = bugReportRepository.findById(bugReportId)
            .orElseThrow(() -> new BugReportNotFoundException(bugReportId));
        
        authService.checkPermission(bugReport.projectId(), Permission.BUG_REPORT_TEST);
        
        bugReportRepository.save(bugReport.withStatus(BugReportStatus.TESTED));
    }

    public void close(UUID bugReportId) {
        BugReport bugReport = bugReportRepository.findById(bugReportId)
            .orElseThrow(() -> new BugReportNotFoundException(bugReportId));
        
        authService.checkPermission(bugReport.projectId(), Permission.BUG_REPORT_CLOSE);
        
        bugReportRepository.save(bugReport.withStatus(BugReportStatus.CLOSED));
    }
}

