package com.ticketing.util;

import com.ticketing.entity.enums.TicketStatus;
import java.util.Map;
import java.util.Set;

public final class Constants {
    private Constants() {}

    public static final String SYSTEM_USER = "SYSTEM";

    public static final Map<TicketStatus, Set<TicketStatus>> VALID_TRANSITIONS =
        Map.of(
            TicketStatus.OPEN,        Set.of(TicketStatus.IN_PROGRESS, TicketStatus.ESCALATED),
            TicketStatus.IN_PROGRESS, Set.of(TicketStatus.RESOLVED, TicketStatus.ESCALATED, TicketStatus.OPEN),
            TicketStatus.RESOLVED,    Set.of(TicketStatus.CLOSED),
            TicketStatus.ESCALATED,   Set.of(TicketStatus.IN_PROGRESS, TicketStatus.RESOLVED),
            TicketStatus.CLOSED,      Set.of()
        );
}
