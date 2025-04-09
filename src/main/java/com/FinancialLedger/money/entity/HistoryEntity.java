package com.FinancialLedger.money.entity;

import com.FinancialLedger.money.dto.HistoryDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Setter
@Getter
@Table(name = "history_table")
public class HistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String historyTitle;

    @Column
    private int historyMoney;

    @Column
    private String historyType;

    @Column
    private LocalDate historyDate;

    public static HistoryEntity toHistorySaveEntity(HistoryDTO historyDTO) {
        HistoryEntity historyEntity = new HistoryEntity();
        historyEntity.setHistoryTitle(historyDTO.getHistoryTitle());
        historyEntity.setHistoryMoney(historyDTO.getHistoryMoney());
        historyEntity.setHistoryType(historyDTO.getHistoryType());
        historyEntity.setHistoryDate(historyDTO.getHistoryDate());
        return historyEntity;
    }
}
