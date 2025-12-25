package org.lab.repository;

import org.lab.model.Ticket;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TicketRepository {
    Ticket save(Ticket ticket);
    
    Optional<Ticket> findById(UUID id);
    
    List<Ticket> findAll();
    
    void deleteById(UUID id);
}

