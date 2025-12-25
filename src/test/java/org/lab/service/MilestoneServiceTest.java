package org.lab.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lab.TestBase;
import org.lab.auth.model.Role;
import org.lab.exception.ActiveMilestoneExistsException;
import org.lab.exception.MilestoneNotFoundException;
import org.lab.exception.NotAllTicketsCompletedException;
import org.lab.model.Milestone;
import org.lab.model.MilestoneStatus;
import org.lab.model.Ticket;
import org.lab.model.TicketStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MilestoneServiceTest extends TestBase {
    
    private UUID projectId;
    private UUID milestoneId;
    
    @BeforeEach
    void setUp() {
        setCurrentUser(managerId);
        var project = projectService.create(managerId, "Project", "Desc");
        projectId = project.id();
    }
    
    @Test
    void testCreateMilestone() {
        setCurrentUser(managerId);
        Milestone milestone = milestoneService.create(
            projectId, 
            LocalDate.now(), 
            LocalDate.now().plusDays(30)
        );
        
        assertNotNull(milestone);
        assertNotNull(milestone.id());
        assertEquals(projectId, milestone.projectId());
        assertEquals(MilestoneStatus.OPENED, milestone.status());
    }
    
    @Test
    void testSetStatusToActive() {
        setCurrentUser(managerId);
        Milestone milestone = milestoneService.create(
            projectId, 
            LocalDate.now(), 
            LocalDate.now().plusDays(30)
        );
        
        milestoneService.setStatus(milestone.id(), MilestoneStatus.ACTIVE);
        
        Milestone updated = milestoneRepository.findById(milestone.id()).orElseThrow();
        assertEquals(MilestoneStatus.ACTIVE, updated.status());
    }
    
    @Test
    void testOnlyOneActiveMilestonePerProject() {
        setCurrentUser(managerId);
        Milestone milestone1 = milestoneService.create(
            projectId, 
            LocalDate.now(), 
            LocalDate.now().plusDays(30)
        );
        milestoneService.setStatus(milestone1.id(), MilestoneStatus.ACTIVE);
        
        Milestone milestone2 = milestoneService.create(
            projectId, 
            LocalDate.now().plusDays(31), 
            LocalDate.now().plusDays(60)
        );
        
        assertThrows(ActiveMilestoneExistsException.class, 
            () -> milestoneService.setStatus(milestone2.id(), MilestoneStatus.ACTIVE));
    }
    
    @Test
    void testCannotCloseMilestoneWithIncompleteTickets() {
        setCurrentUser(managerId);
        Milestone milestone = milestoneService.create(
            projectId, 
            LocalDate.now(), 
            LocalDate.now().plusDays(30)
        );
        
        Ticket ticket = ticketService.create(projectId, milestone.id(), "Test ticket");
        var updatedMilestone = milestoneRepository.findById(milestone.id()).orElseThrow();
        var milestoneWithTicket = updatedMilestone.withTicketIds(
            List.of(ticket.id())
        );
        milestoneRepository.save(milestoneWithTicket);
        
        assertThrows(NotAllTicketsCompletedException.class, 
            () -> milestoneService.setStatus(milestone.id(), MilestoneStatus.CLOSED));
    }
    
    @Test
    void testCanCloseMilestoneWhenAllTicketsCompleted() {
        setCurrentUser(managerId);
        projectService.addDeveloper(projectId, developerId);
        Milestone milestone = milestoneService.create(
            projectId, 
            LocalDate.now(), 
            LocalDate.now().plusDays(30)
        );
        
        Ticket ticket = ticketService.create(projectId, milestone.id(), "Test ticket");
        ticketService.assignDeveloper(ticket.id(), developerId);
        setCurrentUser(developerId);
        ticketService.complete(ticket.id());
        
        var updatedMilestone = milestoneRepository.findById(milestone.id()).orElseThrow();
        var milestoneWithTicket = updatedMilestone.withTicketIds(
            List.of(ticket.id())
        );
        milestoneRepository.save(milestoneWithTicket);
        
        setCurrentUser(managerId);
        assertDoesNotThrow(() -> 
            milestoneService.setStatus(milestone.id(), MilestoneStatus.CLOSED));
        
        Milestone closed = milestoneRepository.findById(milestone.id()).orElseThrow();
        assertEquals(MilestoneStatus.CLOSED, closed.status());
    }
    
    @Test
    void testMilestoneNotFound() {
        setCurrentUser(managerId);
        UUID nonExistentId = UUID.randomUUID();
        
        assertThrows(MilestoneNotFoundException.class, 
            () -> milestoneService.setStatus(nonExistentId, MilestoneStatus.ACTIVE));
    }
}

