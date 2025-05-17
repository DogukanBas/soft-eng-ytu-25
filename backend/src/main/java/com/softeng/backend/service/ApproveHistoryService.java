package com.softeng.backend.service;

import com.softeng.backend.dto.AccountantDTOs;
import java.util.List;

public interface ApproveHistoryService {
    List<AccountantDTOs.StatEntry> getApprovedExpensesByEmployee(String personalNo);
    List<AccountantDTOs.StatEntry> getApprovedExpensesByDepartment(Integer deptId);
    List<AccountantDTOs.StatEntry> getApprovedExpensesByCostType(Integer id);
}