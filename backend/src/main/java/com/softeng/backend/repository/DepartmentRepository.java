package com.softeng.backend.repository;

import com.softeng.backend.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.Query;


@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    Optional<Department> findByDeptname(String deptname);
    boolean existsByDeptname(String deptname);
    boolean existsByDeptId(Integer deptId);
    List<Department> findAll();

    @Modifying
    @Query(value = "UPDATE departments SET deptManager = :personalNo WHERE deptId = :deptId", nativeQuery = true)
    void setManager(@Param("deptId") Integer deptId, @Param("personalNo") String personalNo);
   @Modifying
    @Query("UPDATE Department d SET d.remainingBudget = :remainingBudget " +
           "WHERE d.deptId = :deptId")
    void setDepartmentRemainingBudget(@Param("deptId") Integer deptId, @Param("remainingBudget") Double remainingBudget);

    @Modifying
    @Query("UPDATE Department d SET d.initialBudget = :initialBudget " +
           "WHERE d.deptId = :deptId")
    void setDepartmentInitialBudget(@Param("deptId") Integer deptId, @Param("initialBudget") Double initialBudget);

    @Modifying
    @Query("UPDATE Department d SET d.remainingBudget = d.initialBudget " +
           "WHERE d.deptId = :deptId")
    void resetDepartmentBudget(@Param("deptId") Integer deptId);

}