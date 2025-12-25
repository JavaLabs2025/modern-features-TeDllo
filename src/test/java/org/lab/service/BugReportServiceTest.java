package org.lab.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lab.TestBase;
import org.lab.auth.model.Role;
import org.lab.exception.BugReportNotFoundException;
import org.lab.model.BugReport;
import org.lab.model.BugReportStatus;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BugReportServiceTest extends TestBase {

    private UUID projectId;

    @BeforeEach
    void setUp() {
        setCurrentUser(managerId);
        var project = projectService.create(managerId, "Project", "Desc");
        projectId = project.id();
    }

    @Test
    void testCreateBugReport() {
        setCurrentUser(managerId);
        projectService.addDeveloper(projectId, developerId);
        setCurrentUser(developerId);

        var bugReport = bugReportService.create(projectId, "Bug description");

        assertNotNull(bugReport);
        assertNotNull(bugReport.id());
        assertEquals(projectId, bugReport.projectId());
        assertEquals("Bug description", bugReport.description());
        assertEquals(BugReportStatus.NEW, bugReport.status());
    }

    @Test
    void testFixBugReport() {
        setCurrentUser(managerId);
        projectService.addDeveloper(projectId, developerId);
        setCurrentUser(developerId);

        var bugReport = bugReportService.create(projectId, "Bug");
        bugReportService.fix(bugReport.id());

        var fixed = bugReportRepository.findById(bugReport.id()).orElseThrow();
        assertEquals(BugReportStatus.FIXED, fixed.status());
    }

    @Test
    void testTestBugReport() {
        setCurrentUser(managerId);
        projectService.addTester(projectId, testerId);
        projectService.addDeveloper(projectId, developerId);
        setCurrentUser(developerId);
        var bugReport = bugReportService.create(projectId, "Bug");
        bugReportService.fix(bugReport.id());
        setCurrentUser(testerId);
        bugReportService.test(bugReport.id());

        var tested = bugReportRepository.findById(bugReport.id()).orElseThrow();
        assertEquals(BugReportStatus.TESTED, tested.status());
    }

    @Test
    void testCloseBugReport() {
        setCurrentUser(managerId);
        projectService.addTester(projectId, testerId);
        projectService.addDeveloper(projectId, developerId);
        setCurrentUser(developerId);
        var bugReport = bugReportService.create(projectId, "Bug");
        bugReportService.fix(bugReport.id());
        setCurrentUser(testerId);
        bugReportService.test(bugReport.id());
        bugReportService.close(bugReport.id());

        var closed = bugReportRepository.findById(bugReport.id()).orElseThrow();
        assertEquals(BugReportStatus.CLOSED, closed.status());
    }

    @Test
    void testListByUser() {
        setCurrentUser(managerId);
        projectService.addDeveloper(projectId, developerId);
        setCurrentUser(developerId);

        var bugReport1 = bugReportService.create(projectId, "Bug 1");
        var bugReport2 = bugReportService.create(projectId, "Bug 2");

        var userBugs = bugReportService.listByUser(developerId);
        assertTrue(userBugs.size() >= 2);
        assertTrue(userBugs.stream().anyMatch(b -> b.id().equals(bugReport1.id())));
        assertTrue(userBugs.stream().anyMatch(b -> b.id().equals(bugReport2.id())));
    }

    @Test
    void testBugReportNotFound() {
        setCurrentUser(managerId);
        projectService.addDeveloper(projectId, developerId);
        setCurrentUser(developerId);
        var nonExistentId = UUID.randomUUID();

        assertThrows(BugReportNotFoundException.class,
            () -> bugReportService.fix(nonExistentId));
    }
}

