package com.softeng.backend.service.impl;

import com.softeng.backend.model.BudgetByCostType;
import com.softeng.backend.repository.BudgetByCostTypeRepository;
import com.softeng.backend.service.BudgetByCostTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BudgetByCostTypeServiceImpl implements BudgetByCostTypeService {
    private final BudgetByCostTypeRepository budgetByCostTypeRepository;
    @Autowired
    public BudgetByCostTypeServiceImpl(BudgetByCostTypeRepository budgetByCostTypeRepository) {
        this.budgetByCostTypeRepository = budgetByCostTypeRepository;
    }

    @Override
    public void addBudgetByCostType(String typeName, Double initialBudget) {
        BudgetByCostType budgetByCostType = new BudgetByCostType(typeName, initialBudget);
        budgetByCostTypeRepository.save(budgetByCostType);
    }

    @Override
    public Optional<BudgetByCostType> findByTypeName(String typeName) {
        return budgetByCostTypeRepository.findByTypeName(typeName);
    }

    @Override
    public boolean existsByTypeName(String typeName) {
        return budgetByCostTypeRepository.existsByTypeName(typeName);
    }

    @Override
    @Transactional
    public void setInitialBudgetByTypeName(String typeName, Double initialBudget) {
        budgetByCostTypeRepository.setInitialBudgetByTypeName(typeName, initialBudget);
    }

    @Override
    @Transactional
    public void setRemainingBudgetByTypeName(String typeName, Double remainingBudget) {
        budgetByCostTypeRepository.setRemainingBudgetByTypeName(typeName, remainingBudget);
    }

    @Override
    @Transactional
    public void resetBudgetByTypeName(String typeName) {
        budgetByCostTypeRepository.resetBudgetByTypeName(typeName);
    }
}


