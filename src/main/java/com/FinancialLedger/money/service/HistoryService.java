package com.FinancialLedger.money.service;

import com.FinancialLedger.money.dto.HistoryDTO;
import com.FinancialLedger.money.dto.SummaryDTO;
import com.FinancialLedger.money.entity.HistoryEntity;
import com.FinancialLedger.money.entity.UserEntity;
import com.FinancialLedger.money.repository.HistoryRepository;
import com.FinancialLedger.money.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

    public List<HistoryDTO> findAllByLoginId(String loginID) {
        Optional<UserEntity> userEntityOptional = userRepository.findByUserID(loginID);
        if (userEntityOptional.isPresent()){
            UserEntity userEntity = userEntityOptional.get();
            List<HistoryEntity> historyEntityList = historyRepository.findByUserID(userEntity);
            List<HistoryDTO> historyDTOList = new ArrayList<>();

            for (HistoryEntity historyEntity : historyEntityList){
                historyDTOList.add(HistoryDTO.toHistoryDTO((historyEntity)));
            }
            return historyDTOList;
        } else {
            return new ArrayList<>();
        }
    }
    // 날짜별 집계 데이터 생성
    public List<SummaryDTO> getSummary(String loginID) {
        List<HistoryDTO> allHistory = findAllByLoginId(loginID);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE; // yyyy-mm-dd 형식

        // 날짜별 그룹화 집계
        Map<LocalDate, SummaryDTO> summaryMap = new HashMap<>();

        for (HistoryDTO history : allHistory){
            LocalDate date = history.getHistoryDate();
            String dateStr = date.format(formatter); // LocalDate String 형 변환

            // 해당 날짜의 요약 객체가 없으면 생성
            if(!summaryMap.containsKey(date)){
                summaryMap.put(date, new SummaryDTO(dateStr, 0, 0));
            }
            SummaryDTO summary = summaryMap.get(date);

            // 하루 수입, 지출 합계
            if("수입".equals(history.getHistoryType())){
                summary.setTotalIncome(summary.getTotalIncome() + history.getHistoryMoney());
            } else if ("지출".equals(history.getHistoryType())) {
                summary.setTotalExpense(summary.getTotalExpense() + history.getHistoryMoney());
            }
        }
        // Map에서 List로 변환하여 반환
        return new ArrayList<>(summaryMap.values());
    }

    // 특정 연월에 해당하는 내역 조회
    public List<HistoryDTO> findByYearAndMonth(String loginID, int year, int month) {
        List<HistoryDTO> allHistory = findAllByLoginId(loginID);
        List<HistoryDTO> filteredHistory = new ArrayList<>();

        for (HistoryDTO history : allHistory) {
            LocalDate historyDate = history.getHistoryDate();
            if (historyDate.getYear() == year && historyDate.getMonthValue() == month) {
                filteredHistory.add(history);
            }
        }

        return filteredHistory;
    }

    // 특정 연월의 요약 데이터 생성
    public SummaryDTO getMonthlySummary(String loginID, int year, int month) {
        List<HistoryDTO> allHistory = findAllByLoginId(loginID);
        int totalIncome = 0;
        int totalExpense = 0;

        for (HistoryDTO history : allHistory) {
            LocalDate date = history.getHistoryDate();

            if (date.getYear() == year && date.getMonthValue() == month) {
                if ("수입".equals(history.getHistoryType())) {
                    totalIncome += history.getHistoryMoney();
                } else if ("지출".equals(history.getHistoryType())) {
                    totalExpense += history.getHistoryMoney();
                }
            }
        }

        String dateStr = year + "-" + (month < 10 ? "0" + month : month);
        return new SummaryDTO(dateStr, totalIncome, totalExpense);
    }

    // 특정 날짜의 상세 내역 조회
    public Map<String, List<HistoryDTO>> findByDate(String loginID, LocalDate date) {
        List<HistoryDTO> allHistory = findAllByLoginId(loginID);
        List<HistoryDTO> incomeList = new ArrayList<>();
        List<HistoryDTO> expenseList = new ArrayList<>();

        for (HistoryDTO history : allHistory) {
            if (history.getHistoryDate().equals(date)) {
                if ("수입".equals(history.getHistoryType())) {
                    incomeList.add(history);
                } else if ("지출".equals(history.getHistoryType())) {
                    expenseList.add(history);
                }
            }
        }

        Map<String, List<HistoryDTO>> result = new HashMap<>();
        result.put("income", incomeList);
        result.put("expense", expenseList);

        return result;
    }
}
