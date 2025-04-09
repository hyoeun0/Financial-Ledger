package com.FinancialLedger.money.service;

import com.FinancialLedger.money.dto.HistoryDTO;
import com.FinancialLedger.money.entity.HistoryEntity;
import com.FinancialLedger.money.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HistoryService {
    private final HistoryRepository historyRepository;

    public void historySave(HistoryDTO historyDTO) {
        HistoryEntity historyEntity = HistoryEntity.toHistorySaveEntity(historyDTO);
        historyRepository.save(historyEntity); // save() jpa 제공 메서드
    }
}
