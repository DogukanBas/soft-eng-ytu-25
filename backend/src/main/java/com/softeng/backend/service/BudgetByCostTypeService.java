package com.softeng.backend.service;

import com.softeng.backend.model.BudgetByCostType;

import java.util.List;
import java.util.Optional;

public interface BudgetByCostTypeService {
    void addBudgetByCostType(String typeName, Double initialBudget);
    List<BudgetByCostType> getAllCostTypes();
    Optional<BudgetByCostType> getByTypeName(String typeName);
    boolean existsByTypeName(String typeName);
    void setInitialBudgetByTypeName(String typeName, Double initialBudget);
    void setRemainingBudgetByTypeName(String typeName, Double remainingBudget);
    void resetBudgetByTypeName(String typeName);

}
