package org.lab.service;

import org.lab.exception.TicketNotFoundException;
import org.lab.model.Ticket;
import org.lab.model.TicketStatus;
import org.lab.repository.TicketRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TicketService {
    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Ticket create(UUID projectId, UUID milestoneId, String description) {
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

        List<UUID> developers = new ArrayList<>(ticket.assignedDevelopers());
        if (!developers.contains(developerId)) {
            developers.add(developerId);
        }

        ticketRepository.save(ticket.withAssignedDevelopers(developers));
    }

    public TicketStatus getStatus(UUID ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
            .orElseThrow(() -> new TicketNotFoundException(ticketId));
        return ticket.status();
    }

    public void complete(UUID ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
            .orElseThrow(() -> new TicketNotFoundException(ticketId));

        ticketRepository.save(ticket.withStatus(TicketStatus.COMPLETED));
    }
}

