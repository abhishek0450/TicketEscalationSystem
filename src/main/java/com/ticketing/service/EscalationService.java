package com.ticketing.service;

// TODO: implemented in Phase X
public interface EscalationService {
    void checkForEscalations();
    void escalateTicket(Long ticketId);
}
