package com.ticketing.repository;

import com.ticketing.entity.SlaConfig;
import com.ticketing.entity.enums.TicketPriority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SlaConfigRepository extends JpaRepository<SlaConfig, Long> {
    Optional<SlaConfig> findByPriority(TicketPriority priority);
    List<SlaConfig> findAll();
}
