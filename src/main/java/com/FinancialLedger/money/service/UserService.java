package com.FinancialLedger.money.service;

import com.FinancialLedger.money.dto.UserDTO;
import com.FinancialLedger.money.entity.UserEntity;
import com.FinancialLedger.money.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
    public UserDTO login(UserDTO userDTO) {
        /*
            1. 회원이 입력한 아이디로 DB에서 조회를 함
            2. DB에서 조회한 비밀번호와 사용자가 입력한 비밀번호가 일치하는지 판단
         */
        Optional<UserEntity> byUserID = userRepository.findByUserID(userDTO.getUserID());
        if (byUserID.isPresent()){
            // 조회 결과가 있다 (해당 아이디를 가진 회원 정보가 있다)
            UserEntity userEntity = byUserID.get();
            if(userEntity.getUserPassword().equals(userDTO.getUserPassword())){
                // 비밀번호 일치
                // entity -> dto 변환 후 리턴
                UserDTO dto = UserDTO.toUserDTO(userEntity);
                return dto;
            } else {
                // 비밀번호 불일치 (로그인 실패)
                return null;
            }
        } else {
            // 조회 결과가 없다 (해당 아이디를 가진 회원이 없다)
            return null;
        }
    }

    public String idCheck(String userID) {
        Optional<UserEntity> byUserID = userRepository.findByUserID(userID);
        if (byUserID.isPresent()){
            // 조회결과가 있다 -> 사용할 수 없다.
            return null;
        } else {
            // 조회결과가 없다 -> 사용할 수 있다.
            return "ok";
        }
    }
}
