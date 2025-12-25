package org.lab.service;

import org.lab.auth.AuthService;
import org.lab.auth.model.AccessBinding;
import org.lab.auth.model.Permission;
import org.lab.auth.model.Role;
import org.lab.exception.ProjectNotFoundException;
import org.lab.model.Project;
import org.lab.repository.ProjectRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * У человека может быть только одна роль в проекте.
 * У проекта может быть только один teamLead.
 * У проекта может быть только один manager, определяется создателем.
 */
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final AuthService authService;

    public ProjectService(ProjectRepository projectRepository, AuthService authService) {
        this.projectRepository = projectRepository;
        this.authService = authService;
    }

    public List<Project> list(UUID userId) {
        Set<UUID> userProjectIds = authService.findAllByUserId(userId).stream()
                .map(AccessBinding::projectId)
                .collect(Collectors.toSet());
        
        return projectRepository.findAll().stream()
                .filter(project -> userProjectIds.contains(project.id()))
                .toList();
    }

    public Project create(UUID userId, String title, String description) {
        Project project = new Project(
            UUID.randomUUID(),
            title,
            description,
            new ArrayList<>(),
            new ArrayList<>()
        );
        Project savedProject = projectRepository.save(project);
        authService.addBinding(userId, savedProject.id(), Role.MANAGER);
        return savedProject;
    }

    public void setTeamLead(UUID projectId, UUID teamLeadId) {
        projectRepository.findById(projectId)
            .orElseThrow(() -> new ProjectNotFoundException(projectId));
        
        authService.checkPermission(projectId, Permission.PROJECT_SET_TEAM_LEAD);
        
        authService.removeAllByProjectIdAndRole(projectId, Role.TEAM_LEAD);
        authService.addBinding(teamLeadId, projectId, Role.TEAM_LEAD);
    }

    public void addDeveloper(UUID projectId, UUID developerId) {
        projectRepository.findById(projectId)
            .orElseThrow(() -> new ProjectNotFoundException(projectId));
        
        authService.checkPermission(projectId, Permission.PROJECT_ADD_DEVELOPER);
        
        authService.addBinding(developerId, projectId, Role.DEVELOPER);
    }

    public void addTester(UUID projectId, UUID testerId) {
        projectRepository.findById(projectId)
            .orElseThrow(() -> new ProjectNotFoundException(projectId));
        
        authService.checkPermission(projectId, Permission.PROJECT_ADD_TESTER);
        
        authService.addBinding(testerId, projectId, Role.TESTER);
    }

    public void test(UUID projectId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new ProjectNotFoundException(projectId));
        
        authService.checkPermission(projectId, Permission.PROJECT_TEST);
        
        IO.println("Testing project " + project.title() + "#" + project.id() + "...");
    }
}

