package com.ticketing.service;

import com.ticketing.dto.request.CreateTicketRequest;
import com.ticketing.dto.response.TicketResponse;
import java.util.List;

// TODO: implemented in Phase X
public interface TicketService {
    TicketResponse createTicket(CreateTicketRequest request);
    TicketResponse getTicketById(Long id);
    List<TicketResponse> getAllTickets();
}
