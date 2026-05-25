package com.ticketing.service.impl;

import com.ticketing.dto.request.AddCommentRequest;
import com.ticketing.dto.response.CommentResponse;
import com.ticketing.service.CommentService;
import org.springframework.stereotype.Service;
import java.util.List;

// TODO: implemented in Phase X
@Service
public class CommentServiceImpl implements CommentService {

    @Override
    public CommentResponse addComment(Long ticketId, AddCommentRequest request) {
        return null;
    }

    @Override
    public List<CommentResponse> getCommentsByTicketId(Long ticketId) {
        return null;
    }
}
