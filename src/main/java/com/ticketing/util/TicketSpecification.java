package com.ticketing.util;

import com.ticketing.entity.Ticket;
import com.ticketing.entity.enums.TicketPriority;
import com.ticketing.entity.enums.TicketStatus;
import org.springframework.data.jpa.domain.Specification;

public class TicketSpecification {

    public static Specification<Ticket> hasStatus(TicketStatus status) {
        return (root, query, cb) ->
            status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<Ticket> hasPriority(TicketPriority priority) {
        return (root, query, cb) ->
            priority == null ? null : cb.equal(root.get("priority"), priority);
    }

    public static Specification<Ticket> hasCategoryId(Long categoryId) {
        return (root, query, cb) ->
            categoryId == null ? null : cb.equal(root.get("category").get("id"), categoryId);
    }

    public static Specification<Ticket> hasAgentId(Long agentId) {
        return (root, query, cb) ->
            agentId == null ? null : cb.equal(root.get("assignedAgent").get("id"), agentId);
    }

    // Combine all into one
    public static Specification<Ticket> withFilters(
            TicketStatus status,
            TicketPriority priority,
            Long categoryId,
            Long agentId) {
        return Specification
            .where(hasStatus(status))
            .and(hasPriority(priority))
            .and(hasCategoryId(categoryId))
            .and(hasAgentId(agentId));
    }
}
