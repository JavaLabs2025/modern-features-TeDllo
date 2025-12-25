package org.lab.service;

import org.lab.exception.MilestoneNotFoundException;
import org.lab.model.Milestone;
import org.lab.model.MilestoneStatus;
import org.lab.repository.MilestoneRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

public class MilestoneService {
    private final MilestoneRepository milestoneRepository;

    public MilestoneService(MilestoneRepository milestoneRepository) {
        this.milestoneRepository = milestoneRepository;
    }

    public Milestone create(UUID projectId, LocalDate startDate, LocalDate endDate) {
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

        milestoneRepository.save(milestone.withStatus(status));
    }
}

