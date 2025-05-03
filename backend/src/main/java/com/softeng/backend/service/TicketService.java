package com.softeng.backend.service;

import com.softeng.backend.model.ApproveHistory;
import com.softeng.backend.model.Attachment;
import com.softeng.backend.model.Employee;
import com.softeng.backend.model.Ticket;

import java.util.List;

public interface TicketService {
    List<Ticket> getTicketByEmployeeId(String personalNo);
    List<Ticket> getTicketByManagerId(String PersonalNo);
    void addAttachment(Attachment attachment);
    void addTicket(Ticket ticket);
    void addApproveHistory(ApproveHistory approveHistory);



}