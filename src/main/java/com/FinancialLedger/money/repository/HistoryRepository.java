package com.FinancialLedger.money.repository;

import com.FinancialLedger.money.entity.HistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<HistoryEntity, Long> {

}
