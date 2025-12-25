package org.lab.repository.inmemory;

import org.lab.model.Ticket;
import org.lab.repository.TicketRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryTicketRepository implements TicketRepository {
    private final Map<UUID, Ticket> storage = new ConcurrentHashMap<>();
    
    @Override
    public Ticket save(Ticket ticket) {
        storage.put(ticket.id(), ticket);
        return ticket;
    }
    
    @Override
    public Optional<Ticket> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }
    
    @Override
    public List<Ticket> findAll() {
        return new ArrayList<>(storage.values());
    }
    
    @Override
    public void deleteById(UUID id) {
        storage.remove(id);
    }
}

