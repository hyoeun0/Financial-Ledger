package com.FinancialLedger.money.service;

import com.FinancialLedger.money.dto.HistoryDTO;
import com.FinancialLedger.money.entity.HistoryEntity;
import com.FinancialLedger.money.entity.UserEntity;
import com.FinancialLedger.money.repository.HistoryRepository;
import com.FinancialLedger.money.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HistoryService {
    private final HistoryRepository historyRepository;
    private final UserRepository userRepository;

    public void historySave(HistoryDTO historyDTO, String loginID) {
        Optional<UserEntity> userEntity = userRepository.findByUserID(loginID);
        if (userEntity.isPresent()) {
            HistoryEntity historyEntity = HistoryEntity.toHistorySaveEntity(historyDTO, userEntity.orElse(null));
            historyRepository.save(historyEntity); // save() jpa 제공 메서드
        }
    }
    @Transactional
    public HistoryDTO historyFindById(Long id) {
        Optional<HistoryEntity> optionalHistoryEntity = historyRepository.findById(id);
        return optionalHistoryEntity.map(HistoryDTO::toHistoryDTO).orElse(null);
    }
}
