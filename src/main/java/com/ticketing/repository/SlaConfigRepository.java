package com.ticketing.repository;

import com.ticketing.entity.SlaConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// TODO: implemented in Phase X
@Repository
public interface SlaConfigRepository extends JpaRepository<SlaConfig, Long> {
}
