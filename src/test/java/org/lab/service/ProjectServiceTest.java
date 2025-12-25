package org.lab.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lab.TestBase;
import org.lab.auth.model.Role;
import org.lab.exception.ProjectNotFoundException;
import org.lab.model.Project;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProjectServiceTest extends TestBase {

    private UUID projectId;

    @BeforeEach
    void setUp() {
        setCurrentUser(managerId);
        var project = projectService.create(managerId, "Test Project", "Description");
        projectId = project.id();
    }

    @Test
    void testCreateProject() {
        setCurrentUser(managerId);
        var project = projectService.create(managerId, "New Project", "New Description");

        assertNotNull(project);
        assertNotNull(project.id());
        assertEquals("New Project", project.title());
        assertEquals("New Description", project.description());
    }

    @Test
    void testCreateProjectSetsManagerRole() {
        setCurrentUser(managerId);
        var project = projectService.create(managerId, "Project", "Desc");

        var binding = authRepository.findByUserIdAndProjectId(managerId, project.id());
        assertTrue(binding.isPresent());
        assertEquals(Role.MANAGER, binding.get().role());
    }

    @Test
    void testListProjectsForUser() {
        var testUserId = userService.register("Test User").id();

        setCurrentUser(testUserId);
        var managerProject = projectService.create(testUserId, "Manager Project", "Desc");

        setCurrentUser(managerId);
        var teamLeadProject = projectService.create(managerId, "TeamLead Project", "Desc");
        projectService.setTeamLead(teamLeadProject.id(), testUserId);

        var developerProject = projectService.create(managerId, "Developer Project", "Desc");
        projectService.addDeveloper(developerProject.id(), testUserId);

        var testerProject = projectService.create(managerId, "Tester Project", "Desc");
        projectService.addTester(testerProject.id(), testUserId);

        var otherUserId = userService.register("Other User").id();
        setCurrentUser(otherUserId);
        var otherProject = projectService.create(otherUserId, "Other Project 1", "Desc");

        var userProjects = projectService.list(testUserId);

        assertEquals(4, userProjects.size());
        assertTrue(userProjects.stream().anyMatch(p -> p.id().equals(managerProject.id())));
        assertTrue(userProjects.stream().anyMatch(p -> p.id().equals(teamLeadProject.id())));
        assertTrue(userProjects.stream().anyMatch(p -> p.id().equals(developerProject.id())));
        assertTrue(userProjects.stream().anyMatch(p -> p.id().equals(testerProject.id())));

        assertFalse(userProjects.stream().anyMatch(p -> p.id().equals(otherProject.id())));
    }

    @Test
    void testSetTeamLead() {
        setCurrentUser(managerId);
        projectService.setTeamLead(projectId, teamLeadId);

        var binding = authRepository.findByUserIdAndProjectId(teamLeadId, projectId);
        assertTrue(binding.isPresent());
        assertEquals(Role.TEAM_LEAD, binding.get().role());
    }

    @Test
    void testSetTeamLeadRemovesPreviousTeamLead() {
        setCurrentUser(managerId);
        var previousTeamLead = userService.register("Previous TeamLead").id();
        projectService.setTeamLead(projectId, previousTeamLead);

        projectService.setTeamLead(projectId, teamLeadId);

        var previousBinding = authRepository.findByUserIdAndProjectId(previousTeamLead, projectId);
        var newBinding = authRepository.findByUserIdAndProjectId(teamLeadId, projectId);

        assertFalse(previousBinding.isPresent());
        assertTrue(newBinding.isPresent());
        assertEquals(Role.TEAM_LEAD, newBinding.get().role());
    }

    @Test
    void testAddDeveloper() {
        setCurrentUser(managerId);
        projectService.addDeveloper(projectId, developerId);

        var binding = authRepository.findByUserIdAndProjectId(developerId, projectId);
        assertTrue(binding.isPresent());
        assertEquals(Role.DEVELOPER, binding.get().role());
    }

    @Test
    void testAddTester() {
        setCurrentUser(managerId);
        projectService.addTester(projectId, testerId);

        var binding = authRepository.findByUserIdAndProjectId(testerId, projectId);
        assertTrue(binding.isPresent());
        assertEquals(Role.TESTER, binding.get().role());
    }

    @Test
    void testTestProject() {
        setCurrentUser(managerId);
        projectService.addTester(projectId, testerId);
        setCurrentUser(testerId);

        assertDoesNotThrow(() -> projectService.test(projectId));
    }

    @Test
    void testProjectNotFound() {
        setCurrentUser(managerId);
        var nonExistentId = UUID.randomUUID();

        assertThrows(ProjectNotFoundException.class,
            () -> projectService.setTeamLead(nonExistentId, teamLeadId));
    }
}

