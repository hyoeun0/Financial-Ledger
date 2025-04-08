package com.FinancialLedger.money.repository;

import com.FinancialLedger.money.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    // 아이디로 회원 정보 조회 (select * from user_table where userID=?)
    Optional<UserEntity> findByUserID(String userID);
}