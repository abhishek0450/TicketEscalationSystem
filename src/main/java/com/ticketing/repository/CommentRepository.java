package com.ticketing.repository;

import com.ticketing.entity.TicketComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// TODO: implemented in Phase X
@Repository
public interface CommentRepository extends JpaRepository<TicketComment, Long> {
}
