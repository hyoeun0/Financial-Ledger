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
import java.util.List;

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
    public List<HistoryDTO> getCallendarData(HttpSession session){
        String loginID = (String) session.getAttribute("loginID");
        return historyService.findAllByLoginId(loginID);
    }

    @GetMapping("/money/summary")
    @ResponseBody
    public List<SummaryDTO> getSummary(HttpSession session){
        String loginID = (String) session.getAttribute("loginID");
        if(loginID != null){
            return historyService.getSummary("loginID");
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

}
