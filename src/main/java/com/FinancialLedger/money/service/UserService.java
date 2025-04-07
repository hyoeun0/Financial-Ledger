package com.FinancialLedger.money.service;

import com.FinancialLedger.money.dto.UserDTO;
import com.FinancialLedger.money.entity.UserEntity;
import com.FinancialLedger.money.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public void sign(UserDTO userDTO) {
        /*
            1. dto -> entity 변환
            2. repository의 sign 메서드 호출
            repository의 save 메서드 호출 ( 조건. entity 객체를 넘겨줘야 함 )
         */
        UserEntity userEntity = UserEntity.toUserEntity(userDTO);
        userRepository.save(userEntity); // save() jpa 제공 메서드
    }
}
