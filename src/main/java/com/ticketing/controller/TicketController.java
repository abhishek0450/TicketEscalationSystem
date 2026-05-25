package com.ticketing.controller;

import com.ticketing.dto.request.CreateTicketRequest;
import com.ticketing.dto.response.TicketResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// TODO: implemented in Phase X
@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @PostMapping
    public ResponseEntity<TicketResponse> createTicket(@RequestBody CreateTicketRequest request) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponse> getTicketById(@PathVariable Long id) {
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<TicketResponse>> getAllTickets() {
        return ResponseEntity.ok().build();
    }
}
