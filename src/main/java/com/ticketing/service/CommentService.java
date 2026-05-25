package com.ticketing.service;

import com.ticketing.dto.request.AddCommentRequest;
import com.ticketing.dto.response.CommentResponse;
import java.util.List;

// TODO: implemented in Phase X
public interface CommentService {
    CommentResponse addComment(Long ticketId, AddCommentRequest request);
    List<CommentResponse> getCommentsByTicketId(Long ticketId);
}
