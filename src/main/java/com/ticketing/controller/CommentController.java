package com.ticketing.controller;

import com.ticketing.dto.request.AddCommentRequest;
import com.ticketing.dto.response.CommentResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// TODO: implemented in Phase X
@RestController
@RequestMapping("/api/tickets/{ticketId}/comments")
public class CommentController {

    @PostMapping
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable Long ticketId,
            @RequestBody AddCommentRequest request) {
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable Long ticketId) {
        return ResponseEntity.ok().build();
    }
}
