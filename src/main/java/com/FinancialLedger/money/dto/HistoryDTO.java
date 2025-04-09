package com.FinancialLedger.money.dto;

import com.FinancialLedger.money.entity.HistoryEntity;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HistoryDTO {
    private Long id;
    private String historyTitle;
    private int historyMoney;
    private String historyType;
    private LocalDate historyDate;

    public static HistoryDTO toHistoryDTO(HistoryEntity historyEntity){
        HistoryDTO historyDTO = new HistoryDTO();
        historyDTO.setId(historyEntity.getId());
        historyDTO.setHistoryTitle(historyEntity.getHistoryTitle());
        historyDTO.setHistoryMoney(historyEntity.getHistoryMoney());
        historyDTO.setHistoryType(historyEntity.getHistoryType());
        historyDTO.setHistoryDate(historyEntity.getHistoryDate());
        return historyDTO;
    }
}
