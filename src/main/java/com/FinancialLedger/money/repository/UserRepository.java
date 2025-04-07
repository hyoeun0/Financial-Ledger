package com.FinancialLedger.money.repository;

import com.FinancialLedger.money.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

}