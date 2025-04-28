package com.softeng.backend.controller;

import com.softeng.backend.dto.TicketDTOs;
import com.softeng.backend.model.User;
import com.softeng.backend.service.TicketService2;
import com.softeng.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/tickets")
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "Ticket Controller", description = "Endpoints for ticket management")
public class TicketController2 {
    private static final Logger logger = LoggerFactory.getLogger(TicketController2.class);

    private final TicketService2 ticketService2;
    private final UserService userService;

    @Autowired
    public TicketController2(TicketService2 ticketService2, UserService userService) {
        this.ticketService2 = ticketService2;
        this.userService = userService;
    }

    @PostMapping("/create")
    @Operation(summary = "Create a new ticket", description = "Creates a new ticket with the specified details")
    public ResponseEntity<?> createTicket(@RequestBody TicketDTOs.CreateTicketRequest request, Authentication authentication) {
        logger.debug("Creating new ticket with cost type: {}", request.getCostType());
        
        String personalNo = authentication.getName();
        User currentUser = userService.getUserByPersonalNo(personalNo);
        
        if (currentUser.getUserType() != User.UserType.team_member) {
            return ResponseEntity.status(403)
                    .body(new TicketDTOs.TicketResponse(
                            TicketDTOs.CreateTicketResponse.INVALID_AUTHENTICATION.getMessage()
                    ));
        }

        Map<String, Object> result = ticketService2.createTicket(personalNo, request);
        TicketDTOs.CreateTicketResponse status = (TicketDTOs.CreateTicketResponse) result.get("status");
        boolean success = (boolean) result.get("success");
        
        if (success) {
            return ResponseEntity.ok()
                    .body(new TicketDTOs.TicketResponse(status.getMessage()));
        } else {
            Map<String, Object> budgets = result.containsKey("budgets") ? 
                    (Map<String, Object>) result.get("budgets") : 
                    new HashMap<>();
            
            return ResponseEntity.status(400)
                    .body(new TicketDTOs.TicketResponse(status.getMessage(), budgets));
        }
    }
    
    @GetMapping("/employee")
    @Operation(summary = "Get employee tickets", description = "Retrieves all tickets created by the authenticated employee")
    public ResponseEntity<?> getEmployeeTickets(Authentication authentication) {
        logger.debug("Getting tickets for employee");
        
        String personalNo = authentication.getName();
        User currentUser = userService.getUserByPersonalNo(personalNo);
        
        if (currentUser.getUserType() != User.UserType.team_member) {
            return ResponseEntity.status(403)
                    .body("Unauthorized access");
        }
        
        return ResponseEntity.ok(ticketService2.getTicketsByEmployee(currentUser));
    }
    
    @GetMapping("/manager")
    @Operation(summary = "Get manager tickets", description = "Retrieves all tickets assigned to the authenticated manager")
    public ResponseEntity<?> getManagerTickets(Authentication authentication) {
        logger.debug("Getting tickets for manager");
        
        String personalNo = authentication.getName();
        User currentUser = userService.getUserByPersonalNo(personalNo);
        
        if (currentUser.getUserType() != User.UserType.manager) {
            return ResponseEntity.status(403)
                    .body("Unauthorized access");
        }
        
        return ResponseEntity.ok(ticketService2.getTicketsByManager(currentUser));
    }
}