package org.lab.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lab.TestBase;
import org.lab.auth.model.Role;
import org.lab.exception.TicketNotFoundException;
import org.lab.model.Milestone;
import org.lab.model.MilestoneStatus;
import org.lab.model.Ticket;
import org.lab.model.TicketStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TicketServiceTest extends TestBase {

    private UUID projectId;
    private UUID milestoneId;

    @BeforeEach
    void setUp() {
        setCurrentUser(managerId);
        var project = projectService.create(managerId, "Project", "Desc");
        projectId = project.id();

        var milestone = milestoneService.create(
            projectId,
            LocalDate.now(),
            LocalDate.now().plusDays(30)
        );
        milestoneId = milestone.id();
    }

    @Test
    void testCreateTicket() {
        setCurrentUser(managerId);
        var ticket = ticketService.create(projectId, milestoneId, "Test ticket");

        assertNotNull(ticket);
        assertNotNull(ticket.id());
        assertEquals(projectId, ticket.projectId());
        assertEquals(milestoneId, ticket.milestoneId());
        assertEquals("Test ticket", ticket.description());
        assertEquals(TicketStatus.NEW, ticket.status());
    }

    @Test
    void testAssignDeveloper() {
        setCurrentUser(managerId);
        projectService.addDeveloper(projectId, developerId);

        var ticket = ticketService.create(projectId, milestoneId, "Ticket");
        ticketService.assignDeveloper(ticket.id(), developerId);

        var updated = ticketRepository.findById(ticket.id()).orElseThrow();
        assertTrue(updated.assignedDevelopers().contains(developerId));
    }

    @Test
    void testAssignDeveloperDoesNotDuplicate() {
        setCurrentUser(managerId);
        projectService.addDeveloper(projectId, developerId);

        var ticket = ticketService.create(projectId, milestoneId, "Ticket");
        ticketService.assignDeveloper(ticket.id(), developerId);
        ticketService.assignDeveloper(ticket.id(), developerId);

        var updated = ticketRepository.findById(ticket.id()).orElseThrow();
        assertEquals(1, updated.assignedDevelopers().size());
    }

    @Test
    void testGetStatus() {
        setCurrentUser(managerId);
        projectService.addDeveloper(projectId, developerId);

        var ticket = ticketService.create(projectId, milestoneId, "Ticket");
        var status = ticketService.getStatus(ticket.id());

        assertEquals(TicketStatus.NEW, status);
    }

    @Test
    void testCompleteTicket() {
        setCurrentUser(managerId);
        projectService.addDeveloper(projectId, developerId);
        var ticket = ticketService.create(projectId, milestoneId, "Ticket");
        ticketService.assignDeveloper(ticket.id(), developerId);
        setCurrentUser(developerId);
        ticketService.complete(ticket.id());

        var completed = ticketRepository.findById(ticket.id()).orElseThrow();
        assertEquals(TicketStatus.COMPLETED, completed.status());
    }

    @Test
    void testListByUser() {
        setCurrentUser(managerId);
        projectService.addDeveloper(projectId, developerId);

        var ticket1 = ticketService.create(projectId, milestoneId, "Ticket 1");
        var ticket2 = ticketService.create(projectId, milestoneId, "Ticket 2");

        ticketService.assignDeveloper(ticket1.id(), developerId);
        ticketService.assignDeveloper(ticket2.id(), developerId);

        var userTickets = ticketService.listByUser(developerId);
        assertEquals(2, userTickets.size());
    }

    @Test
    void testTicketNotFound() {
        setCurrentUser(managerId);
        var nonExistentId = UUID.randomUUID();

        assertThrows(TicketNotFoundException.class,
            () -> ticketService.getStatus(nonExistentId));
    }
}

