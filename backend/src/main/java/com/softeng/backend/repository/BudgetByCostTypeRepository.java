package com.softeng.backend.repository;

import com.softeng.backend.model.BudgetByCostType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetByCostTypeRepository  extends JpaRepository<BudgetByCostType, Integer> {
    List<BudgetByCostType> findAll();
    Optional<BudgetByCostType> findByTypeName(String typeName);

    boolean existsByTypeName(String typeName);

    @Modifying
    @Query("UPDATE BudgetByCostType b SET b.initialBudget = :initialBudget " +
           "WHERE b.typeName = :typeName")
    void setInitialBudgetByTypeName(String typeName, Double initialBudget);

    @Modifying
    @Query("UPDATE BudgetByCostType b SET b.remainingBudget = :remainingBudget " +
           "WHERE b.typeName = :typeName")
    void setRemainingBudgetByTypeName(String typeName, Double remainingBudget);

    @Modifying
    @Query("UPDATE BudgetByCostType b SET b.maxCost = :maxCost " +
           "WHERE b.typeName = :typeName")
    void setMaxCostByTypeName(String typeName, Double maxCost);

    @Modifying
    @Query("UPDATE BudgetByCostType b SET b.remainingBudget = b.initialBudget " +
           "WHERE b.typeName = :typeName")
    void resetBudgetByTypeName(String typeName);

}
