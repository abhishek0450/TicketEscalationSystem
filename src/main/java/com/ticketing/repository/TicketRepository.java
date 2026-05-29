package com.ticketing.repository;

import com.ticketing.entity.Ticket;
import com.ticketing.entity.enums.TicketPriority;
import com.ticketing.entity.enums.TicketStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long>, JpaSpecificationExecutor<Ticket> {
    List<Ticket> findByCreatedBy_Id(Long userId);
    List<Ticket> findByAssignedAgent_Id(Long agentId);
    List<Ticket> findByStatus(TicketStatus status);
    List<Ticket> findByPriority(TicketPriority priority);
    List<Ticket> findByStatusNot(TicketStatus status);
    Page<Ticket> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query("SELECT t FROM Ticket t WHERE t.status NOT IN (com.ticketing.entity.enums.TicketStatus.RESOLVED, com.ticketing.entity.enums.TicketStatus.CLOSED, com.ticketing.entity.enums.TicketStatus.ESCALATED) AND t.dueAt < :now")
    List<Ticket> findBreachedTickets(@Param("now") LocalDateTime now);

    long countByStatus(TicketStatus status);
    long countByPriority(TicketPriority priority);
    long countByStatusAndAssignedAgent_Id(TicketStatus status, Long agentId);
}
