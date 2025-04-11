package com.FinancialLedger.money.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SummaryDTO {
    private String date;
    private int totalIncome; // 총 수입
    private int totalExpense; // 총 지출

    public int getBalance() {
        return totalIncome - totalExpense;
    }
}
