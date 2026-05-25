package com.ticketing.service.impl;

import com.ticketing.dto.request.CreateTicketRequest;
import com.ticketing.dto.response.TicketResponse;
import com.ticketing.service.TicketService;
import org.springframework.stereotype.Service;
import java.util.List;

// TODO: implemented in Phase X
@Service
public class TicketServiceImpl implements TicketService {

    @Override
    public TicketResponse createTicket(CreateTicketRequest request) {
        return null;
    }

    @Override
    public TicketResponse getTicketById(Long id) {
        return null;
    }

    @Override
    public List<TicketResponse> getAllTickets() {
        return null;
    }
}
