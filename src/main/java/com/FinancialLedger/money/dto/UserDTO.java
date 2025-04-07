package com.FinancialLedger.money.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDTO {
    private Long id;
    private String userID;
    private String userPassword;
    private String userName;
}
