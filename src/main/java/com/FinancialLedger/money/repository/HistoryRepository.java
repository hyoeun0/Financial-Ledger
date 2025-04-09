package com.FinancialLedger.money.repository;

import com.FinancialLedger.money.entity.HistoryEntity;
import com.FinancialLedger.money.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryRepository extends JpaRepository<HistoryEntity, Long> {
    List<HistoryEntity> findByUserID(UserEntity UserID);

}
