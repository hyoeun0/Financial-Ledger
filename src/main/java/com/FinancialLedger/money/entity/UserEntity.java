package com.FinancialLedger.money.entity;

import com.FinancialLedger.money.dto.UserDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "user_table")
public class UserEntity {
    @Id // pk(기본키) 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto_increment
    private Long id;

    @Column(unique = true) //unique 제약조건 추가
    private String userID;

    @Column
    private String userPassword;

    @Column
    private String userName;

    public static UserEntity toUserEntity(UserDTO userDTO){
        UserEntity userEntity = new UserEntity();
        userEntity.setUserID(userDTO.getUserID());
        userEntity.setUserPassword(userDTO.getUserPassword());
        userEntity.setUserName(userDTO.getUserName());
        return userEntity;
    }
}
