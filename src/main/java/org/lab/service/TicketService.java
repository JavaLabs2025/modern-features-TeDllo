package org.lab.service;

import org.lab.auth.AuthService;
import org.lab.auth.model.Permission;
import org.lab.exception.TicketNotFoundException;
import org.lab.model.Ticket;
import org.lab.model.TicketStatus;
import org.lab.repository.TicketRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TicketService {
    private final TicketRepository ticketRepository;
    private final AuthService authService;

    public TicketService(TicketRepository ticketRepository, AuthService authService) {
        this.ticketRepository = ticketRepository;
        this.authService = authService;
    }

    public Ticket create(UUID projectId, UUID milestoneId, String description) {
        authService.checkPermission(projectId, Permission.TICKET_CREATE);
        
        Ticket ticket = new Ticket(
            UUID.randomUUID(),
            projectId,
            milestoneId,
            description,
            new ArrayList<>(),
            TicketStatus.NEW
        );
        return ticketRepository.save(ticket);
    }

    public List<Ticket> listByUser(UUID userId) {
        return ticketRepository.findAll().stream()
            .filter(ticket -> ticket.assignedDevelopers().contains(userId))
            .toList();
    }

    public void assignDeveloper(UUID ticketId, UUID developerId) {
        Ticket ticket = ticketRepository.findById(ticketId)
            .orElseThrow(() -> new TicketNotFoundException(ticketId));
        
        authService.checkPermission(ticket.projectId(), Permission.TICKET_ASSIGN_DEVELOPER);

        List<UUID> developers = new ArrayList<>(ticket.assignedDevelopers());
        if (!developers.contains(developerId)) {
            developers.add(developerId);
        }

        ticketRepository.save(ticket.withAssignedDevelopers(developers));
    }

    public TicketStatus getStatus(UUID ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
            .orElseThrow(() -> new TicketNotFoundException(ticketId));
        
        authService.checkPermission(ticket.projectId(), Permission.TICKET_GET_STATUS);
        
        return ticket.status();
    }

    public void complete(UUID ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
            .orElseThrow(() -> new TicketNotFoundException(ticketId));
        
        authService.checkPermission(ticket.projectId(), Permission.TICKET_COMPLETE);

        ticketRepository.save(ticket.withStatus(TicketStatus.COMPLETED));
    }
}

