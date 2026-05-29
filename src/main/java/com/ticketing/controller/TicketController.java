package com.ticketing.controller;

import com.ticketing.dto.request.CreateTicketRequest;
import com.ticketing.dto.request.UpdateTicketRequest;
import com.ticketing.dto.request.AssignTicketRequest;
import com.ticketing.dto.request.UpdateStatusRequest;
import com.ticketing.dto.response.TicketResponse;
import com.ticketing.dto.response.TicketSummaryResponse;
import com.ticketing.entity.enums.TicketPriority;
import com.ticketing.entity.enums.TicketStatus;
import com.ticketing.service.TicketService;
import com.ticketing.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
@Slf4j
public class TicketController {

    @Autowired
    private TicketService ticketService;

    // CREATE — any authenticated user
    @PostMapping
    public ResponseEntity<ApiResponse<TicketResponse>> createTicket(
            @Valid @RequestBody CreateTicketRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.debug("User {} creating ticket with title: {}", userDetails.getUsername(), request.getTitle());
        TicketResponse response = ticketService.createTicket(request, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Ticket created successfully", response));
    }

    // GET ALL — admin sees all, filtered + paginated
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<TicketSummaryResponse>>> getAllTickets(
            @RequestParam(required = false) TicketStatus status,
            @RequestParam(required = false) TicketPriority priority,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long agentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        log.debug("Admin requesting all tickets with filters - status: {}, priority: {}, page: {}", status, priority, page);
        Page<TicketSummaryResponse> response = ticketService.getAllTickets(status, priority, categoryId, agentId, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success("Tickets retrieved successfully", response));
    }

    // GET MY TICKETS — employee sees own tickets
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<Page<TicketSummaryResponse>>> getMyTickets(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.debug("Employee {} requesting their tickets - page: {}", userDetails.getUsername(), page);
        Page<TicketSummaryResponse> response = ticketService.getMyTickets(userDetails.getUsername(), page, size);
        return ResponseEntity.ok(ApiResponse.success("Your tickets retrieved successfully", response));
    }

    // GET ASSIGNED — agent sees their assigned tickets
    @GetMapping("/assigned")
    @PreAuthorize("hasAnyRole('SUPPORT_AGENT','ADMIN')")
    public ResponseEntity<ApiResponse<Page<TicketSummaryResponse>>> getAssignedTickets(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.debug("Agent {} requesting assigned tickets - page: {}", userDetails.getUsername(), page);
        Page<TicketSummaryResponse> response = ticketService.getAssignedTickets(userDetails.getUsername(), page, size);
        return ResponseEntity.ok(ApiResponse.success("Assigned tickets retrieved successfully", response));
    }

    // GET BY ID — role-based access enforced in service
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TicketResponse>> getTicketById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.debug("User {} requesting ticket ID: {}", userDetails.getUsername(), id);
        TicketResponse response = ticketService.getTicketById(id, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Ticket retrieved successfully", response));
    }

    // UPDATE TICKET — agent or admin
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPPORT_AGENT','ADMIN')")
    public ResponseEntity<ApiResponse<TicketResponse>> updateTicket(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTicketRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.debug("User {} updating ticket ID: {}", userDetails.getUsername(), id);
        TicketResponse response = ticketService.updateTicket(id, request, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Ticket updated successfully", response));
    }

    // ASSIGN TICKET — admin only
    @PutMapping("/{id}/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TicketResponse>> assignTicket(
            @PathVariable Long id,
            @Valid @RequestBody AssignTicketRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.debug("Admin {} assigning ticket ID: {} to agent ID: {}", userDetails.getUsername(), id, request.getAgentId());
        TicketResponse response = ticketService.assignTicket(id, request, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Ticket assigned successfully", response));
    }

    // UPDATE STATUS — agent or admin
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('SUPPORT_AGENT','ADMIN')")
    public ResponseEntity<ApiResponse<TicketResponse>> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStatusRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.debug("User {} updating status of ticket ID: {} to status: {}", userDetails.getUsername(), id, request.getStatus());
        TicketResponse response = ticketService.updateStatus(id, request, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Status updated successfully", response));
    }

    // DELETE — admin only, soft delete
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteTicket(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.debug("Admin {} soft-deleting ticket ID: {}", userDetails.getUsername(), id);
        ticketService.deleteTicket(id, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Ticket soft-deleted successfully"));
    }
}
