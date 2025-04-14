package com.FinancialLedger.money.controller;

import org.springframework.ui.Model;
import com.FinancialLedger.money.dto.HistoryDTO;
import com.FinancialLedger.money.dto.SummaryDTO;
import com.FinancialLedger.money.service.HistoryService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class HistoryController {
    private final HistoryService historyService;

    @GetMapping("/money/main")
    public String mainForm(HttpSession session, Model model) {
        String loginID = (String) session.getAttribute("loginID");
        if (loginID != null){
            // 상세 내역 조회
            List<HistoryDTO> historyList = historyService.findAllByLoginId(loginID);
            model.addAttribute("historyList", historyList);

            // 일별 요약 데이터 조회
            List<SummaryDTO> summary = historyService.getSummary(loginID);
            model.addAttribute("summary", summary);

            model.addAttribute("calendarEvents", prepareCalendarEvents(historyList, summary));
            // 날짜별 상세 내역 맵 생성
            model.addAttribute("dateDetailMap", prepareDateDetailMap(historyList));
        }

        return "main";
    }

    @GetMapping("/money/index")
    public String indexForm() {
        return "index";
    }

    @PostMapping("/money/main")
    public String historySave(@ModelAttribute HistoryDTO historyDTO, HttpSession session){
        String loginID = (String) session.getAttribute("loginID");
        historyService.historySave(historyDTO, loginID);

        return "redirect:/money/main";
    }

    @GetMapping("/money/calendar-data")
    @ResponseBody
    public Map<String, Object> getCalendarData(HttpSession session){
        String loginID = (String) session.getAttribute("loginID");
        List<HistoryDTO> historyList = historyService.findAllByLoginId(loginID);
        List<SummaryDTO> summaryList = historyService.getSummary(loginID);

        Map<String, Object> result = new HashMap<>();
        result.put("events", prepareCalendarEvents(historyList, summaryList));
        result.put("dateDetailMap", prepareDateDetailMap(historyList));

        return result;
    }

    @GetMapping("/money/summary")
    @ResponseBody
    public List<SummaryDTO> getSummary(HttpSession session){
        String loginID = (String) session.getAttribute("loginID");
        if(loginID != null){
            return historyService.getSummary(loginID);
        }
        return  new ArrayList<>();
    }


    @GetMapping("/money/detail")
    public String detailForm(HttpSession session, Model model,
                             @RequestParam(value = "year", required = false) Integer year,
                             @RequestParam(value = "month", required = false) Integer month) {
        String loginID = (String) session.getAttribute("loginID");
        if (loginID != null) {
            // 현재 날짜 정보 가져오기
            LocalDate now = LocalDate.now();
            int currentYear = year != null ? year : now.getYear();
            int currentMonth = month != null ? month : now.getMonthValue();

            // 필터링된 내역 조회
            List<HistoryDTO> historyList = historyService.findByYearAndMonth(loginID, currentYear, currentMonth);
            model.addAttribute("historyList", historyList);

            // 월별 요약 정보 조회
            SummaryDTO monthlySummary = historyService.getMonthlySummary(loginID, currentYear, currentMonth);
            model.addAttribute("summary", monthlySummary);

            // 연도 목록 (현재 연도 기준 5년 전부터)
            List<Integer> years = new ArrayList<>();
            for (int y = now.getYear() - 5; y <= now.getYear(); y++) {
                years.add(y);
            }
            model.addAttribute("years", years);

            // 선택된 연도와 월 전달
            model.addAttribute("selectedYear", currentYear);
            model.addAttribute("selectedMonth", currentMonth);
        }
        return "detail";
    }

    // 달력 이벤트 데이터 준비 메서드
    private List<Map<String, Object>> prepareCalendarEvents(List<HistoryDTO> historyList, List<SummaryDTO> summaryList) {
        List<Map<String, Object>> events = new ArrayList<>();

        // 일별 요약 정보로 이벤트 생성
        for (SummaryDTO summary : summaryList) {
            // 수입 요약 이벤트
            if (summary.getTotalIncome() > 0) {
                Map<String, Object> incomeEvent = new HashMap<>();
                incomeEvent.put("title", "총 수입: " + summary.getTotalIncome() + "원");
                incomeEvent.put("start", summary.getDate());
                incomeEvent.put("className", "summary income-summary");
                incomeEvent.put("display", "block");

                Map<String, String> extendedProps = new HashMap<>();
                extendedProps.put("type", "income-summary");
                extendedProps.put("date", summary.getDate());
                incomeEvent.put("extendedProps", extendedProps);

                events.add(incomeEvent);
            }
            // 지출 요약 이벤트
            if (summary.getTotalExpense() > 0) {
                Map<String, Object> expenseEvent = new HashMap<>();
                expenseEvent.put("title", "총 지출: " + summary.getTotalExpense() + "원");
                expenseEvent.put("start", summary.getDate());
                expenseEvent.put("className", "summary expense-summary");
                expenseEvent.put("display", "block");

                Map<String, String> extendedProps = new HashMap<>();
                extendedProps.put("type", "expense-summary");
                extendedProps.put("date", summary.getDate());
                expenseEvent.put("extendedProps", extendedProps);

                events.add(expenseEvent);
            }
            // 합계 이벤트
            Map<String, Object> balanceEvent = new HashMap<>();
            balanceEvent.put("title", "합계: " + summary.getBalance() + "원");
            balanceEvent.put("start", summary.getDate());
            balanceEvent.put("className", "summary balance-summary");
            balanceEvent.put("display", "block");

            Map<String, String> extendedProps = new HashMap<>();
            extendedProps.put("type", "balance-summary");
            extendedProps.put("date", summary.getDate());
            balanceEvent.put("extendedProps", extendedProps);

            events.add(balanceEvent);
        }
        return events;
    }

    // 날짜별 상세 내역 맵 생성 메서드
    private Map<String, Map<String, List<HistoryDTO>>> prepareDateDetailMap(List<HistoryDTO> historyList) {
        Map<String, Map<String, List<HistoryDTO>>> dateDetailMap = new HashMap<>();

        for (HistoryDTO history : historyList) {
            String date = history.getHistoryDate().toString();
            // 날짜별 상세 내역 맵 초기화
            if (!dateDetailMap.containsKey(date)) {
                Map<String, List<HistoryDTO>> typeMap = new HashMap<>();
                typeMap.put("income", new ArrayList<>());
                typeMap.put("expense", new ArrayList<>());
                dateDetailMap.put(date, typeMap);
            }
            // 수입/지출에 따라 분류하여 저장
            if ("수입".equals(history.getHistoryType())) {
                dateDetailMap.get(date).get("income").add(history);
            } else if ("지출".equals(history.getHistoryType())) {
                dateDetailMap.get(date).get("expense").add(history);
            }
        }
        return dateDetailMap;
    }
    // 날짜별 세부 내역 조회 API
    @GetMapping("/money/detail-by-date")
    @ResponseBody
    public Map<String, Object> getDetailByDate(@RequestParam("date") String date, HttpSession session) {
        String loginID = (String) session.getAttribute("loginID");
        List<HistoryDTO> historyList = historyService.findAllByLoginId(loginID);

        List<HistoryDTO> incomeList = new ArrayList<>();
        List<HistoryDTO> expenseList = new ArrayList<>();

        for (HistoryDTO history : historyList) {
            if (history.getHistoryDate().toString().equals(date)) {
                if ("수입".equals(history.getHistoryType())) {
                    incomeList.add(history);
                } else if ("지출".equals(history.getHistoryType())) {
                    expenseList.add(history);
                }
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("income", incomeList);
        result.put("expense", expenseList);

        return result;
    }

    // Detail 페이지에서 내역 추가
    @PostMapping("/money/detail/add")
    public String detailAdd(@ModelAttribute HistoryDTO historyDTO,
                            @RequestParam("year") Integer year,
                            @RequestParam("month") Integer month,
                            HttpSession session){
        String loginID = (String) session.getAttribute("loginID");
        historyService.historySave(historyDTO, loginID);

        // 기존 필터링 조건(연도, 월)을 유지하면서 리다이렉트
        return "redirect:/money/detail?year=" + year + "&month=" + month;
    }

    @GetMapping("/money/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, HttpSession session,
                           @RequestParam("year") Integer year,
                           @RequestParam("month") Integer month){
        String loginID = (String) session.getAttribute("loginID");
        if (loginID != null) {
            HistoryDTO historyDTO = historyService.findById(id);
            if (historyDTO != null) {
                model.addAttribute("history", historyDTO);
                model.addAttribute("year", year);
                model.addAttribute("month", month);
                return "edit"; // 뷰 이름 반환
            }
        }
        return "redirect:/money/main";
    }

    @PostMapping("/money/edit/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute HistoryDTO historyDTO,
                         @RequestParam("year") Integer year,
                         @RequestParam("month") Integer month){
        historyService.update(id, historyDTO);
        return "redirect:/money/detail?year=" + year + "&month=" + month;
    }
}
