package org.lab.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lab.TestBase;
import org.lab.auth.PermissionDeniedException;
import org.lab.auth.model.Permission;
import org.lab.auth.model.Role;
import org.lab.model.Project;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RoleBasedAccessTest extends TestBase {
    
    private UUID projectId;
    private UUID milestoneId;
    
    @BeforeEach
    void setUp() {
        setCurrentUser(managerId);
        Project project = projectService.create(managerId, "Project", "Desc");
        projectId = project.id();
        
        var milestone = milestoneService.create(
            projectId, 
            LocalDate.now(), 
            LocalDate.now().plusDays(30)
        );
        milestoneId = milestone.id();
    }
    
    @Test
    void testManagerCanSetTeamLead() {
        setCurrentUser(managerId);
        assertDoesNotThrow(() -> 
            projectService.setTeamLead(projectId, teamLeadId));
    }
    
    @Test
    void testManagerCanAddDeveloper() {
        setCurrentUser(managerId);
        assertDoesNotThrow(() -> 
            projectService.addDeveloper(projectId, developerId));
    }
    
    @Test
    void testManagerCanAddTester() {
        setCurrentUser(managerId);
        assertDoesNotThrow(() -> 
            projectService.addTester(projectId, testerId));
    }
    
    @Test
    void testManagerCanCreateMilestone() {
        setCurrentUser(managerId);
        assertDoesNotThrow(() -> 
            milestoneService.create(projectId, LocalDate.now(), LocalDate.now().plusDays(30)));
    }
    
    @Test
    void testManagerCanCreateTicket() {
        setCurrentUser(managerId);
        assertDoesNotThrow(() -> 
            ticketService.create(projectId, milestoneId, "Ticket"));
    }
    
    @Test
    void testTeamLeadCanCreateTicket() {
        setCurrentUser(managerId);
        projectService.setTeamLead(projectId, teamLeadId);
        setCurrentUser(teamLeadId);
        
        assertDoesNotThrow(() -> 
            ticketService.create(projectId, milestoneId, "Ticket"));
    }
    
    @Test
    void testTeamLeadCanAssignDeveloper() {
        setCurrentUser(managerId);
        projectService.setTeamLead(projectId, teamLeadId);
        projectService.addDeveloper(projectId, developerId);
        
        var ticket = ticketService.create(projectId, milestoneId, "Ticket");
        setCurrentUser(teamLeadId);
        
        assertDoesNotThrow(() -> 
            ticketService.assignDeveloper(ticket.id(), developerId));
    }
    
    @Test
    void testDeveloperCannotSetTeamLead() {
        setCurrentUser(managerId);
        projectService.addDeveloper(projectId, developerId);
        setCurrentUser(developerId);
        
        assertThrows(PermissionDeniedException.class, 
            () -> projectService.setTeamLead(projectId, teamLeadId));
    }
    
    @Test
    void testDeveloperCanCompleteTicket() {
        setCurrentUser(managerId);
        projectService.addDeveloper(projectId, developerId);
        var ticket = ticketService.create(projectId, milestoneId, "Ticket");
        ticketService.assignDeveloper(ticket.id(), developerId);
        
        setCurrentUser(developerId);
        assertDoesNotThrow(() -> ticketService.complete(ticket.id()));
    }
    
    @Test
    void testDeveloperCanCreateBugReport() {
        setCurrentUser(managerId);
        projectService.addDeveloper(projectId, developerId);
        setCurrentUser(developerId);
        
        assertDoesNotThrow(() -> 
            bugReportService.create(projectId, "Bug description"));
    }
    
    @Test
    void testDeveloperCanFixBugReport() {
        setCurrentUser(managerId);
        projectService.addDeveloper(projectId, developerId);
        setCurrentUser(developerId);
        
        var bugReport = bugReportService.create(projectId, "Bug");
        assertDoesNotThrow(() -> bugReportService.fix(bugReport.id()));
    }
    
    @Test
    void testTesterCanTestProject() {
        setCurrentUser(managerId);
        projectService.addTester(projectId, testerId);
        setCurrentUser(testerId);
        
        assertDoesNotThrow(() -> projectService.test(projectId));
    }
    
    @Test
    void testTesterCanCreateBugReport() {
        setCurrentUser(managerId);
        projectService.addTester(projectId, testerId);
        setCurrentUser(testerId);
        
        assertDoesNotThrow(() -> 
            bugReportService.create(projectId, "Bug description"));
    }
    
    @Test
    void testTesterCanTestBugReport() {
        setCurrentUser(managerId);
        projectService.addTester(projectId, testerId);
        projectService.addDeveloper(projectId, developerId);
        setCurrentUser(developerId);
        var bugReport = bugReportService.create(projectId, "Bug");
        bugReportService.fix(bugReport.id());
        setCurrentUser(testerId);
        assertDoesNotThrow(() -> bugReportService.test(bugReport.id()));
    }
}

