package com.ticketing.repository;

import com.ticketing.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// TODO: implemented in Phase X
@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
