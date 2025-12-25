package org.lab.service;

import org.lab.exception.ProjectNotFoundException;
import org.lab.model.Project;
import org.lab.repository.ProjectRepository;

import java.io.IO;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> list(UUID userId) {
        return projectRepository.findAll().stream()
            .filter(project -> project.manager().equals(userId) ||
                project.teamLead() != null && project.teamLead().equals(userId) ||
                project.developers().contains(userId) ||
                project.testers().contains(userId))
            .toList();
    }

    public Project create(UUID userId, String title, String description) {
        Project project = new Project(
            UUID.randomUUID(),
            title,
            description,
            new ArrayList<>(),
            new ArrayList<>(),
            userId,
            null,
            new ArrayList<>(),
            new ArrayList<>()
        );
        return projectRepository.save(project);
    }

    public void setTeamLead(UUID projectId, UUID teamLeadId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new ProjectNotFoundException(projectId));

        projectRepository.save(project.withTeamLead(teamLeadId));
    }

    public void addDeveloper(UUID projectId, UUID developerId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new ProjectNotFoundException(projectId));

        List<UUID> developers = new ArrayList<>(project.developers());
        if (!developers.contains(developerId)) {
            developers.add(developerId);
        }

        projectRepository.save(project.withDevelopers(developers));
    }

    public void addTester(UUID projectId, UUID testerId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new ProjectNotFoundException(projectId));

        List<UUID> testers = new ArrayList<>(project.testers());
        if (!testers.contains(testerId)) {
            testers.add(testerId);
        }
        projectRepository.save(project.withTesters(testers));
    }

    public void test(UUID projectId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new ProjectNotFoundException(projectId));
        IO.println("Testing project " + project.title() + "#" + project.id() + "...");
    }
}

