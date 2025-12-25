package org.lab.service;

import org.lab.exception.BugReportNotFoundException;
import org.lab.model.BugReport;
import org.lab.model.BugReportStatus;
import org.lab.repository.BugReportRepository;

import java.util.UUID;

public class BugReportService {
    private final BugReportRepository bugReportRepository;

    public BugReportService(BugReportRepository bugReportRepository) {
        this.bugReportRepository = bugReportRepository;
    }

    public BugReport create(UUID projectId, String description) {
        BugReport bugReport = new BugReport(
            UUID.randomUUID(),
            projectId,
            description,
            BugReportStatus.NEW
        );
        return bugReportRepository.save(bugReport);
    }

    public void fix(UUID bugReportId) {
        updateStatus(bugReportId, BugReportStatus.FIXED);
    }

    public void test(UUID bugReportId) {
        updateStatus(bugReportId, BugReportStatus.TESTED);
    }

    public void close(UUID bugReportId) {
        updateStatus(bugReportId, BugReportStatus.CLOSED);
    }

    private void updateStatus(UUID bugReportId, BugReportStatus status) {
        BugReport bugReport = bugReportRepository.findById(bugReportId)
            .orElseThrow(() -> new BugReportNotFoundException(bugReportId));

        bugReportRepository.save(bugReport.withStatus(status));
    }
}

