package com.ticketing.service.impl;

import com.ticketing.dto.request.CreateTicketRequest;
import com.ticketing.dto.request.UpdateTicketRequest;
import com.ticketing.dto.request.AssignTicketRequest;
import com.ticketing.dto.request.UpdateStatusRequest;
import com.ticketing.dto.response.TicketResponse;
import com.ticketing.dto.response.TicketSummaryResponse;
import com.ticketing.dto.response.UserSummaryResponse;
import com.ticketing.entity.Category;
import com.ticketing.entity.SlaConfig;
import com.ticketing.entity.Ticket;
import com.ticketing.entity.TicketHistory;
import com.ticketing.entity.User;
import com.ticketing.entity.enums.RoleName;
import com.ticketing.entity.enums.TicketPriority;
import com.ticketing.entity.enums.TicketStatus;
import com.ticketing.exception.InvalidStatusTransitionException;
import com.ticketing.exception.TicketNotFoundException;
import com.ticketing.exception.UnauthorizedAccessException;
import com.ticketing.exception.UserNotFoundException;
import com.ticketing.repository.CategoryRepository;
import com.ticketing.repository.SlaConfigRepository;
import com.ticketing.repository.TicketHistoryRepository;
import com.ticketing.repository.TicketRepository;
import com.ticketing.repository.UserRepository;
import com.ticketing.service.TicketService;
import com.ticketing.util.Constants;
import com.ticketing.util.TicketSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Service
@Slf4j
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SlaConfigRepository slaConfigRepository;

    @Autowired
    private TicketHistoryRepository ticketHistoryRepository;

    @Override
    @Transactional
    public TicketResponse createTicket(CreateTicketRequest request, String createdByEmail) {
        User creator = userRepository.findByEmail(createdByEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + createdByEmail));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + request.getCategoryId()));

        SlaConfig slaConfig = slaConfigRepository.findByPriority(request.getPriority())
                .orElseThrow(() -> new IllegalArgumentException("SLA Configuration not found for priority: " + request.getPriority()));

        LocalDateTime dueAt = LocalDateTime.now().plusHours(slaConfig.getMaxResolutionHours());

        Ticket ticket = Ticket.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .priority(request.getPriority())
                .category(category)
                .createdBy(creator)
                .status(TicketStatus.OPEN)
                .escalationLevel(0)
                .isDeleted(false)
                .dueAt(dueAt)
                .build();

        ticketRepository.save(ticket);

        // Record history entry
        TicketHistory history = TicketHistory.builder()
                .ticket(ticket)
                .fieldChanged("status")
                .oldValue(null)
                .newValue("OPEN")
                .changedBy(creator)
                .build();
        ticketHistoryRepository.save(history);

        log.info("Ticket created: id={}, priority={}, dueAt={}", ticket.getId(), ticket.getPriority(), ticket.getDueAt());

        return mapToTicketResponse(ticket);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TicketSummaryResponse> getAllTickets(TicketStatus status, TicketPriority priority, Long categoryId, Long agentId, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Ticket> spec = TicketSpecification.withFilters(status, priority, categoryId, agentId);
        Page<Ticket> ticketPage = ticketRepository.findAll(spec, pageable);

        return ticketPage.map(this::mapToSummaryResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public TicketResponse getTicketById(Long ticketId, String requestingUserEmail) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found with ID: " + ticketId));

        User requester = userRepository.findByEmail(requestingUserEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + requestingUserEmail));

        boolean isAdmin = requester.getRoles().stream().anyMatch(r -> r.getName() == RoleName.ROLE_ADMIN);
        boolean isAgent = requester.getRoles().stream().anyMatch(r -> r.getName() == RoleName.ROLE_SUPPORT_AGENT);

        if (!isAdmin) {
            if (isAgent) {
                // Any support agent can view unassigned tickets or those assigned to them
                if (ticket.getAssignedAgent() != null && !ticket.getAssignedAgent().getId().equals(requester.getId())) {
                    throw new UnauthorizedAccessException("You are not authorized to view this ticket");
                }
            } else {
                // Employee can only view their own tickets
                if (!ticket.getCreatedBy().getId().equals(requester.getId())) {
                    throw new UnauthorizedAccessException("You are not authorized to view this ticket");
                }
            }
        }

        return mapToTicketResponse(ticket);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TicketSummaryResponse> getMyTickets(String email, int page, int size) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        
        Specification<Ticket> spec = (root, query, cb) -> cb.equal(root.get("createdBy").get("id"), user.getId());
        return ticketRepository.findAll(spec, pageable).map(this::mapToSummaryResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TicketSummaryResponse> getAssignedTickets(String agentEmail, int page, int size) {
        User agent = userRepository.findByEmail(agentEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + agentEmail));

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        
        Specification<Ticket> spec = (root, query, cb) -> cb.equal(root.get("assignedAgent").get("id"), agent.getId());
        return ticketRepository.findAll(spec, pageable).map(this::mapToSummaryResponse);
    }

    @Override
    @Transactional
    public TicketResponse updateTicket(Long ticketId, UpdateTicketRequest request, String updatedByEmail) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found with ID: " + ticketId));

        User requester = userRepository.findByEmail(updatedByEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + updatedByEmail));

        boolean isAdmin = requester.getRoles().stream().anyMatch(r -> r.getName() == RoleName.ROLE_ADMIN);
        boolean isAssignedAgent = ticket.getAssignedAgent() != null && ticket.getAssignedAgent().getId().equals(requester.getId());

        if (!isAdmin && !isAssignedAgent) {
            throw new UnauthorizedAccessException("Only admin or the assigned agent can update this ticket");
        }

        if (request.getTitle() != null && !request.getTitle().equals(ticket.getTitle())) {
            saveHistory(ticket, "title", ticket.getTitle(), request.getTitle(), requester);
            ticket.setTitle(request.getTitle());
        }

        if (request.getDescription() != null && !request.getDescription().equals(ticket.getDescription())) {
            saveHistory(ticket, "description", ticket.getDescription(), request.getDescription(), requester);
            ticket.setDescription(request.getDescription());
        }

        if (request.getPriority() != null && request.getPriority() != ticket.getPriority()) {
            saveHistory(ticket, "priority", ticket.getPriority().name(), request.getPriority().name(), requester);
            ticket.setPriority(request.getPriority());
        }

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + request.getCategoryId()));
            if (!category.getId().equals(ticket.getCategory().getId())) {
                saveHistory(ticket, "category", ticket.getCategory().getName(), category.getName(), requester);
                ticket.setCategory(category);
            }
        }

        ticketRepository.save(ticket);
        log.info("Ticket {} updated by {}", ticket.getId(), updatedByEmail);

        return mapToTicketResponse(ticket);
    }

    @Override
    @Transactional
    public TicketResponse assignTicket(Long ticketId, AssignTicketRequest request, String adminEmail) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found with ID: " + ticketId));

        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + adminEmail));

        User agent = userRepository.findById(request.getAgentId())
                .orElseThrow(() -> new UserNotFoundException("Agent not found with ID: " + request.getAgentId()));

        boolean isSupportAgent = agent.getRoles().stream().anyMatch(r -> r.getName() == RoleName.ROLE_SUPPORT_AGENT);
        if (!isSupportAgent) {
            throw new UnauthorizedAccessException("Target user is not a support agent");
        }

        String oldAgentEmail = ticket.getAssignedAgent() != null ? ticket.getAssignedAgent().getEmail() : "unassigned";
        saveHistory(ticket, "assignedAgent", oldAgentEmail, agent.getEmail(), admin);

        ticket.setAssignedAgent(agent);

        if (ticket.getStatus() == TicketStatus.OPEN) {
            saveHistory(ticket, "status", TicketStatus.OPEN.name(), TicketStatus.IN_PROGRESS.name(), admin);
            ticket.setStatus(TicketStatus.IN_PROGRESS);
        }

        ticketRepository.save(ticket);
        log.info("Ticket {} assigned to agent {} by {}", ticket.getId(), agent.getEmail(), adminEmail);

        return mapToTicketResponse(ticket);
    }

    @Override
    @Transactional
    public TicketResponse updateStatus(Long ticketId, UpdateStatusRequest request, String updatedByEmail) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found with ID: " + ticketId));

        User requester = userRepository.findByEmail(updatedByEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + updatedByEmail));

        TicketStatus currentStatus = ticket.getStatus();
        TicketStatus newStatus = request.getStatus();

        Set<TicketStatus> allowed = Constants.VALID_TRANSITIONS.get(currentStatus);
        if (allowed == null || !allowed.contains(newStatus)) {
            throw new InvalidStatusTransitionException("Cannot transition from " + currentStatus + " to " + newStatus);
        }

        saveHistory(ticket, "status", currentStatus.name(), newStatus.name(), requester);
        ticket.setStatus(newStatus);

        if (newStatus == TicketStatus.RESOLVED) {
            ticket.setResolvedAt(LocalDateTime.now());
        } else if (newStatus == TicketStatus.CLOSED && ticket.getResolvedAt() == null) {
            ticket.setResolvedAt(LocalDateTime.now());
        }

        ticketRepository.save(ticket);
        log.info("Ticket {} status changed from {} to {} by {}", ticket.getId(), currentStatus, newStatus, updatedByEmail);

        return mapToTicketResponse(ticket);
    }

    @Override
    @Transactional
    public void deleteTicket(Long ticketId, String adminEmail) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found with ID: " + ticketId));

        ticket.setDeleted(true);
        ticketRepository.save(ticket);

        log.info("Ticket {} soft-deleted by {}", ticket.getId(), adminEmail);
    }

    private void saveHistory(Ticket ticket, String field, String oldVal, String newVal, User changer) {
        TicketHistory history = TicketHistory.builder()
                .ticket(ticket)
                .fieldChanged(field)
                .oldValue(oldVal)
                .newValue(newVal)
                .changedBy(changer)
                .build();
        ticketHistoryRepository.save(history);
    }

    @Override
    public TicketResponse mapToTicketResponse(Ticket ticket) {
        return TicketResponse.builder()
                .id(ticket.getId())
                .title(ticket.getTitle())
                .description(ticket.getDescription())
                .status(ticket.getStatus().name())
                .priority(ticket.getPriority().name())
                .categoryName(ticket.getCategory().getName())
                .categoryId(ticket.getCategory().getId())
                .createdBy(mapToUserSummary(ticket.getCreatedBy()))
                .assignedAgent(mapToUserSummary(ticket.getAssignedAgent()))
                .escalationLevel(ticket.getEscalationLevel())
                .isDeleted(ticket.isDeleted())
                .createdAt(ticket.getCreatedAt())
                .updatedAt(ticket.getUpdatedAt())
                .resolvedAt(ticket.getResolvedAt())
                .dueAt(ticket.getDueAt())
                .build();
    }

    @Override
    public TicketSummaryResponse mapToSummaryResponse(Ticket ticket) {
        return TicketSummaryResponse.builder()
                .id(ticket.getId())
                .title(ticket.getTitle())
                .status(ticket.getStatus().name())
                .priority(ticket.getPriority().name())
                .categoryName(ticket.getCategory().getName())
                .createdBy(mapToUserSummary(ticket.getCreatedBy()))
                .assignedAgent(mapToUserSummary(ticket.getAssignedAgent()))
                .escalationLevel(ticket.getEscalationLevel())
                .createdAt(ticket.getCreatedAt())
                .updatedAt(ticket.getUpdatedAt())
                .dueAt(ticket.getDueAt())
                .build();
    }

    private UserSummaryResponse mapToUserSummary(User user) {
        if (user == null) {
            return null;
        }
        return UserSummaryResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .department(user.getDepartment())
                .build();
    }
}
