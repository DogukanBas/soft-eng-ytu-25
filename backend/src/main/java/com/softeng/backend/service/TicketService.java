package com.softeng.backend.service;

import com.softeng.backend.dto.TicketDTOs;
import com.softeng.backend.model.ApproveHistory;
import com.softeng.backend.model.Attachment;
import com.softeng.backend.model.Ticket;

import java.util.List;

public interface TicketService {
    List<Ticket> getTicketByEmployeeId(String personalNo);
    List<Ticket> getTicketByManagerId(String PersonalNo);
    void addAttachment(Attachment attachment);
    void addTicket(Ticket ticket);
    void addApproveHistory(ApproveHistory approveHistory);
    List<Integer> getCreatedClosedTicketIdsByPersonalNo(String personalNo);
    List<Integer> getCreatedActiveTicketIdsByPersonalNo(String personalNo);
    List<Integer> getAssignedClosedTicketIdsByPersonalNo(String personalNo);
    List<Integer> getAssignedActiveTicketIdsByPersonalNo(String personalNo);
    TicketDTOs.TicketWithoutInvoiceResponse getTicketById(Integer ticketId);
    List<TicketDTOs.ApproveHistoryResponse> getApproveHistoryByTicketId(Integer ticketId);
}