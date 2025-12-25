package org.lab.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lab.TestBase;
import org.lab.auth.PermissionDeniedException;
import org.lab.auth.model.Permission;
import org.lab.auth.model.Role;
import org.lab.model.Project;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest extends TestBase {
    
    private UUID projectId;
    
    @BeforeEach
    void setUp() {
        setCurrentUser(managerId);
        Project project = projectService.create(managerId, "Project", "Desc");
        projectId = project.id();
    }
    
    @Test
    void testAddBinding() {
        authService.addBinding(developerId, projectId, Role.DEVELOPER);
        
        var binding = authRepository.findByUserIdAndProjectId(developerId, projectId);
        assertTrue(binding.isPresent());
        assertEquals(Role.DEVELOPER, binding.get().role());
    }
    
    @Test
    void testRemoveBinding() {
        authService.addBinding(developerId, projectId, Role.DEVELOPER);
        authService.removeBinding(developerId, projectId, Role.DEVELOPER);
        
        var binding = authRepository.findByUserIdAndProjectId(developerId, projectId);
        assertFalse(binding.isPresent());
    }
    
    @Test
    void testRemoveBindingOnlyRemovesCorrectRole() {
        authService.addBinding(developerId, projectId, Role.DEVELOPER);
        authService.removeBinding(developerId, projectId, Role.TESTER);
        
        var binding = authRepository.findByUserIdAndProjectId(developerId, projectId);
        assertTrue(binding.isPresent());
    }
    
    @Test
    void testCheckPermissionSuccess() {
        setCurrentUser(managerId);
        authService.addBinding(managerId, projectId, Role.MANAGER);
        
        assertDoesNotThrow(() -> 
            authService.checkPermission(projectId, Permission.PROJECT_SET_TEAM_LEAD));
    }
    
    @Test
    void testCheckPermissionDenied() {
        setCurrentUser(developerId);
        authService.addBinding(developerId, projectId, Role.DEVELOPER);
        
        assertThrows(PermissionDeniedException.class, 
            () -> authService.checkPermission(projectId, Permission.PROJECT_SET_TEAM_LEAD));
    }
    
    @Test
    void testFindAllByUserId() {
        UUID projectId2 = projectService.create(managerId, "Project 2", "Desc").id();
        
        authService.addBinding(developerId, projectId, Role.DEVELOPER);
        authService.addBinding(developerId, projectId2, Role.DEVELOPER);
        
        var bindings = authService.findAllByUserId(developerId);
        assertEquals(2, bindings.size());
    }
}

