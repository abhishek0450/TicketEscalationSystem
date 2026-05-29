package com.ticketing.service;

import com.ticketing.dto.request.CreateTicketRequest;
import com.ticketing.dto.request.UpdateTicketRequest;
import com.ticketing.dto.request.AssignTicketRequest;
import com.ticketing.dto.request.UpdateStatusRequest;
import com.ticketing.dto.response.TicketResponse;
import com.ticketing.dto.response.TicketSummaryResponse;
import com.ticketing.entity.Ticket;
import com.ticketing.entity.enums.TicketPriority;
import com.ticketing.entity.enums.TicketStatus;
import org.springframework.data.domain.Page;

public interface TicketService {

    // Create
    TicketResponse createTicket(CreateTicketRequest request, String createdByEmail);

    // Read
    Page<TicketSummaryResponse> getAllTickets(
        TicketStatus status,
        TicketPriority priority,
        Long categoryId,
        Long agentId,
        int page,
        int size,
        String sortBy,
        String sortDir
    );

    TicketResponse getTicketById(Long ticketId, String requestingUserEmail);

    Page<TicketSummaryResponse> getMyTickets(String email, int page, int size);

    Page<TicketSummaryResponse> getAssignedTickets(String agentEmail, int page, int size);

    // Update
    TicketResponse updateTicket(Long ticketId, UpdateTicketRequest request, String updatedByEmail);

    TicketResponse assignTicket(Long ticketId, AssignTicketRequest request, String adminEmail);

    TicketResponse updateStatus(Long ticketId, UpdateStatusRequest request, String updatedByEmail);

    // Delete
    void deleteTicket(Long ticketId, String adminEmail);

    // Mapper helper
    TicketResponse mapToTicketResponse(Ticket ticket);
    TicketSummaryResponse mapToSummaryResponse(Ticket ticket);
}
