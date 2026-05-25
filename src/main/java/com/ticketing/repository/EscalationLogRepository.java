package com.ticketing.repository;

import com.ticketing.entity.EscalationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EscalationLogRepository extends JpaRepository<EscalationLog, Long> {
    List<EscalationLog> findByTicket_IdOrderByEscalatedAtDesc(Long ticketId);
    Page<EscalationLog> findAllByOrderByEscalatedAtDesc(Pageable pageable);
}
