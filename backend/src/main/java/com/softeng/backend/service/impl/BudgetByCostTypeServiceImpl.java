package com.softeng.backend.service.impl;

import com.softeng.backend.exception.ResourceNotFoundException;
import com.softeng.backend.model.BudgetByCostType;
import com.softeng.backend.repository.BudgetByCostTypeRepository;
import com.softeng.backend.service.BudgetByCostTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BudgetByCostTypeServiceImpl implements BudgetByCostTypeService {
    private final BudgetByCostTypeRepository budgetByCostTypeRepository;
    @Autowired
    public BudgetByCostTypeServiceImpl(BudgetByCostTypeRepository budgetByCostTypeRepository) {
        this.budgetByCostTypeRepository = budgetByCostTypeRepository;
    }

    @Override
    public void addBudgetByCostType(String typeName, Double initialBudget, Double maxCost) {
        BudgetByCostType budgetByCostType = new BudgetByCostType(typeName, initialBudget, maxCost);
        budgetByCostTypeRepository.save(budgetByCostType);
    }

    @Override
    public List<BudgetByCostType> getAllCostTypes() {
        return budgetByCostTypeRepository.findAll();
    }

    @Override
    public List<String> getAllCostTypeNames() {
        return budgetByCostTypeRepository.findAllTypeNames();
    }

    @Override
    public BudgetByCostType getByTypeName(String typeName) {
        return budgetByCostTypeRepository.findByTypeName(typeName)
                .orElseThrow(() -> new IllegalArgumentException("Budget not found with type name: " + typeName));
    }

    @Override
    public boolean existsByTypeName(String typeName) {
        return budgetByCostTypeRepository.existsByTypeName(typeName);
    }
    @Override
    public boolean existsById(Integer id){
        return budgetByCostTypeRepository.existsById(id);


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
    public void setMaxCostByTypeName(String typeName, Double maxCost) {
        budgetByCostTypeRepository.setMaxCostByTypeName(typeName, maxCost);
    }

    @Override
    @Transactional
    public void resetBudgetByTypeName(String typeName) {
        budgetByCostTypeRepository.resetBudgetByTypeName(typeName);
    }

    @Override
    public BudgetByCostType getById(Integer id) {
        return budgetByCostTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found with id: " + id));
    }
}


