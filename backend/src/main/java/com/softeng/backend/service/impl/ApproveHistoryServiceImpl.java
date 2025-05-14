package com.softeng.backend.service.impl;

import com.softeng.backend.dto.AccountantDTOs;
import com.softeng.backend.repository.ApproveHistoryRepository;
import com.softeng.backend.service.ApproveHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApproveHistoryServiceImpl implements ApproveHistoryService {
    private final ApproveHistoryRepository approveHistoryRepository;

    @Autowired
    public ApproveHistoryServiceImpl(ApproveHistoryRepository approveHistoryRepository) {
        this.approveHistoryRepository = approveHistoryRepository;
    }

    @Override
    public List<AccountantDTOs.StatEntry> getApprovedExpensesByEmployee(String personalNo) {
        return approveHistoryRepository.findApprovedByEmployeeId(personalNo);
    }

    @Override
    public List<AccountantDTOs.StatEntry> getApprovedExpensesByDepartment(Integer deptId) {
        return approveHistoryRepository.findApprovedByDepartmentId(deptId);
    }

    @Override
    public List<AccountantDTOs.StatEntry> getApprovedExpensesByCostType(String costType) {
        return approveHistoryRepository.findApprovedByCostType(costType);
    }
}