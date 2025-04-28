package com.softeng.backend.service;

import com.softeng.backend.dto.TicketDTOs;
import com.softeng.backend.model.Ticket;
import com.softeng.backend.model.User;

import java.util.List;
import java.util.Map;

public interface TicketService2 {
    Map<String, Object> createTicket(String personalNo, TicketDTOs.CreateTicketRequest request);
    
    List<Ticket> getTicketsByEmployee(User employee);
    
    List<Ticket> getTicketsByManager(User manager);
}