package com.ticketing.repository;

import com.ticketing.entity.EscalationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// TODO: implemented in Phase X
@Repository
public interface EscalationLogRepository extends JpaRepository<EscalationLog, Long> {
}
