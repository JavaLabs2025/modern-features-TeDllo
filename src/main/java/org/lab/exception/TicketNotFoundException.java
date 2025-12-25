package org.lab.exception;

import java.util.UUID;
import java.util.function.Supplier;

public class TicketNotFoundException extends RuntimeException {
    public TicketNotFoundException(UUID ticketId) {
        super("Ticket not found: " + ticketId);
    }
    
    public static Supplier<TicketNotFoundException> supplier(UUID ticketId) {
        return () -> new TicketNotFoundException(ticketId);
    }
}

