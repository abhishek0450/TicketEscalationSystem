package com.ticketing.repository;

import com.ticketing.entity.TicketComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<TicketComment, Long> {
    List<TicketComment> findByTicket_IdOrderByCreatedAtAsc(Long ticketId);
    List<TicketComment> findByTicket_IdAndIsInternalFalseOrderByCreatedAtAsc(Long ticketId);
}
