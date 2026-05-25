package com.ticketing.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// TODO: implemented in Phase X
@RestController
@RequestMapping("/api/escalations")
public class EscalationController {

    @PostMapping("/trigger")
    public ResponseEntity<Void> triggerEscalationCheck() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{ticketId}")
    public ResponseEntity<Void> escalateTicket(@PathVariable Long ticketId) {
        return ResponseEntity.ok().build();
    }
}
