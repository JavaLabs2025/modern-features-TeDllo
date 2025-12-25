package org.lab.service;

import org.lab.auth.AuthService;
import org.lab.auth.model.Permission;
import org.lab.exception.ActiveMilestoneExistsException;
import org.lab.exception.MilestoneNotFoundException;
import org.lab.exception.NotAllTicketsCompletedException;
import org.lab.model.Milestone;
import org.lab.model.MilestoneStatus;
import org.lab.model.Ticket;
import org.lab.model.TicketStatus;
import org.lab.repository.MilestoneRepository;
import org.lab.repository.TicketRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public class MilestoneService {
    private final MilestoneRepository milestoneRepository;
    private final TicketRepository ticketRepository;
    private final AuthService authService;

    public MilestoneService(MilestoneRepository milestoneRepository, TicketRepository ticketRepository, AuthService authService) {
        this.milestoneRepository = milestoneRepository;
        this.ticketRepository = ticketRepository;
        this.authService = authService;
    }

    public Milestone create(UUID projectId, LocalDate startDate, LocalDate endDate) {
        authService.checkPermission(projectId, Permission.MILESTONE_CREATE);
        
        Milestone milestone = new Milestone(
            UUID.randomUUID(),
            projectId,
            startDate,
            endDate,
            new ArrayList<>(),
            MilestoneStatus.OPENED
        );
        return milestoneRepository.save(milestone);
    }

    public void setStatus(UUID milestoneId, MilestoneStatus status) {
        Milestone milestone = milestoneRepository.findById(milestoneId)
            .orElseThrow(() -> new MilestoneNotFoundException(milestoneId));
        
        authService.checkPermission(milestone.projectId(), Permission.MILESTONE_SET_STATUS);
        
        if (status == MilestoneStatus.CLOSED) {
            boolean allTicketsCompleted = milestone.ticketIds().stream()
                    .map(ticketRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .allMatch(ticket -> ticket.status() == TicketStatus.COMPLETED);
            
            if (!allTicketsCompleted) {
                throw new NotAllTicketsCompletedException(milestoneId);
            }
        }
        
        if (status == MilestoneStatus.ACTIVE) {
            boolean hasActiveMilestone = milestoneRepository.findAll().stream()
                    .anyMatch(m -> m.projectId().equals(milestone.projectId()) 
                            && m.status() == MilestoneStatus.ACTIVE 
                            && !m.id().equals(milestoneId));
            
            if (hasActiveMilestone) {
                throw new ActiveMilestoneExistsException(milestone.projectId());
            }
        }

        milestoneRepository.save(milestone.withStatus(status));
    }
}

